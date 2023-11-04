package com.datmt.keycloak.springbootauth.http.requests;

import lombok.Getter;

@Getter
public class AuthorizationRequest {

    String email;
    String password;
}
