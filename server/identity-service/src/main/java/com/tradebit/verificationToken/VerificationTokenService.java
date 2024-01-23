package com.tradebit.verificationToken;


import com.tradebit.user.models.User;

public interface VerificationTokenService {
    VerificationToken generateVerificationToken(User user);
    VerificationToken getVerificationToken(String token);
    void deleteVerificationToken(VerificationToken token);
    void deleteVerificationToken(User user);
    void deleteVerificationTokenIfExists(User user);
}
