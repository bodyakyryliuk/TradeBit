package com.tradebit.service;

import com.tradebit.http.requests.RegistrationRequest;
import jakarta.ws.rs.core.Response;


public interface RegistrationService {
    Response register(RegistrationRequest user);
}
