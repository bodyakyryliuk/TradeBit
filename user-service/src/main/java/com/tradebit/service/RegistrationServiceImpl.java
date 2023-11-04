package com.tradebit.service;

import com.tradebit.config.KeycloakProvider;
import com.tradebit.exception.InvalidTokenException;
import com.tradebit.exception.UserNotFoundException;
import com.tradebit.http.requests.EmailRequest;
import com.tradebit.http.requests.RegistrationRequest;
import com.tradebit.user.models.Role;
import com.tradebit.user.models.User;
import com.tradebit.user.repositories.UserRepository;
import com.tradebit.verificationToken.VerificationToken;
import com.tradebit.verificationToken.VerificationTokenService;
import com.tradebit.RabbitMQMessageProducer;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.resource.RoleMappingResource;
import org.keycloak.admin.client.resource.RoleScopeResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class RegistrationServiceImpl implements RegistrationService {
    @Value("${keycloak.realm}")
    public String realm;

    private final KeycloakProvider kcProvider;
    private final UserRepository userRepository;
    private final VerificationTokenService verificationTokenService;
    private final RabbitMQMessageProducer messageProducer;


    @Override
    public Response register(RegistrationRequest registrationRequest) {
        UsersResource usersResource = kcProvider.getInstance().realm(realm).users();
        UserRepresentation kcUser = createKeycloakUser(registrationRequest);
        Response response = usersResource.create(kcUser);

        if (response.getStatus() == 201) {
            String userId = extractUserId(response);
            String role = assignRole(userId, usersResource);
            User user = saveUserInDB(kcUser, userId, role);
            VerificationToken verificationToken = verificationTokenService.generateVerificationToken(user);

            messageProducer.publish(
                    generateEmailRequest(user.getEmail(), verificationToken.getToken()),
                    "internal.exchange",
                    "internal.email.routing-key"
            );
        }

        return response;
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

        return kcUser;
    }

    private EmailRequest generateEmailRequest(String to, String token) {
        return EmailRequest.builder()
                .to(to)
                .message(token)
                .build();
    }

    private String assignRole(String userId, UsersResource usersResource){
        RoleMappingResource roleMappingResource = usersResource.get(userId).roles();
        RoleScopeResource realmRolesResource = roleMappingResource.realmLevel();

        // Get existing realm roles
        List<RoleRepresentation> availableRoles = realmRolesResource.listAvailable();

        // Find the roles you want to assign
        List<RoleRepresentation> rolesToAdd = availableRoles.stream()
                .filter(role -> role.getName().equals("USER"))
                .collect(Collectors.toList());

        // Add the roles to the user
        realmRolesResource.add(rolesToAdd);
        return rolesToAdd
                        .stream()
                        .findFirst()
                        .map(RoleRepresentation::getName)
                        .orElse(null);
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
        verificationTokenService.deleteVerificationToken(verificationToken);
        userRepository.save(user);

        return new ResponseEntity<>(Map.of("status", "success",
                                           "mmessage", "User email verified successfully. Please log in to continue."),
                                    HttpStatus.OK);

    }

    private List<String> getUserRoles(String userId, UsersResource usersResource){
        List<RoleRepresentation> assignedRoles = usersResource.get(userId).roles().realmLevel().listEffective();

        // Extract role names from the assigned roles
        return assignedRoles.stream()
                .map(RoleRepresentation::getName)
                .collect(Collectors.toList());
    }

    private String extractUserId(Response response) {
        URI location = response.getLocation();
        if (location == null) {
            throw new IllegalStateException("No Location header present in response");
        }
        String path = location.getPath();
        return path.substring(path.lastIndexOf('/') + 1);
    }

    private User saveUserInDB(UserRepresentation kcUser, String userId, String role){
        User user = User.builder()
                .email(kcUser.getEmail())
                .id(userId)
                .firstName(kcUser.getFirstName())
                .lastName(kcUser.getLastName())
                .role(mapRole(role))
                .enabled(kcUser.isEnabled())
                .emailVerified(kcUser.isEmailVerified())
                .build();

        userRepository.save(user);
        log.info("User {} saved to database!", user.getEmail());
        return user;
    }

    private Role mapRole(String roleName){
        return Role.valueOf(roleName);
    }

    private static CredentialRepresentation createPasswordCredentials(String password) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        return passwordCredentials;
    }

}
