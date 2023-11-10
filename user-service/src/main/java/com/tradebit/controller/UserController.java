package com.tradebit.controller;

import com.tradebit.config.KeycloakProvider;
import com.tradebit.user.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;
    private final KeycloakProvider keycloakProvider;

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable String userId){
        userRepository.deleteById(userId);
        return keycloakProvider.deleteUser(userId);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, String>> deleteAllUsers(){
        userRepository.deleteAll();
        return keycloakProvider.deleteAllUsers();
    }

    @GetMapping
    public List<UserRepresentation> getAllUsers(){
        return keycloakProvider.getAllUsers();
    }
}
