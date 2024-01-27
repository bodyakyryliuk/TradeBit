package com.tradebit.verificationToken;

import com.tradebit.user.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByUser(User user);
    Optional<VerificationToken> findByToken(String token);
    void deleteByUserId(String userId);
}