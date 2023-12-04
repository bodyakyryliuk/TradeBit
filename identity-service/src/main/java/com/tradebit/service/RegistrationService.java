package com.tradebit.service;

import com.tradebit.requests.RegistrationRequest;
import org.springframework.http.ResponseEntity;

import java.util.Map;


public interface RegistrationService {
    void register(RegistrationRequest user);
    void confirmRegistration(String token);

}
