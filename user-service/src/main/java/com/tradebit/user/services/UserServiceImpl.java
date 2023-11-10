package com.tradebit.user.services;

import com.tradebit.exception.UserNotFoundException;
import com.tradebit.user.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
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
}
