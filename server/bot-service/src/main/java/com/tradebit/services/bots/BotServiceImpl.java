package com.tradebit.services.bots;

import com.tradebit.dto.BotDTO;
import com.tradebit.exceptions.BotNotFoundException;
import com.tradebit.models.Bot;
import com.tradebit.repositories.BotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class BotServiceImpl implements BotService {
    private final BotRepository botRepository;
    private final BotManager botManager;
    private final ThreadPoolTaskExecutor botTaskExecutor;
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
                .isReadyToBuy(true)
                .isReadyToSell(false)
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
    public Bot getBot(Long botId, String userId) {
        Optional<Bot> botOptional = botRepository.findByIdAndUserId(botId, userId);
        return botOptional.orElseThrow(() ->
                new BotNotFoundException("Bot " + botId + ", with userId: " + userId + " not found!"));
    }

    @Override
    public Bot getBot(Long botId) {
        Optional<Bot> botOptional = botRepository.findById(botId);
        return botOptional.orElseThrow(() ->
                new BotNotFoundException("Bot " + botId + " not found!"));
    }

    @Override
    public boolean toggleBot(Long botId, String userId) {
        Bot bot = getBot(botId, userId);
        boolean newState = !bot.getEnabled();
        botManager.setBotEnabledState(bot, newState);
        botManager.setBotReadyToBuyState(botId, bot.getIsReadyToBuy());
        botManager.setBotReadyToSellState(botId, bot.getIsReadyToSell());
        bot.setEnabled(newState);
        botRepository.saveAndFlush(bot);
        if (newState) {
            botTaskExecutor.execute(() -> botTradingService.trade(bot));
        }
        return newState;
    }
}