package com.example.TradeBit.registration;

import com.example.TradeBit.authentication.AuthenticationResponse;
import com.example.TradeBit.exceptions.InvalidTokenException;
import com.example.TradeBit.exceptions.UserExistsException;
import com.example.TradeBit.exceptions.UserNotFoundException;
import com.example.TradeBit.jwt.JwtService;
import com.example.TradeBit.registration.email.EmailService;
import com.example.TradeBit.registration.verificationToken.VerificationToken;
import com.example.TradeBit.registration.verificationToken.VerificationTokenService;
import com.example.TradeBit.role.Role;
import com.example.TradeBit.user.User;
import com.example.TradeBit.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final VerificationTokenService verificationTokenService;
    private final EmailService emailService;
    public AuthenticationResponse register(RegisterRequest request) {
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .enabled(false)
                .build();

        if (checkUserExists(user.getEmail()))
            throw new UserExistsException();

        userRepository.save(user);
        String jwtToken = jwtService.generateToken(user);

        VerificationToken verificationToken = verificationTokenService.generateVerificationToken(user);
        emailService.sendVerificationToken(user.getEmail(), verificationToken.getToken());

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public User confirmRegistration(String token){
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
        return userRepository.save(user);
    }

    private boolean checkUserExists(String email){
        return userRepository.findByEmail(email).isPresent();
    }
}
