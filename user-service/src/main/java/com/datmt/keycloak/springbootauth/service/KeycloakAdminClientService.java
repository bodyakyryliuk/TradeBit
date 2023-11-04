package com.datmt.keycloak.springbootauth.service;

import com.datmt.keycloak.springbootauth.config.KeycloakProvider;
import com.datmt.keycloak.springbootauth.http.requests.CreateUserRequest;
import com.datmt.keycloak.springbootauth.user.models.Role;
import com.datmt.keycloak.springbootauth.user.models.User;
import com.datmt.keycloak.springbootauth.user.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.resource.RoleMappingResource;
import org.keycloak.admin.client.resource.RoleScopeResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.net.URI;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class KeycloakAdminClientService {
    @Value("${keycloak.realm}")
    public String realm;

    private final KeycloakProvider kcProvider;
    private final UserRepository userRepository;
    public Response createKeycloakUser(CreateUserRequest user) {
        UsersResource usersResource = kcProvider.getInstance().realm(realm).users();
        CredentialRepresentation credentialRepresentation = createPasswordCredentials(user.getPassword());

        UserRepresentation kcUser = new UserRepresentation();
        kcUser.setUsername(user.getEmail());
        kcUser.setCredentials(Collections.singletonList(credentialRepresentation));
        kcUser.setFirstName(user.getFirstname());
        kcUser.setLastName(user.getLastname());
        kcUser.setEmail(user.getEmail());
        kcUser.setEnabled(true);
        kcUser.setEmailVerified(false);


        Response response = usersResource.create(kcUser);

        if (response.getStatus() == 201) {
            String userId = extractUserId(response);
            String role = assignRole(userId, usersResource);
            saveUserInDB(kcUser, userId, role);
        }

        return response;

    }

    private String assignRole(String userId, UsersResource usersResource){
        RoleMappingResource roleMappingResource = usersResource.get(userId).roles();
        RoleScopeResource realmRolesResource = roleMappingResource.realmLevel();

        // Get existing realm roles
        List<RoleRepresentation> availableRoles = realmRolesResource.listAvailable();

        // Find the roles you want to assign
        List<RoleRepresentation> rolesToAdd = availableRoles.stream()
                .filter(role -> role.getName().equals("USER"))
                .collect(Collectors.toList());

        // Add the roles to the user
        realmRolesResource.add(rolesToAdd);
        return rolesToAdd
                        .stream()
                        .findFirst()
                        .map(RoleRepresentation::getName)
                        .orElse(null);
    }

    private List<String> getUserRoles(String userId, UsersResource usersResource){
        List<RoleRepresentation> assignedRoles = usersResource.get(userId).roles().realmLevel().listEffective();

        // Extract role names from the assigned roles
        return assignedRoles.stream()
                .map(RoleRepresentation::getName)
                .collect(Collectors.toList());
    }

    private String extractUserId(Response response) {
        URI location = response.getLocation();
        if (location == null) {
            throw new IllegalStateException("No Location header present in response");
        }
        String path = location.getPath();
        return path.substring(path.lastIndexOf('/') + 1);
    }

    private void saveUserInDB(UserRepresentation kcUser, String userId, String role){
        User user = User.builder()
                .email(kcUser.getEmail())
                .id(userId)
                .firstName(kcUser.getFirstName())
                .lastName(kcUser.getLastName())
                .role(mapRole(role))
                .enabled(kcUser.isEnabled())
                .emailVerified(kcUser.isEmailVerified())
                .build();

        userRepository.save(user);
        log.info("User {} saved to database!", user.getEmail());

    }

    private Role mapRole(String roleName){
        return Role.valueOf(roleName);
    }

    private static CredentialRepresentation createPasswordCredentials(String password) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        return passwordCredentials;
    }


}
