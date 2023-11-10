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
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.IDToken;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.*;


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
        try {
            UsersResource usersResource = kcProvider.getInstance().realm(realm).users();
            UserRepresentation kcUser = createKeycloakUser(registrationRequest);
            Response response = usersResource.create(kcUser);
            if (response.getStatus() == 201) {
                String userId = extractUserId(response);
                String role = Role.USER.name();
                User user = getUserFromRepresentation(kcUser, userId, role);
                sendVerificationLink(user);

                return response;
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

    @Override
    public ResponseEntity<?> processGoogleLogin(Authentication authentication) {
        String email = extractEmailFromAuthentication(authentication);
        Optional<User> optional = userRepository.findByEmail(email);
        if(optional.isPresent()){
            User user = optional.get();
            if (!user.isEmailVerified()){
                user.setEmailVerified(true);
                setKeycloakEmailVerified(user.getId());
                userRepository.save(user);
            }
        }else {
            User user = getUserFromAuthentication(authentication, Role.USER.name());
            userRepository.save(user);
        }
        return null;
    }

    public String extractEmailFromAuthentication(Authentication authentication) {
        if (authentication instanceof KeycloakAuthenticationToken) {
            KeycloakAuthenticationToken keycloakAuthenticationToken = (KeycloakAuthenticationToken) authentication;
            KeycloakPrincipal<?> keycloakPrincipal = (KeycloakPrincipal<?>) keycloakAuthenticationToken.getPrincipal();
            IDToken idToken = keycloakPrincipal.getKeycloakSecurityContext().getIdToken();
            return idToken.getEmail();
        }
        throw new IllegalStateException("Authentication token is not an instance of KeycloakAuthenticationToken");
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

    private User getUserFromRepresentation(UserRepresentation kcUser, String userId, String role){
        User user = User.builder()
                .email(kcUser.getEmail())
                .id(userId)
                .firstName(kcUser.getFirstName())
                .lastName(kcUser.getLastName())
                .role(mapRole(role))
                .enabled(kcUser.isEnabled())
                .emailVerified(kcUser.isEmailVerified())
                .build();

        return user;
    }

    private User getUserFromAuthentication(Authentication authentication, String role) {
        if (!(authentication instanceof KeycloakAuthenticationToken)) {
            throw new IllegalArgumentException("Authentication is not of type KeycloakAuthenticationToken");
        }

        KeycloakAuthenticationToken keycloakAuth = (KeycloakAuthenticationToken) authentication;
        KeycloakPrincipal<?> keycloakPrincipal = (KeycloakPrincipal<?>) keycloakAuth.getPrincipal();
        IDToken idToken = keycloakPrincipal.getKeycloakSecurityContext().getIdToken();

        // Extract user information from the IDToken
        String email = idToken.getEmail();
        String firstName = idToken.getGivenName();
        String lastName = idToken.getFamilyName();
        boolean emailVerified = true;
        boolean enabled = true;

        // Construct the user object
        User user = User.builder()
                .email(email)
                .id(UUID.randomUUID().toString()) // Generate a new UUID or set your own ID logic
                .firstName(firstName)
                .lastName(lastName)
                .role(mapRole(role))
                .enabled(enabled)
                .emailVerified(emailVerified)
                .build();

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
