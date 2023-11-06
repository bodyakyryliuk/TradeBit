package com.tradebit.controller;


import com.tradebit.http.requests.AuthorizationRequest;
import com.tradebit.http.requests.RegistrationRequest;
import com.tradebit.service.AuthorizationService;
import com.tradebit.service.RegistrationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final RegistrationService registrationService;
    private final AuthorizationService authorizationService;

    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(UserController.class);

    @PostMapping(value = "/create")
    public ResponseEntity<?> createUser(@RequestBody @Valid RegistrationRequest registrationRequest) {
        Response createdResponse = registrationService.register(registrationRequest);
        return ResponseEntity.status(createdResponse.getStatus()).build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthorizationRequest authorizationRequest) {
        // TODO: create authorization service and perform this logic there. Handle not correct credentials when user logins
        // TODO:

        return authorizationService.login(authorizationRequest);
    }

    @GetMapping("/registrationConfirm")
    public ResponseEntity<?> confirmRegistration(@RequestParam("token") String token){
        try {
            return new ResponseEntity<>(registrationService.confirmRegistration(token), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

	

}
