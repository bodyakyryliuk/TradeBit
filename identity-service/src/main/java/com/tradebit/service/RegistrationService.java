package com.tradebit.service;

import com.tradebit.http.requests.RegistrationRequest;
import jakarta.ws.rs.core.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.Map;


public interface RegistrationService {
    void register(RegistrationRequest user);
    void confirmRegistration(String token);

}
