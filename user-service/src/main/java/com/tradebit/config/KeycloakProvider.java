package com.tradebit.config;


import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.tradebit.exception.InternalErrorException;
import com.tradebit.exception.UserNotFoundException;
import com.tradebit.responses.TokenResponse;
import jakarta.ws.rs.NotFoundException;
import lombok.Getter;
import org.json.JSONObject;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

@Configuration
@Getter
public class KeycloakProvider {

    @Value("${keycloak.auth-server-url}")
    public String serverURL;
    @Value("${keycloak.realm}")
    public String realm;
    @Value("${keycloak.resource}")
    public String clientID;
    @Value("${keycloak.credentials.secret}")
    public String clientSecret;

    private static Keycloak keycloak = null;

    public KeycloakProvider() {
    }

    public Keycloak getInstance() {
        if (keycloak == null) {

            return KeycloakBuilder.builder()
                    .realm(realm)
                    .serverUrl(serverURL)
                    .clientId(clientID)
                    .clientSecret(clientSecret)
                    .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                    .build();
        }
        return keycloak;
    }


    public KeycloakBuilder newKeycloakBuilderWithPasswordCredentials(String username, String password) {
        return KeycloakBuilder.builder() //
                .realm(realm) //
                .serverUrl(serverURL)//
                .clientId(clientID) //
                .clientSecret(clientSecret) //
                .username(username) //
                .password(password);
    }

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

    public ResponseEntity<Map<String, String>> deleteUser(String userId) {
        try (Keycloak keycloak = getInstance()) {
            keycloak.realm(realm).users().delete(userId);
            return new ResponseEntity<>(Map.of("status", "success",
                    "message", "User has been deleted successfully!"),
                    HttpStatus.OK);
        }catch (NotFoundException e){
            throw new UserNotFoundException("User with a given userId doesn't exist");
        }catch (Exception e){
            throw new InternalErrorException(e.getMessage());
        }
    }

    public ResponseEntity<Map<String, String>> deleteAllUsers() {
        try (Keycloak keycloak = getInstance()) {
            UsersResource usersResource = keycloak.realm(realm).users();
            List<UserRepresentation> users = usersResource.list();

            for (UserRepresentation user : users) {
                // Fetch effective realm roles for the user
                boolean isAdmin = usersResource.get(user.getId()).roles().realmLevel().listEffective()
                        .stream()
                        .anyMatch(role -> role.getName().equals("ADMIN"));

                if (!isAdmin) {
                    usersResource.delete(user.getId());
                }
            }

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "All non-admin users have been deleted successfully!"
            ));
        } catch (Exception e) {
            throw new InternalErrorException("An error occurred while deleting non-admin users: " + e.getMessage());
        }
    }


    public List<UserRepresentation> getAllUsers() {
        Keycloak keycloak = getInstance();
        UsersResource usersResource = keycloak.realm(realm).users();
        return usersResource.list();
    }
}