package com.tradebit.registration.services;

import com.tradebit.authentication.AuthenticationResponse;
import com.tradebit.exception.InvalidTokenException;
import com.tradebit.exception.UserNotFoundException;
import com.tradebit.registration.RegistrationRequest;
import com.tradebit.registration.services.RegistrationService;
import com.tradebit.user.models.Role;
import com.tradebit.user.models.User;
import com.tradebit.user.repositories.UserRepository;
import com.tradebit.verificationToken.VerificationToken;
import com.tradebit.verificationToken.VerificationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenService verificationTokenService;
    private final JwtService jwtService;
    public Map<String, String> register(RegistrationRequest request) {
        HashMap<String, String> response = new HashMap<>();

        User user = createUserFromRequest(request);

        if (checkUserExists(user.getEmail())){
            response.put("status", "failure");
            response.put("message", "User already exists!");
            return response;
        }

        userRepository.save(user);

        VerificationToken verificationToken = verificationTokenService.generateVerificationToken(user);
        // TODO: create a notification microservice with emailService class to send verification token
//        emailService.sendVerificationToken(user.getEmail(), verificationToken.getToken());

        response.put("status", "success");
        response.put("message", "Registration successful! Please check your email to activate your account.");
        return response;
    }

    public AuthenticationResponse confirmRegistration(String token){
        VerificationToken verificationToken = verificationTokenService.getVerificationToken(token);

        if (verificationToken == null)
            throw new InvalidTokenException("Invalid token");
        else if (verificationToken.getExpiryDate().before(new Date()))
            throw new InvalidTokenException("Token has expired");
        User user = verificationToken.getUser();
        if (user == null)
            throw new UserNotFoundException("User with a given token doesn't exist");

        user.setEnabled(true);
        verificationTokenService.deleteVerificationToken(verificationToken);
        userRepository.save(user);

        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    private boolean checkUserExists(String email){
        return userRepository.findByEmail(email).isPresent();
    }

    private User createUserFromRequest(RegistrationRequest request) {
        return User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .enabled(false)
                .build();
    }
}
