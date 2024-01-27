package com.tradebit.resetToken;

import com.tradebit.user.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResetTokenRepository extends JpaRepository<ResetToken, Long> {
    ResetToken findByUser(User user);
    ResetToken findByToken(String token);

    void deleteByUserId(String userId);
}
