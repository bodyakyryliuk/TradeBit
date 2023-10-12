package com.example.TradeBit;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    @GetMapping("/mainPage")
    public ResponseEntity<String> getMainPage(){
        return ResponseEntity.ok("Hello from main page");
    }
}
