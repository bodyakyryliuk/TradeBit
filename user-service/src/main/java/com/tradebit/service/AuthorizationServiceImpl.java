package com.tradebit.service;

import com.tradebit.config.KeycloakProvider;
import com.tradebit.exception.AccountNotVerifiedException;
import com.tradebit.exception.InvalidCredentialsException;
import com.tradebit.exception.UserNotFoundException;
import com.tradebit.http.requests.AuthorizationRequest;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotAuthorizedException;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthorizationServiceImpl implements AuthorizationService{
    @Value("${keycloak.realm}")
    public String realm;

    private final KeycloakProvider kcProvider;
    @Override
    public ResponseEntity<?> login(AuthorizationRequest authorizationRequest) {
        Keycloak keycloak = kcProvider.getInstance();

        // Check if the user's email is verified
        UsersResource usersResource = keycloak.realm(realm).users();
        List<UserRepresentation> users = usersResource.search(authorizationRequest.getEmail(), true);
        if (users.isEmpty()) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        UserRepresentation user = users.get(0);

        // Proceed to attempt login
        try {
            keycloak = kcProvider.newKeycloakBuilderWithPasswordCredentials(authorizationRequest.getEmail(), authorizationRequest.getPassword()).build();
            AccessTokenResponse accessTokenResponse = keycloak.tokenManager().getAccessToken();

            if (!user.isEmailVerified()) {
                throw new AccountNotVerifiedException();
            }

            return ResponseEntity.status(HttpStatus.OK).body(accessTokenResponse);
        } catch (NotAuthorizedException ex) {
            throw new InvalidCredentialsException("Invalid credentials");
        } catch (BadRequestException ex) {
            throw new AccountNotVerifiedException();
        } catch (IllegalStateException ex){
            if (ex.getMessage().equals("username required"))
                throw new InvalidCredentialsException("email required");
            else
                throw new InvalidCredentialsException(ex.getMessage());
        }
    }
}
