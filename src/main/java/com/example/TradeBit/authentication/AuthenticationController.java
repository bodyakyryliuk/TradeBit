package com.example.TradeBit.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping()
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/login/email")
    public ResponseEntity<Map<String, String>> authenticate(
            @RequestBody AuthenticationRequest request
    ){
        HashMap<String, String> response = new HashMap<>(authenticationService.authenticate(request));
        return ResponseEntity.ok(response);
    }

}
