package com.tradebit.service;

import com.tradebit.RabbitMQMessageProducer;
import com.tradebit.config.KeycloakProvider;
import com.tradebit.exception.InvalidTokenException;
import com.tradebit.exception.UserCreationException;
import com.tradebit.exception.UserNotFoundException;
import com.tradebit.http.requests.EmailRequest;
import com.tradebit.http.requests.RegistrationRequest;
import com.tradebit.user.models.Role;
import com.tradebit.user.models.User;
import com.tradebit.user.repositories.UserRepository;
import com.tradebit.verificationToken.VerificationToken;
import com.tradebit.verificationToken.VerificationTokenService;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RoleMappingResource;
import org.keycloak.admin.client.resource.RoleScopeResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;


@Service
@RequiredArgsConstructor
@Slf4j
public class RegistrationServiceImpl implements RegistrationService {
    @Value("${keycloak.realm}")
    public String realm;
    @Value("${keycloak.resource}")
    private String clientId;

    private final KeycloakProvider kcProvider;
    private final UserRepository userRepository;
    private final VerificationTokenService verificationTokenService;
    private final RabbitMQMessageProducer messageProducer;


    @Override
    public ResponseEntity<?> register(RegistrationRequest registrationRequest) {
        try {
            UsersResource usersResource = kcProvider.getInstance().realm(realm).users();
            UserRepresentation kcUser = createKeycloakUser(registrationRequest);
            Response response = usersResource.create(kcUser);
            if (response.getStatus() == 201) {
                String userId = extractUserId(response);
                assignClientRole(userId, usersResource);
                User user = getUserFromRepresentation(kcUser, userId, Role.USER);
                sendVerificationLink(user);
                return new ResponseEntity<>(Map.of("status", "success",
                        "message", "User has been registered successfully!"),
                        HttpStatus.CREATED);
            }
            else {
                String errorMessage = new JSONObject(response.readEntity(String.class)).getString("errorMessage");
                throw new UserCreationException(errorMessage, response.getStatus());
            }
        }catch (WebApplicationException e){
            String errorMessage = e.getResponse().readEntity(String.class);
            throw new UserCreationException(errorMessage, e.getResponse().getStatus());

        }
    }

    private void sendVerificationLink(User user){
        VerificationToken verificationToken = verificationTokenService.generateVerificationToken(user);

        messageProducer.publish(
                generateEmailRequest(user.getEmail(), verificationToken.getToken()),
                "internal.exchange",
                "internal.email.routing-key"
        );
    }

    private String assignClientRole(String userId, UsersResource usersResource){
        // Fetch the client UUID from Keycloak
        String clientUuid = kcProvider.getInstance().realm(realm).clients().findByClientId(clientId).get(0).getId();

        RoleMappingResource roleMappingResource = usersResource.get(userId).roles();
        RoleScopeResource clientRolesResource = roleMappingResource.clientLevel(clientUuid);

        // Get existing client roles
        List<RoleRepresentation> availableRoles = clientRolesResource.listAvailable();

        // Find the roles you want to assign
        List<RoleRepresentation> rolesToAdd = availableRoles.stream()
                .filter(role -> role.getName().equals("ADMIN")) // Change to the desired role name
                .collect(Collectors.toList());

        // Add the roles to the user
        clientRolesResource.add(rolesToAdd);
        return rolesToAdd
                .stream()
                .findFirst()
                .map(RoleRepresentation::getName)
                .orElse(null);
    }


    public UserRepresentation createKeycloakUser(RegistrationRequest user) {
        CredentialRepresentation credentialRepresentation = createPasswordCredentials(user.getPassword());

        UserRepresentation kcUser = new UserRepresentation();
        kcUser.setUsername(user.getEmail());
        kcUser.setCredentials(Collections.singletonList(credentialRepresentation));
        kcUser.setFirstName(user.getFirstname());
        kcUser.setLastName(user.getLastname());
        kcUser.setEmail(user.getEmail());
        kcUser.setEnabled(true);
        kcUser.setEmailVerified(false);
        kcUser.setClientRoles(Map.of("tradebit", List.of(Role.USER.name())));

        return kcUser;
    }

    private EmailRequest generateEmailRequest(String to, String token) {
        return EmailRequest.builder()
                .to(to)
                .message(token)
                .build();
    }

    public ResponseEntity<Map<String, String>> confirmRegistration(String token){
        VerificationToken verificationToken = verificationTokenService.getVerificationToken(token);

        if (verificationToken == null)
            throw new InvalidTokenException("Invalid token");
        else if (verificationToken.getExpiryDate().before(new Date()))
            throw new InvalidTokenException("Token has expired");

        User user = verificationToken.getUser();
        if (user == null)
            throw new UserNotFoundException("User with a given token doesn't exist");

        user.setEmailVerified(true);
        setKeycloakEmailVerified(user.getId());
        verificationTokenService.deleteVerificationToken(verificationToken);
        userRepository.save(user);

        return new ResponseEntity<>(Map.of("status", "success",
                                           "message", "User email verified successfully. Please log in to continue."),
                                    HttpStatus.OK);
    }


    private void setKeycloakEmailVerified(String userId){
        UserResource userResource = kcProvider.getInstance().realm(realm).users().get(userId);
        UserRepresentation userRepresentation = userResource.toRepresentation();
        userRepresentation.setEmailVerified(true);
        List<String> requiredActions = userRepresentation.getRequiredActions();
        requiredActions.remove("VERIFY_EMAIL");
        userRepresentation.setRequiredActions(requiredActions);
        userResource.update(userRepresentation);
    }


    private String extractUserId(Response response) {
        URI location = response.getLocation();
        if (location == null) {
            throw new IllegalStateException("No Location header present in response");
        }
        String path = location.getPath();
        return path.substring(path.lastIndexOf('/') + 1);
    }

    private User getUserFromRepresentation(UserRepresentation kcUser, String userId, Role role){
        User user = User.builder()
                .email(kcUser.getEmail())
                .id(userId)
                .firstName(kcUser.getFirstName())
                .lastName(kcUser.getLastName())
                .role(role)
                .enabled(kcUser.isEnabled())
                .emailVerified(kcUser.isEmailVerified())
                .build();

        return user;
    }

    private static CredentialRepresentation createPasswordCredentials(String password) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        return passwordCredentials;
    }

}
