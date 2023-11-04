package com.tradebit.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Secured {
    @RequestMapping("/secured")
    public String getSecuredEndpoint(){
        return "Hello from secured endpoint";
    }
}
