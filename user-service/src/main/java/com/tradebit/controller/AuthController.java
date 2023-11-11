package com.tradebit.controller;

import com.tradebit.http.requests.AuthorizationRequest;
import com.tradebit.http.requests.RegistrationRequest;
import com.tradebit.resetToken.ResetTokenService;
import com.tradebit.service.AuthorizationService;
import com.tradebit.service.KeycloakService;
import com.tradebit.service.RegistrationService;
import com.tradebit.http.requests.EmailRequest;
import com.tradebit.http.requests.PasswordRequest;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final RegistrationService registrationService;
    private final AuthorizationService authorizationService;
    private final ResetTokenService resetTokenService;
    private final KeycloakService keycloakService;
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
        // TODO: dont return redirect view, only response
        return new RedirectView("http://localhost:8180/auth/realms/tradebit-realm/protocol/openid-connect/logout?redirect_uri=http://localhost:8080/user/auth/login");
    }

    @PostMapping("/registrationConfirm")
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
            AccessTokenResponse response = keycloakService.refreshToken(refreshToken);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to refresh token");
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody @Valid EmailRequest email) {
        try {
            authorizationService.forgotPassword(email.getEmail());
            return ResponseEntity.ok(Map.of("success", "If an account with that email exists, a password reset email has been sent."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error processing your request"));
        }
    }

    @GetMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam("token") String token){
        boolean isValidToken = resetTokenService.isTokenValid(token);
        if (isValidToken) {
            return ResponseEntity.ok(Map.of("status", "success", "message", "Token is valid."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("status", "error", "message", "Invalid or expired token."));
        }
    }

    @PostMapping("/update-password")
    public ResponseEntity<?> updatePassword(@RequestParam("token") String token,
                                            @RequestBody @Valid PasswordRequest passwordRequest) {
        return keycloakService.updatePassword(token, passwordRequest);
    }


}
