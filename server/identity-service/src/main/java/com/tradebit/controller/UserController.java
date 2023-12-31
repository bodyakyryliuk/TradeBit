package com.tradebit.controller;

import com.tradebit.service.KeycloakService;
import com.tradebit.user.repositories.UserRepository;
import com.tradebit.user.services.UserService;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final KeycloakService keycloakService;
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable String userId){
        userService.deleteUser(userId);
        keycloakService.deleteUser(userId);
        return new ResponseEntity<>(Map.of("status", "success",
                "message", "User has been deleted successfully!"),
                HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, String>> deleteAllUsers(){
        userService.deleteAllUsers();
        keycloakService.deleteAllUsers();
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "All the users have been deleted successfully!"
        ));
    }

    @GetMapping
    public List<UserRepresentation> getAllUsers(){
        return keycloakService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public UserRepresentation getUser(@PathVariable String userId){
        return keycloakService.getUser(userId);
    }


}
