package com.tradebit.authentication;

public interface AuthenticationService {
    OAuth2TokenResponse authenticate(AuthenticationRequest request);
}
