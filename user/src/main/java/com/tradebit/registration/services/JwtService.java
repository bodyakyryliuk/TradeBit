package com.tradebit.registration.services;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String extractUserEmail(String token);
    boolean isTokenValid(String token, UserDetails userDetails);
    String generateToken(UserDetails userDetails);

    boolean extractEnabledStatus(String token);
}
