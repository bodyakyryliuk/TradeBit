package com.tradebit.registration.services;

import com.tradebit.authentication.OAuth2TokenResponse;
import com.tradebit.registration.RegistrationRequest;
import jakarta.ws.rs.core.Response;

import java.util.Map;

public interface RegistrationService {
    Map<String, String> register(RegistrationRequest request);
    Response createKeycloakUser(RegistrationRequest request);
    OAuth2TokenResponse confirmRegistration(String token);
}
