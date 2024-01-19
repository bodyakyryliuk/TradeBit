package com.tradebit.services.bots;

import com.tradebit.dto.BotDTO;
import org.springframework.security.core.Authentication;

public interface BotService {
    void createBot(BotDTO botDTO, String userId);
    void deleteBotById(String botID);
    void toggleBot(Long botId, String userId);

    String getUserIdFromAuthentication(Authentication authentication);

}