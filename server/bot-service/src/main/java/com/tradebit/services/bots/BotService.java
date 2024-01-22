package com.tradebit.services.bots;

import com.tradebit.dto.BotDTO;
import com.tradebit.models.Bot;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface BotService {
    Bot createBot(BotDTO botDTO, String userId);
    void deleteBotById(Long botID);
    boolean toggleBot(Long botId, String userId);
    String getUserIdFromAuthentication(Authentication authentication);
    Bot getBot(Long botId, String userId);
    Bot getBot(Long botId);
    List<Bot> getAllByUserId(String userId);
    Bot updateBot(Long botId, BotDTO botDTO);
}
