package com.example.TradeBit.registration.verificationToken;

import com.example.TradeBit.user.User;

public interface VerificationTokenService {
    VerificationToken generateVerificationToken(User user);
    VerificationToken getVerificationToken(String token);
    void deleteVerificationToken(VerificationToken token);
}
