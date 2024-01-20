package com.tradebit.services.bots;

import com.tradebit.dto.BotDTO;
import com.tradebit.models.Bot;
import org.springframework.security.core.Authentication;

public interface BotService {
    void createBot(BotDTO botDTO, String userId);
    void deleteBotById(Long botID);
    boolean toggleBot(Long botId, String userId);
    String getUserIdFromAuthentication(Authentication authentication);
    Bot getBot(Long botId, String userId);
    Bot getBot(Long botId);
}
