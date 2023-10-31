package com.tradebit.registration.services;

import com.tradebit.authentication.AuthenticationResponse;
import com.tradebit.registration.RegistrationRequest;

import java.util.Map;

public interface RegistrationService {
    Map<String, String> register(RegistrationRequest request);
    AuthenticationResponse confirmRegistration(String token);
}
