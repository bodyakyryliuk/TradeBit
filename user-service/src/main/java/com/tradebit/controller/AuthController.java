package com.tradebit.controller;


import com.tradebit.exception.AccountNotVerifiedException;
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
import org.slf4j.Logger;
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

    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(AuthController.class);

    @PostMapping(value = "/create")
    public ResponseEntity<?> createUser(@RequestBody @Valid RegistrationRequest registrationRequest) {
        Response createdResponse = registrationService.register(registrationRequest);
        return ResponseEntity.status(createdResponse.getStatus()).build();
    }

    @GetMapping("/login")
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

//    @GetMapping("/user/public/hello")
//    public ResponseEntity<?> handleGoogleLogin(@RequestParam("code") String code) {
//        // Exchange the code for tokens
//        TokenResponse tokenResponse = authorizationService.exchangeCodeForTokens(code);
//
//        // Create a session or other post-login actions
//
//        // Return success response
//        return ResponseEntity.ok().body(tokenResponse);
//    }

    @GetMapping("/account-not-exists")
    public ResponseEntity<?> handleNoAccount() {
        // Logic to confirm no account exists

        // Return custom error response
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new CustomErrorResponse(
                        "account_not_created",
                        "Please create an account to continue."
                ));
    }


    @GetMapping("/required-action")
    public void handleEmailNotVerified(@RequestParam("execution") String execution) {
        throw new AccountNotVerifiedException();
    }





}
