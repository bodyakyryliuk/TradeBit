package com.tradebit.service;

import com.tradebit.requests.AuthorizationRequest;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.http.ResponseEntity;

public interface AuthorizationService {
    AccessTokenResponse login(AuthorizationRequest authorizationRequest);
    void forgotPassword(String email);

}
