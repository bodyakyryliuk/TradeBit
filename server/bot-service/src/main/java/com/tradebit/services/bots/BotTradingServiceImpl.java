package com.tradebit.services.bots;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BotTradingServiceImpl implements BotTradingService{
    private final BotManager botManager;

    @Override
    public void trade(Long botId) {
        while (botManager.getBotEnabledState(botId)) {
            try {
                System.out.println("Bot" + botId + "is trading");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return; // Exit if the thread is interrupted
            }
        }
    }
}
