package com.tradebit.services.identity;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class IdentityServiceClientImpl implements IdentityServiceClient{
    private final WebClient webClient;
    @Override
    public Mono<String> getUserEmail(String userId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/identity-service/users")
                        .queryParam("userId", userId)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .map(response -> (String) response.get("username"));
    }
}
