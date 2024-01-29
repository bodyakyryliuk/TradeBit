package com.tradebit.service;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.tradebit.config.KeycloakProvider;
import com.tradebit.exception.InternalErrorException;
import com.tradebit.exception.InvalidTokenException;
import com.tradebit.exception.UserNotFoundException;
import com.tradebit.dto.ResetPasswordDTO;
import com.tradebit.resetToken.ResetToken;
import com.tradebit.resetToken.ResetTokenService;
import com.tradebit.user.models.User;
import com.tradebit.user.services.UserService;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KeycloakServiceImpl implements KeycloakService{
    @Value("${keycloak.auth-server-url}")
    public String serverURL;
    @Value("${keycloak.realm}")
    public String realm;
    @Value("${keycloak.resource}")
    public String clientID;
    @Value("${keycloak.credentials.secret}")
    public String clientSecret;

    private final KeycloakProvider keycloakProvider;
    private final UserService userService;
    private final ResetTokenService resetTokenService;

    @Override
    public AccessTokenResponse refreshToken(String refreshToken) throws UnirestException {
        String url = serverURL + "/realms/" + realm + "/protocol/openid-connect/token";
        JsonNode jsonNode = Unirest.post(url)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .field("client_id", clientID)
                .field("client_secret", clientSecret)
                .field("refresh_token", refreshToken)
                .field("grant_type", "refresh_token")
                .asJson().getBody();

        return createTokenResponseFromJsonNode(jsonNode);

    }

    @Override
    public AccessTokenResponse createTokenResponseFromJsonNode(JsonNode jsonNode){
        JSONObject jsonObject = jsonNode.getObject();

        // Create an instance of AccessTokenResponse and set its fields
        AccessTokenResponse accessTokenResponse = new AccessTokenResponse();
        accessTokenResponse.setToken(jsonObject.optString("access_token", null));
        accessTokenResponse.setExpiresIn(jsonObject.optInt("expires_in", 0));
        accessTokenResponse.setRefreshExpiresIn(jsonObject.optInt("refresh_expires_in", 0));
        accessTokenResponse.setRefreshToken(jsonObject.optString("refresh_token", null));
        accessTokenResponse.setTokenType(jsonObject.optString("token_type", null));
        accessTokenResponse.setIdToken(jsonObject.optString("id_token", null));
        accessTokenResponse.setNotBeforePolicy(jsonObject.optInt("not_before_policy", 0));
        accessTokenResponse.setSessionState(jsonObject.optString("session_state", null));
        accessTokenResponse.setScope(jsonObject.optString("scope", null));
        accessTokenResponse.setError(jsonObject.optString("error", null));
        accessTokenResponse.setErrorDescription(jsonObject.optString("error_description", null));
        accessTokenResponse.setErrorUri(jsonObject.optString("error_uri", null));

        return accessTokenResponse;
    }

    @Override
    public void deleteUser(String userId) {
        try (Keycloak keycloak = keycloakProvider.getInstance()) {
            keycloak.realm(realm).users().delete(userId);
        }catch (NotFoundException e){
            throw new UserNotFoundException("User with a given userId doesn't exist");
        }catch (Exception e){
            throw new InternalErrorException(e.getMessage());
        }
    }

    @Override
    public void deleteAllUsers() {
        try (Keycloak keycloak = keycloakProvider.getInstance()) {
            UsersResource usersResource = keycloak.realm(realm).users();
            List<UserRepresentation> users = usersResource.list();

            for (UserRepresentation user : users) {
//                boolean isAdmin = usersResource.get(user.getId()).roles().realmLevel().listEffective()
//                        .stream()
//                        .anyMatch(role -> role.getName().equals("ADMIN"));
//
//                if (!isAdmin) {
//                    usersResource.delete(user.getId());
//                }
                usersResource.delete(user.getId());
            }
        } catch (Exception e) {
            throw new InternalErrorException("An error occurred while deleting users: " + e.getMessage());
        }
    }

    @Override
    public List<UserRepresentation> getAllUsers() {
        Keycloak keycloak = keycloakProvider.getInstance();
        UsersResource usersResource = keycloak.realm(realm).users();
        return usersResource.list();
    }

    @Override
    public UserRepresentation getUser(String userId){
        try (Keycloak keycloak = keycloakProvider.getInstance()) {
            UserResource userResource = keycloak.realm(realm).users().get(userId);
            return userResource.toRepresentation();
        }catch (NotFoundException e){
            throw new UserNotFoundException("User with a given userId doesn't exist");
        }catch (Exception e){
            throw new InternalErrorException(e.getMessage());
        }
    }

    @Override
    public User userExists(String email) {
        List<UserRepresentation> users = keycloakProvider.getInstance().realm(realm).users().search(email, 0, 1);
        if (!users.isEmpty())
            return userService.getUserFromRepresentation(users.get(0));
        return null;
    }

    @Override
    public void updatePassword(String token, ResetPasswordDTO newPassword) {
        ResetToken resetToken = resetTokenService.getResetToken(token);
        if (resetToken != null && resetTokenService.isTokenValid(token)) {
            String userId = resetToken.getUser().getId();
            try {
                Keycloak keycloak = keycloakProvider.getInstance();
                UserResource userResource = keycloak.realm(realm).users().get(userId);

                CredentialRepresentation credential = new CredentialRepresentation();
                credential.setType(CredentialRepresentation.PASSWORD);
                credential.setValue(newPassword.getPassword());
                credential.setTemporary(false);

                userResource.resetPassword(credential);

                resetTokenService.invalidateToken(resetToken);
            } catch (Exception e) {
                // Handle any Keycloak exceptions, e.g., user not found, connection issues, etc.
                throw new InternalErrorException("An error occurred while updating password: " + e.getMessage());
            }
        }else{
            throw new InvalidTokenException("Invalid or expired token.");
        }
    }
}
