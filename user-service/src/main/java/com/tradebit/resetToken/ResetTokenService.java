package com.tradebit.resetToken;

import com.tradebit.user.models.User;
import com.tradebit.verificationToken.VerificationToken;

public interface ResetTokenService {
    ResetToken generateResetToken(User user);
    ResetToken getResetToken(String token);

    ResetToken findByUser(User user);
    void deleteResetToken(ResetToken token);
    boolean isTokenValid(String token);

    void invalidateToken(ResetToken token);

    ResetToken save(ResetToken token);
}
