package com.tradebit.repositories;

import com.tradebit.models.Bot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BotRepository extends JpaRepository<Bot, Long> {
    Bot findByIdAndUserId(Long botId, String userId);
}
