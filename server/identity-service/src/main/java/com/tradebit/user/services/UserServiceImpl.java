package com.tradebit.user.services;

import com.tradebit.exception.UserNotFoundException;
import com.tradebit.resetToken.ResetTokenRepository;
import com.tradebit.user.models.Role;
import com.tradebit.user.models.User;
import com.tradebit.user.repositories.UserRepository;
import com.tradebit.verificationToken.VerificationTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final ResetTokenRepository resetTokenRepository;
    @Override
    @Transactional
    public void deleteUser(String userId) {
        if(userRepository.findById(userId).isPresent()){
            verificationTokenRepository.deleteByUserId(userId);
            resetTokenRepository.deleteByUserId(userId);
            userRepository.deleteById(userId);
        }else
            throw new UserNotFoundException("User does not exist");
    }

    @Override
    @Transactional
    public void deleteAllUsers() {
        verificationTokenRepository.deleteAll();
        resetTokenRepository.deleteAll();
        userRepository.deleteAll();
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

    @Override
    public User getUserById(String userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException("User with id:" + userId + " not found!"));
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }
}
