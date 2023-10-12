package com.example.TradeBit.registration.verificationToken;

import com.example.TradeBit.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    VerificationToken findByUser(User user);
    VerificationToken findByToken(String token);
}
