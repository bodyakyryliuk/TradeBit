package com.tradebit.services.bots;

import com.tradebit.dto.BotDTO;
import com.tradebit.models.Bot;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface BotService {
    Bot createBot(BotDTO botDTO, Authentication authentication);
    void deleteBotById(Long botID);
    boolean toggleBot(Long botId, Authentication authentication);
    Bot getBot(Long botId, String userId);
    Bot getBot(Long botId);
    List<Bot> getAllByUserId(Authentication authentication);
    Bot updateBot(Long botId, BotDTO botDTO);
}
