package com.tradebit.services.bots;

import com.tradebit.dto.BotDTO;
import com.tradebit.exceptions.BotEnabledException;
import com.tradebit.exceptions.BotNameAlreadyExistsException;
import com.tradebit.exceptions.BotNotFoundException;
import com.tradebit.exceptions.MaxBotsLimitExceededException;
import com.tradebit.models.Bot;
import com.tradebit.repositories.BotRepository;
import com.tradebit.repositories.BuyOrderRepository;
import com.tradebit.repositories.SellOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class BotServiceImpl implements BotService {
    private final BotRepository botRepository;
    private final BotManager botManager;
    private final ThreadPoolTaskExecutor botTaskExecutor;
    private final BotTradingService botTradingService;
    private final BuyOrderRepository buyOrderRepository;
    private final SellOrderRepository sellOrderRepository;

    @Override
    public Bot createBot(BotDTO botDTO, Authentication authentication) {
        String userId = getUserIdFromAuthentication(authentication);

        if (canCreateBot(botDTO.getName(), userId)) {
            Bot bot = Bot.builder()
                    .name(botDTO.getName())
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
                    .hidden(false)
                    .build();

           return botRepository.save(bot);
        }
        return null;
    }

    private boolean canCreateBot(String name, String userId){
        int count = botRepository.countAllByUserIdAndHidden(userId, false);
        boolean exists = botRepository.existsByNameAndUserIdAndHidden(name, userId, false);

        if (count >= 10)
            throw new MaxBotsLimitExceededException("Exceeded limit of bots allowed");
        else if(exists)
            throw new BotNameAlreadyExistsException();
        return true;
    }

    private void canEnableBot(String userId){
        int count = botRepository.countAllByUserIdAndEnabled(userId, true);

        if (count >= 5)
            throw new MaxBotsLimitExceededException("Exceeded limit of enabled bots allowed");
    }

    @Override
    public void deleteBotById(Long botId) {
        Bot bot = botRepository.findById(botId)
                .orElseThrow(() -> new BotNotFoundException("Bot with ID: " + botId + " not found"));

        if (buyOrderRepository.existsByBot(bot) || sellOrderRepository.existsByBot(bot)){
            bot.setHidden(true);
            botManager.setBotEnabledState(bot, false);
            botRepository.save(bot);
        }else
            botRepository.deleteById(botId);

    }

    private String getUserIdFromAuthentication(Authentication authentication) {
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
    public List<Bot> getAllByUserId(Authentication authentication) {
        String userId = getUserIdFromAuthentication(authentication);

        List<Bot> bots = botRepository.findAllByUserId(userId);
        if (bots.isEmpty())
            throw new BotNotFoundException("No bot found by userId: " + userId);

        return bots;
    }

    @Override
    public Bot updateBot(Long botId, BotDTO botDTO) {
        Bot bot = getBot(botId);
        if (bot.getEnabled())
            throw new BotEnabledException("Cannot update a bot while it is enabled");

        bot.setName(botDTO.getName());
        bot.setBuyThreshold(botDTO.getBuyThreshold());
        bot.setSellThreshold(botDTO.getSellThreshold());
        bot.setTakeProfitPercentage(botDTO.getTakeProfitPercentage());
        bot.setStopLossPercentage(botDTO.getStopLossPercentage());
        bot.setTradeSize(botDTO.getTradeSize());
        bot.setTradingPair(botDTO.getTradingPair());

        return botRepository.save(bot);
    }

    @Override
    public boolean toggleBot(Long botId, Authentication authentication) {
        String userId = getUserIdFromAuthentication(authentication);
        Bot bot = getBot(botId, userId);
        boolean newState = !bot.getEnabled();

        if (newState)
            canEnableBot(userId);

        botManager.setBotEnabledState(bot, newState);
        botManager.setBotReadyToBuyState(botId, bot.getIsReadyToBuy());
        botManager.setBotReadyToSellState(botId, bot.getIsReadyToSell());

        if (newState) {
            botTaskExecutor.execute(() -> botTradingService.trade(bot));
        }

        return newState;
    }
}