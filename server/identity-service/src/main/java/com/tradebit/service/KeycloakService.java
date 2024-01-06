package com.tradebit.service;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.tradebit.requests.PasswordRequest;
import com.tradebit.user.models.User;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface KeycloakService {
    AccessTokenResponse refreshToken(String refreshToken) throws UnirestException;

    AccessTokenResponse createTokenResponseFromJsonNode(JsonNode jsonNode);

    void deleteUser(String userId);

    void deleteAllUsers();

    List<UserRepresentation> getAllUsers();

    UserRepresentation getUser(String userId);

    User userExists(String email);

    void updatePassword(String token, PasswordRequest newPassword);
}
