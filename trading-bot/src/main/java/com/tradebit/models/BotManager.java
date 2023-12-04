package com.tradebit.models;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class BotManager {
    private final Map<Long, Boolean> botEnabledState = new ConcurrentHashMap<>();

    public void setBotEnabledState(Long botId, Boolean enabled) {
        botEnabledState.put(botId, enabled);
    }

    public Boolean getBotEnabledState(Long botId) {
        return botEnabledState.getOrDefault(botId, false);
    }
}
