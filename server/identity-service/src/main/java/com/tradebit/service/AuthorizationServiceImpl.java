package com.tradebit.service;

import com.tradebit.RabbitMQMessageProducer;
import com.tradebit.config.KeycloakProvider;
import com.tradebit.exception.AccountNotVerifiedException;
import com.tradebit.exception.InternalErrorException;
import com.tradebit.exception.InvalidCredentialsException;
import com.tradebit.dto.AuthorizationDTO;
import com.tradebit.requests.EmailRequest;
import com.tradebit.resetToken.ResetToken;
import com.tradebit.resetToken.ResetTokenService;
import com.tradebit.responses.TokenResponse;
import com.tradebit.user.models.EmailType;
import com.tradebit.user.models.User;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotAuthorizedException;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorizationServiceImpl implements AuthorizationService{
    @Value("${keycloak.realm}")
    public String realm;
    private final KeycloakProvider kcProvider;
    private final KeycloakService keycloakService;
    private final RabbitMQMessageProducer messageProducer;
    private final ResetTokenService resetTokenService;

    @Override
    public TokenResponse login(AuthorizationDTO authorizationDTO) {
        Keycloak keycloak = kcProvider.getInstance();

        // Check if the user's email is verified
        UsersResource usersResource = keycloak.realm(realm).users();
        List<UserRepresentation> users = usersResource.search(authorizationDTO.getEmail(), true);
        if (users.isEmpty()) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        UserRepresentation user = users.get(0);

        // Proceed to attempt login
        try {
            keycloak = kcProvider.newKeycloakBuilderWithPasswordCredentials(authorizationDTO.getEmail(), authorizationDTO.getPassword()).build();
            AccessTokenResponse accessTokenResponse = keycloak.tokenManager().getAccessToken();

            if (!user.isEmailVerified()) {
                throw new AccountNotVerifiedException();
            }

            return generateTokenResponse(accessTokenResponse, user);
        } catch (NotAuthorizedException ex) {
            throw new InvalidCredentialsException("Invalid credentials");
        } catch (BadRequestException ex) {
            throw new AccountNotVerifiedException();
        } catch (IllegalStateException ex){
            if (ex.getMessage().equals("username required"))
                throw new InvalidCredentialsException("email required");
            else
                throw new InvalidCredentialsException(ex.getMessage());
        } catch (Exception ex){
            throw new InternalErrorException(ex.getMessage());
        }
    }

    private TokenResponse generateTokenResponse(AccessTokenResponse accessTokenResponse, UserRepresentation user){
        TokenResponse response = TokenResponse.builder()
                .access_token(accessTokenResponse.getToken())
                .expires_in(accessTokenResponse.getExpiresIn())
                .refresh_token(accessTokenResponse.getRefreshToken())
                .refresh_expires_in(accessTokenResponse.getRefreshExpiresIn())
                .token_type(accessTokenResponse.getTokenType())
                .userId(user.getId())
                .build();

        return response;
    }

    public void forgotPassword(String email){
        User user = keycloakService.userExists(email);
        if (user != null){
            ResetToken existing = resetTokenService.findByUser(user);
            ResetToken resetToken;
            if (existing != null) {
                if (existing.isTokenValid()) {
                    existing.setExpiryDate(existing.calculateExpiryDate(ResetToken.EXPIRATION));
                    resetToken = resetTokenService.save(existing);
                } else {
                    resetToken = resetTokenService.generateResetToken(user);
                }
            }else {
                resetToken = resetTokenService.generateResetToken(user);
            }
            sendResetPasswordLink(email, resetToken.getToken());
        }
    }

    private void sendResetPasswordLink(String email, String token){
        messageProducer.publish(
                generateEmailRequest(email, token, EmailType.RESET_PASSWORD_EMAIL),
                "internal.exchange",
                "internal.email.routing-key"
        );
    }

    private EmailRequest generateEmailRequest(String to, String token, EmailType emailType) {
        return EmailRequest.builder()
                .to(to)
                .message(token)
                .emailType(emailType)
                .build();
    }

}
