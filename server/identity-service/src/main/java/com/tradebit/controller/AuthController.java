package com.tradebit.controller;

import com.tradebit.dto.AuthorizationDTO;
import com.tradebit.dto.EmailDTO;
import com.tradebit.dto.ResetPasswordDTO;
import com.tradebit.dto.RegistrationDTO;
import com.tradebit.responses.TokenResponse;
import com.tradebit.service.AuthorizationService;
import com.tradebit.service.KeycloakService;
import com.tradebit.service.RegistrationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    @Value("${api.gateway.host}")
    private String apiGatewayHost;
    private final RegistrationService registrationService;
    private final AuthorizationService authorizationService;
    private final KeycloakService keycloakService;
    @PostMapping(value = "/register")
    public ResponseEntity<?> createUser(@RequestBody @Valid RegistrationDTO registrationDTO) {
        String userId = registrationService.register(registrationDTO);
        return new ResponseEntity<>(Map.of("status", "success",
                "message", "User has been registered successfully!",
                "userId", userId),
                HttpStatus.CREATED);
    }

    @GetMapping
    public String login(){
        return "General login page";
    }

    @PostMapping("/login/email")
    public ResponseEntity<TokenResponse> loginEmail(@RequestBody @Valid AuthorizationDTO authorizationDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(authorizationService.login(authorizationDTO));
    }

    @GetMapping("/login/google")
    public RedirectView loginGoogle() {
        // Redirect to the Keycloak login page
        return new RedirectView("http://localhost:8180/auth/realms/tradebit-realm/protocol/openid-connect/auth?client_id=tradebit&response_type=code&scope=openid&redirect_uri=" +
                "http://" +
                apiGatewayHost +
                "/identity-service/public/hello&kc_idp_hint=google");
    }

    @PostMapping("/logout")
    public RedirectView logout(HttpServletRequest request) throws ServletException {
        request.logout();
        return new RedirectView("http://localhost:8180/auth/realms/tradebit-realm/protocol/openid-connect/logout?redirect_uri=" +
                "http://" +
                apiGatewayHost
                +"/identity-service/auth");
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshAccessToken(@RequestParam String refreshToken) {
        try {
            AccessTokenResponse response = keycloakService.refreshToken(refreshToken);
            if (response.getErrorDescription()!=null) {
                if (response.getErrorDescription().equals("Invalid refresh token"))
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                            Map.of("status", "failure",
                                    "message", response.getErrorDescription()));

                else if (response.getErrorDescription().equals("Token is not active"))
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                            Map.of("status", "failure",
                                    "message", response.getErrorDescription()));
            }
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of("status", "failure",
                    "message", e.getMessage()));
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody @Valid EmailDTO email) {
        try {
            authorizationService.forgotPassword(email.getEmail());
            return ResponseEntity.ok(Map.of("success", "If an account with that email exists, a password reset email has been sent."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of("status", "failure", "message",e.getMessage()));
        }
    }

    @PostMapping("/update-password")
    public ResponseEntity<?> updatePassword(@RequestParam("token") String token,
                                            @RequestBody @Valid ResetPasswordDTO resetPasswordDTO) {
        keycloakService.updatePassword(token, resetPasswordDTO);
        return new ResponseEntity<>(Map.of("status", "success", "message", "Password has been successfully updated."), HttpStatus.OK);

    }


    @PostMapping("/sendConfirmationMail")
    public ResponseEntity<Map<String, String>> sendConfirmationMail(@RequestParam("userId") String userId){
        registrationService.sendVerificationLink(userId);
        return new ResponseEntity<>(Map.of("status", "success", "message", "Verification link has been sent."), HttpStatus.OK);
    }


}
