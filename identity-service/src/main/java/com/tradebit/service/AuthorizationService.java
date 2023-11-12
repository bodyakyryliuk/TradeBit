package com.tradebit.service;

import com.tradebit.http.requests.AuthorizationRequest;
import org.springframework.http.ResponseEntity;

public interface AuthorizationService {
    ResponseEntity<?> login(AuthorizationRequest authorizationRequest);
    void forgotPassword(String email);

}
