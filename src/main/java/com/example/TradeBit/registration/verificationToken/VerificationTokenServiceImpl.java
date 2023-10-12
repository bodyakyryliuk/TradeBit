package com.example.TradeBit.registration.verificationToken;

import com.example.TradeBit.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerificationTokenServiceImpl implements VerificationTokenService{
    private final VerificationTokenRepository tokenRepository;

    @Override
    public VerificationToken generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken(user, token);

        return tokenRepository.save(verificationToken);
    }

    @Override
    public VerificationToken getVerificationToken(String token) {
        return tokenRepository.findByToken(token);
    }

    @Override
    public void deleteVerificationToken(VerificationToken token) {
        tokenRepository.delete(token);
    }
}
