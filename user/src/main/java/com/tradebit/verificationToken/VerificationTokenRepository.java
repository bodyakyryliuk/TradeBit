package com.tradebit.verificationToken;

import com.tradebit.user.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    VerificationToken findByUser(User user);
    VerificationToken findByToken(String token);
}