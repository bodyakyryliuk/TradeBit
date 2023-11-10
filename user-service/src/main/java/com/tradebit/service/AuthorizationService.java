package com.tradebit.service;

import com.tradebit.http.requests.AuthorizationRequest;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface AuthorizationService {
    ResponseEntity<?> login(AuthorizationRequest authorizationRequest);

}
