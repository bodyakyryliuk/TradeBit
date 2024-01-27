package com.tradebit.service;

import com.tradebit.requests.AuthorizationRequest;
import com.tradebit.responses.TokenResponse;

public interface AuthorizationService {
    TokenResponse login(AuthorizationRequest authorizationRequest);
    void forgotPassword(String email);

}
