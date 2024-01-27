package com.tradebit.services.bots;

import com.tradebit.models.Bot;
import com.tradebit.repositories.BotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class BotManager {
    private final Map<Long, Boolean> botEnabledState = new ConcurrentHashMap<>();
    private final Map<Long, Boolean> botReadyToSellState = new ConcurrentHashMap<>();
    private final Map<Long, Boolean> botReadyToBuyState = new ConcurrentHashMap<>();

    private final BotRepository botRepository;

    public void setBotEnabledState(Bot bot, Boolean enabled) {
        botEnabledState.put(bot.getId(), enabled);

        // save to db
        bot.setEnabled(enabled);
        botRepository.saveAndFlush(bot);
    }

    public Boolean getBotEnabledState(Long botId) {
        return botEnabledState.getOrDefault(botId, false);
    }

    public void setBotReadyToSellState(Long botId, Boolean readyToSell) {
        botReadyToSellState.put(botId, readyToSell);
    }

    public void setBotReadyToBuyState(Long botId, Boolean readyToBuy) {
        botReadyToBuyState.put(botId, readyToBuy);
    }

    public boolean isBotReadyToSell(Long botId) {
        return botReadyToSellState.getOrDefault(botId, false);
    }

    public boolean isBotReadyToBuy(Long botId) {
        return botReadyToBuyState.getOrDefault(botId, false);
    }

    public void updateBotTradingState(Bot bot, boolean readyToBuy, boolean readyToSell) {
        bot.setIsReadyToBuy(readyToBuy);
        bot.setIsReadyToSell(readyToSell);
        botRepository.save(bot);

        setBotReadyToBuyState(bot.getId(), readyToBuy);
        setBotReadyToSellState(bot.getId(), readyToSell);
    }
}