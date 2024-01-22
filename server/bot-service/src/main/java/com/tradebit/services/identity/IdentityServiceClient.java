package com.tradebit.services.identity;

import reactor.core.publisher.Mono;

public interface IdentityServiceClient {
    Mono<String> getUserEmail(String userId);
}
