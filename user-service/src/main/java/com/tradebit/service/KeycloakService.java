package com.tradebit.service;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface KeycloakService {
    AccessTokenResponse refreshToken(String refreshToken) throws UnirestException;

    AccessTokenResponse createTokenResponseFromJsonNode(JsonNode jsonNode);

    ResponseEntity<Map<String, String>> deleteUser(String userId);

    ResponseEntity<Map<String, String>> deleteAllUsers();

    public List<UserRepresentation> getAllUsers();

    public UserRepresentation getUser(String userId);
}
