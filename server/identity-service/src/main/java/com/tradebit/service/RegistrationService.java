package com.tradebit.service;

import com.tradebit.requests.RegistrationRequest;
import org.springframework.http.ResponseEntity;

import java.util.Map;


public interface RegistrationService {
    ResponseEntity<?> register(RegistrationRequest user);
    ResponseEntity<Map<String, String>> confirmRegistration(String token);

}
