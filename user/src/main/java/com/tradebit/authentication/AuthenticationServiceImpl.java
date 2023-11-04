package com.tradebit.authentication;

import lombok.RequiredArgsConstructor;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService{
    private final RestTemplate keycloakRestTemplate;
    @Override
    public OAuth2TokenResponse authenticate(AuthenticationRequest request) {
        MultiValueMap<String, String> formParameters = new LinkedMultiValueMap<>();
        formParameters.add("client_id", "your-client-id");
        formParameters.add("username", request.getEmail());
        formParameters.add("password", request.getPassword());
        formParameters.add("grant_type", "password");

        // Send the request to Keycloak's token endpoint
        ResponseEntity<OAuth2TokenResponse> response = keycloakRestTemplate.postForEntity(
                "/protocol/openid-connect/token", formParameters, OAuth2TokenResponse.class);

        // Return the response body
        return response.getBody();
    }
}
