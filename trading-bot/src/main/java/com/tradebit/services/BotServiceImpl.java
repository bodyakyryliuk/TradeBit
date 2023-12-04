package com.tradebit.services;

import com.tradebit.dto.BotDTO;
import com.tradebit.exceptions.BotNotFoundException;
import com.tradebit.models.Bot;
import com.tradebit.models.BotManager;
import com.tradebit.repositories.BotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Random;


@Service
@RequiredArgsConstructor
public class BotServiceImpl implements BotService{
    private final BotRepository botRepository;
    private final ThreadPoolTaskExecutor botTaskExecutor;
    private final BotManager botManager;

    @Override
    public Bot createBot(BotDTO botDTO, String userId) {
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

        return botRepository.save(bot);
    }

    @Override
    public String getUserIdFromAuthentication(Authentication authentication) {
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
        return jwtAuthenticationToken.getToken().getClaimAsString("sub");
    }

    //TODO: implement trading strategy
    private void executeTrading(Long botId) {
        Random random = new Random();
        while (botManager.getBotEnabledState(botId)) {
            try {
                System.out.println("Bot " + botId + " is trading");
                Thread.sleep(500 + random.nextInt(1000));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return; // Exit if the thread is interrupted
            }
        }
    }


    /*
        TODO: allow only one bot working with one trading pair
        Otherwise, bots will interfere in trading process of each one,
        making trading unpredictable
     */
    @Override
    public void toggleBot(Long botId, String userId) {
        Bot bot = botRepository.findByIdAndUserId(botId, userId);
        if (bot == null)
            throw new BotNotFoundException(botId);
        boolean newState = !bot.getEnabled();
        botManager.setBotEnabledState(botId, newState);
        bot.setEnabled(newState);
        botRepository.saveAndFlush(bot);
        if(newState){
            botTaskExecutor.execute(() -> executeTrading(botId));
        }
    }
}
