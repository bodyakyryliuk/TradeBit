package com.tradebit.services.bots;

import com.tradebit.models.Bot;
import com.tradebit.repositories.BotRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BotInitializationService {
    private final BotRepository botRepository;
    private final BotManager botManager;
    private final ThreadPoolTaskExecutor botTaskExecutor;
    private final BotTradingService botTradingService;

    @PostConstruct
    public void init() {
        List<Bot> bots = botRepository.findAll();
        for (Bot bot : bots) {
            botManager.setBotEnabledState(bot, bot.getEnabled());
            botManager.setBotReadyToBuyState(bot.getId(), bot.getIsReadyToBuy());
            botManager.setBotReadyToSellState(bot.getId(), bot.getIsReadyToSell());

            if (bot.getEnabled()) {
                botTaskExecutor.execute(() -> botTradingService.trade(bot));
            }
        }
    }

}
