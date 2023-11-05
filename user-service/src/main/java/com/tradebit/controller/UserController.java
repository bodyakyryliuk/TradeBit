package com.tradebit.controller;


import com.tradebit.config.KeycloakProvider;
import com.tradebit.http.requests.RegistrationRequest;
import com.tradebit.http.requests.AuthorizationRequest;
import com.tradebit.service.RegistrationServiceImpl;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.AccessTokenResponse;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
public class UserController {
    private final RegistrationServiceImpl kcAdminClient;

    private final KeycloakProvider kcProvider;

    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(UserController.class);


    public UserController(RegistrationServiceImpl kcAdminClient, KeycloakProvider kcProvider) {
        this.kcProvider = kcProvider;
        this.kcAdminClient = kcAdminClient;
    }
	

    @PostMapping(value = "/create")
    public ResponseEntity<?> createUser(@RequestBody RegistrationRequest registrationRequest) {
        Response createdResponse = kcAdminClient.register(registrationRequest);
        return ResponseEntity.status(createdResponse.getStatus()).build();
//        return kcAdminClient.registerUser(registrationRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<AccessTokenResponse> login(@NotNull @RequestBody AuthorizationRequest authorizationRequest) {
        Keycloak keycloak = kcProvider.newKeycloakBuilderWithPasswordCredentials(authorizationRequest.getEmail(), authorizationRequest.getPassword()).build();

        AccessTokenResponse accessTokenResponse = null;
        try {
            accessTokenResponse = keycloak.tokenManager().getAccessToken();
            return ResponseEntity.status(HttpStatus.OK).body(accessTokenResponse);
        } catch (BadRequestException ex) {
            LOG.warn("invalid account. User probably hasn't verified email.", ex);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(accessTokenResponse);
        }
    }

    @GetMapping("/registrationConfirm")
    public ResponseEntity<?> confirmRegistration(@RequestParam("token") String token){
        try {
            return new ResponseEntity<>(kcAdminClient.confirmRegistration(token), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

	

}
