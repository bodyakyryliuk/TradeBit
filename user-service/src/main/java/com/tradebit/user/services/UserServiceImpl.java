package com.tradebit.user.services;

import com.tradebit.exception.UserNotFoundException;
import com.tradebit.user.models.Role;
import com.tradebit.user.models.User;
import com.tradebit.user.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    @Override
    public void deleteUser(String userId) {
        userRepository.findById(userId).ifPresentOrElse(
                userRepository::delete, () -> {
            throw new UserNotFoundException("User with ID " + userId + " does not exist!");
            }
        );
    }

    public User getUserFromRepresentation(UserRepresentation kcUser, String userId, Role role){
        User user = User.builder()
                .email(kcUser.getEmail())
                .id(userId)
                .firstName(kcUser.getFirstName())
                .lastName(kcUser.getLastName())
                .role(role)
                .enabled(kcUser.isEnabled())
                .emailVerified(kcUser.isEmailVerified())
                .build();

        return user;
    }

    @Override
    public User getUserFromRepresentation(UserRepresentation kcUser) {
        User user = User.builder()
                .email(kcUser.getEmail())
                .id(kcUser.getId())
                .firstName(kcUser.getFirstName())
                .lastName(kcUser.getLastName())
                .role(Role.USER)
                .enabled(kcUser.isEnabled())
                .emailVerified(kcUser.isEmailVerified())
                .build();

        return user;    }
}
