package com.tradebit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@RestController
@EnableAuthorizationServer
public class AuthenticationApplication {
    @RequestMapping(value = { "/user" }, produces = "application/json")
    public Map<String, Object> user(Authentication authentication) {
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("user", authentication.getName());
        userInfo.put("authorities", authentication.getAuthorities());
        return userInfo;
    }


    public static void main(String[] args) {
        SpringApplication.run(AuthenticationApplication.class, args);
    }
}