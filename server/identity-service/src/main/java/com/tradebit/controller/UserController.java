package com.tradebit.controller;

import com.tradebit.service.KeycloakService;
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
    @DeleteMapping
    public ResponseEntity<Map<String, String>> deleteUser(@RequestParam(required = false) String userId){
        if (userId != null) {
            userService.deleteUser(userId);
            keycloakService.deleteUser(userId);
            return new ResponseEntity<>(Map.of("status", "success",
                    "message", "User has been deleted successfully!"),
                    HttpStatus.OK);
        }else{
            return deleteAllUsers();
        }
    }

    private ResponseEntity<Map<String, String>> deleteAllUsers(){
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

    @GetMapping(params = "userId")
    public UserRepresentation getUser(@RequestParam String userId){
        return keycloakService.getUser(userId);
    }


}
