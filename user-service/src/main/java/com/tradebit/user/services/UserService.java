package com.tradebit.user.services;

import com.tradebit.user.models.Role;
import com.tradebit.user.models.User;
import org.keycloak.representations.idm.UserRepresentation;

public interface UserService {
    void deleteUser(String userId);
    User getUserFromRepresentation(UserRepresentation kcUser, String userId, Role role);
    User getUserFromRepresentation(UserRepresentation kcUser);

}
