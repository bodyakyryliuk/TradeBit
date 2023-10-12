package com.example.TradeBit.authentication;


import com.example.TradeBit.exceptions.UserDisabledException;
import com.example.TradeBit.jwt.JwtService;
import com.example.TradeBit.user.User;
import com.example.TradeBit.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public Map<String, String> authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        } catch (AuthenticationException e) {
            return new HashMap<>() {{
                put("status", "failure");
                put("message", "Authentication failed: " + e.getMessage());
            }};
        }

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User not found")); //TODO THROW EXCEPTION
        String jwtToken = jwtService.generateToken(user);

        return new HashMap<>()
        {{
            put("status", "success");
            put("message", "User is successfully authorized");
            put("jwt", jwtToken);
        }};
    }
}
