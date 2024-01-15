package com.tradebit.services.bots;

import com.tradebit.dto.BotDTO;
import com.tradebit.models.Bot;
import com.tradebit.repositories.BotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class BotServiceImpl implements BotService {
    private final BotRepository botRepository;
    private final ThreadPoolTaskExecutor botTaskExecutor;
    private final BotManager botManager;
    private final BotTradingService botTradingService;

    @Override
    public void createBot(BotDTO botDTO, String userId) {
        Bot bot = Bot.builder()
                .buyThreshold(botDTO.getBuyThreshold())
                .sellThreshold(botDTO.getSellThreshold())
                .takeProfitPercentage(botDTO.getTakeProfitPercentage())
                .stopLossPercentage(botDTO.getStopLossPercentage())
                .tradeSize(botDTO.getTradeSize())
                .tradingPair(botDTO.getTradingPair())
                .userId(userId)
                .enabled(false)
                .build();

        botRepository.save(bot);
    }

    @Override
    public void deleteBotById(String botID) {

    }

    @Override
    public String getUserIdFromAuthentication(Authentication authentication) {
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
        return jwtAuthenticationToken.getToken().getClaimAsString("sub");
    }

    @Override
    public void toggleBot(Long botId, String userId) {
        Bot bot = botRepository.findByIdAndUserId(botId, userId);
        //TODO: check if bot is not null
        boolean newState = !bot.getEnabled();
        botManager.setBotEnabledState(botId, newState);
        bot.setEnabled(newState);
        botRepository.saveAndFlush(bot);
        if(newState){
            botTaskExecutor.execute(() -> botTradingService.trade(botId));
        }
    }
}