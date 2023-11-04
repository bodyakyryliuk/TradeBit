package com.tradebit.config;

import com.tradebit.authentication.OAuth2TokenResponse;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Configuration
@Getter
public class KeycloakRestTemplateConfig {

    @Value("${keycloak.auth-server-url}")
    private String keycloakServerUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.resource}")
    private String clientId;

    @Value("${keycloak.credentials.secret}")
    private String clientSecret;

    @Bean
    public RestTemplate keycloakRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add((request, body, execution) -> {
            // Assuming that we are using client credentials grant type for this service-to-service communication
            HttpHeaders headers = request.getHeaders();
            headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken());
            return execution.execute(request, body);
        });
        return restTemplate;
    }

    private String getAccessToken() {

        MultiValueMap<String, String> formParameters = new LinkedMultiValueMap<>();
        formParameters.add("client_id", clientId);
        formParameters.add("client_secret", clientSecret);
        formParameters.add("grant_type", "client_credentials");

        ResponseEntity<OAuth2TokenResponse> response = new RestTemplate().postForEntity(
                keycloakServerUrl + "/realms/" + realm + "/protocol/openid-connect/token",
                formParameters, OAuth2TokenResponse.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody().getAccessToken();
        } else {
            throw new RuntimeException("Failed to retrieve access token from Keycloak");
        }
    }
}
