package com.tradebit.resetToken;

import com.tradebit.user.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResetTokenServiceImpl implements ResetTokenService{
    private final ResetTokenRepository tokenRepository;

    @Override
    public ResetToken generateResetToken(User user) {
        String token = UUID.randomUUID().toString();
        ResetToken resetToken = new ResetToken(user, token);

        return tokenRepository.save(resetToken);
    }

    @Override
    public ResetToken getResetToken(String token) {
        return tokenRepository.findByToken(token);
    }

    @Override
    public ResetToken findByUser(User user) {
        return tokenRepository.findByUser(user);
    }

    @Override
    public void deleteResetToken(ResetToken token) {
        tokenRepository.delete(token);
    }

    @Override
    public boolean isTokenValid(String token) {
        ResetToken resetToken = tokenRepository.findByToken(token);
        return resetToken != null && resetToken.isTokenValid();
    }

    @Override
    public void invalidateToken(ResetToken token) {
        tokenRepository.delete(token);
    }

    @Override
    public ResetToken save(ResetToken token) {
        return tokenRepository.save(token);
    }
}
