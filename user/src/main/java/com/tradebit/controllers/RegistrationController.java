package com.tradebit.controllers;

import com.tradebit.authentication.OAuth2TokenResponse;
import com.tradebit.registration.RegistrationRequest;
import com.tradebit.registration.services.RegistrationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class RegistrationController {
    private final RegistrationService registrationService;
    @PostMapping("/register")
    public ResponseEntity<Response> register(
            @RequestBody RegistrationRequest request
    ){
//        return ResponseEntity.ok(registrationService.register(request));
        return ResponseEntity.ok(registrationService.createKeycloakUser(request));
    }

    @GetMapping("/registrationConfirm")
    public ResponseEntity<?> confirmRegistration(@RequestParam("token") String token){
        try {
            OAuth2TokenResponse response = registrationService.confirmRegistration(token);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) throws ServletException{
        request.logout();
        return "redirect:localhost:8085/login";
    }
}
