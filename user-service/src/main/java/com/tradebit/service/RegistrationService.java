package com.tradebit.service;

import com.tradebit.http.requests.RegistrationRequest;
import jakarta.ws.rs.core.Response;
import org.springframework.http.ResponseEntity;

import java.util.Map;


public interface RegistrationService {
    Response register(RegistrationRequest user);
    ResponseEntity<Map<String, String>> confirmRegistration(String token);
}
