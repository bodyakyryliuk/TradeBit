package com.tradebit.registration.services;

import com.tradebit.RabbitMQMessageProducer;
import com.tradebit.authentication.OAuth2TokenResponse;
import com.tradebit.config.KeycloakProviderConfig;
import com.tradebit.exception.InvalidTokenException;
import com.tradebit.exception.UserNotFoundException;
import com.tradebit.registration.EmailRequest;
import com.tradebit.registration.RegistrationRequest;
import com.tradebit.user.models.Role;
import com.tradebit.user.models.User;
import com.tradebit.user.repositories.UserRepository;
import com.tradebit.verificationToken.VerificationToken;
import com.tradebit.verificationToken.VerificationTokenService;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {
    @Value("${keycloak.realm}")
    public String realm;

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenService verificationTokenService;
    private final JwtService jwtService;
    private final RabbitMQMessageProducer messageProducer;
    private final KeycloakProviderConfig kcProvider;
    public Map<String, String> register(RegistrationRequest request) {
        HashMap<String, String> response = new HashMap<>();

        User user = createUserFromRequest(request);

        if (checkUserExists(user.getEmail())){
            response.put("status", "failure");
            response.put("message", "User already exists!");
            return response;
        }

        userRepository.save(user);

        VerificationToken verificationToken = verificationTokenService.generateVerificationToken(user);
        // TODO: create a email microservice with emailService class to send verification token

        messageProducer.publish(
                generateEmailRequest(user.getEmail(), verificationToken.getToken()),
                "internal.exchange",
                "internal.email.routing-key"
        );
        response.put("status", "success");
        response.put("message", "Registration successful! Please check your email to activate your account.");
        return response;
    }

    public Response createKeycloakUser(RegistrationRequest registrationRequest) {
        UsersResource usersResource = kcProvider.getInstance().realm(realm).users();
        CredentialRepresentation credentialRepresentation = createPasswordCredentials(registrationRequest.getPassword());

        UserRepresentation kcUser = new UserRepresentation();
        kcUser.setUsername(registrationRequest.getEmail());
        kcUser.setCredentials(Collections.singletonList(credentialRepresentation));
        kcUser.setFirstName(registrationRequest.getFirstName());
        kcUser.setLastName(registrationRequest.getLastName());
        kcUser.setEmail(registrationRequest.getEmail());
        kcUser.setEnabled(true);
        kcUser.setEmailVerified(false);


        Response response = usersResource.create(kcUser);

        if (response.getStatus() == 201) {
            saveUserInDB(kcUser);
        }

        return response;
    }

    private void saveUserInDB(UserRepresentation kcUser){
        User user = User.builder()
                .firstName(kcUser.getFirstName())
                .lastName(kcUser.getLastName())
                .email(kcUser.getEmail())
                .enabled(kcUser.isEnabled())
                .emailVerified(kcUser.isEmailVerified())
                .build();
        userRepository.save(user);
    }

    private static CredentialRepresentation createPasswordCredentials(String password) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        return passwordCredentials;
    }

    private EmailRequest generateEmailRequest(String to, String token) {
        return EmailRequest.builder()
                .to(to)
                .message(token)
                .build();
    }

    public OAuth2TokenResponse confirmRegistration(String token){
        VerificationToken verificationToken = verificationTokenService.getVerificationToken(token);

        if (verificationToken == null)
            throw new InvalidTokenException("Invalid token");
        else if (verificationToken.getExpiryDate().before(new Date()))
            throw new InvalidTokenException("Token has expired");
        User user = verificationToken.getUser();
        if (user == null)
            throw new UserNotFoundException("User with a given token doesn't exist");

        user.setEnabled(true);
        verificationTokenService.deleteVerificationToken(verificationToken);
        userRepository.save(user);

        String jwtToken = jwtService.generateToken(user);
        return OAuth2TokenResponse.builder()
                .accessToken(jwtToken)
                .build();
    }

    private boolean checkUserExists(String email){
        return userRepository.findByEmail(email).isPresent();
    }

    private User createUserFromRequest(RegistrationRequest request) {
        return User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .enabled(false)
                .build();
    }
}
