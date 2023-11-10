package com.tradebit.controller;

import com.mashape.unirest.http.JsonNode;
import com.tradebit.config.KeycloakProvider;
import com.tradebit.http.requests.AuthorizationRequest;
import com.tradebit.http.requests.RegistrationRequest;
import com.tradebit.responses.TokenResponse;
import com.tradebit.service.AuthorizationService;
import com.tradebit.service.RegistrationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final RegistrationService registrationService;
    private final AuthorizationService authorizationService;
    private final KeycloakProvider keycloakProvider;

    @PostMapping(value = "/register")
    public ResponseEntity<?> createUser(@RequestBody @Valid RegistrationRequest registrationRequest) {
        return registrationService.register(registrationRequest);
    }

    @GetMapping
    public String login(){
        return "General login page";
    }

    @PostMapping("/login/email")
    public ResponseEntity<?> loginEmail(@RequestBody @Valid AuthorizationRequest authorizationRequest) {
        return authorizationService.login(authorizationRequest);
    }

    @GetMapping("/login/google")
    public RedirectView loginGoogle() {
        // Redirect to the Keycloak login page
        return new RedirectView("http://localhost:8180/auth/realms/tradebit-realm/protocol/openid-connect/auth?client_id=tradebit&response_type=code&scope=openid&redirect_uri=http://localhost:8080/user/public/hello&kc_idp_hint=google");
    }

    @GetMapping("/logout")
    public RedirectView logout(HttpServletRequest request) throws ServletException {
        request.logout();
        return new RedirectView("http://localhost:8180/auth/realms/tradebit-realm/protocol/openid-connect/logout?redirect_uri=http://localhost:8080/user/auth/login");
    }

    @GetMapping("/registrationConfirm")
    public ResponseEntity<?> confirmRegistration(@RequestParam("token") String token){
        try {
            return new ResponseEntity<>(registrationService.confirmRegistration(token), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshAccessToken(@RequestParam String refreshToken) {
        try {
            AccessTokenResponse response = keycloakProvider.refreshToken(refreshToken);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to refresh token");
        }
    }

}
