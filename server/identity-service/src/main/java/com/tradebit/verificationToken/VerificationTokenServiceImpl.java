package com.tradebit.verificationToken;

import com.tradebit.exception.VerificationTokenNotFoundException;
import com.tradebit.user.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerificationTokenServiceImpl implements VerificationTokenService {
    private final VerificationTokenRepository tokenRepository;

    @Override
    public VerificationToken generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken(user, token);

        return tokenRepository.save(verificationToken);
    }

    @Override
    public VerificationToken getVerificationToken(String token) {
        return tokenRepository.findByToken(token).orElseThrow(() ->
                new VerificationTokenNotFoundException("Verification token not found by token: " + token));
    }

    @Override
    public void deleteVerificationToken(VerificationToken token) {
        tokenRepository.delete(token);
    }

    @Override
    public void deleteVerificationToken(User user) {
        VerificationToken verificationToken = tokenRepository.findByUser(user).orElseThrow(() ->
                new VerificationTokenNotFoundException("Verification token not found by userID: " + user.getId()));

        tokenRepository.delete(verificationToken);
    }

    @Override
    public void deleteVerificationTokenIfExists(User user) {
        Optional<VerificationToken> verificationToken = tokenRepository.findByUser(user);

        verificationToken.ifPresent(tokenRepository::delete);
    }
}