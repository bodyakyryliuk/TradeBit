package com.tradebit.controllers;

import com.tradebit.authentication.AuthenticationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/login")
//public class AuthenticationController {
//    private final AuthenticationService authenticationService;
//
//    @PostMapping("/email")
//    public ResponseEntity<Map<String, String>> authenticate(
//            @RequestBody AuthenticationRequest request
//    ){
//        return authenticationService.authenticate(request);
//    }}