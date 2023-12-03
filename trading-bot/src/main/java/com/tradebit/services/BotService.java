package com.tradebit.services;

import com.tradebit.dto.BotDTO;
import com.tradebit.models.Bot;
import org.springframework.security.core.Authentication;

public interface BotService {
    Bot createBot(BotDTO botDTO, String userId);
    String getUserIdFromAuthentication(Authentication authentication);

    void toggleBot(Long botId, String userId);
}
