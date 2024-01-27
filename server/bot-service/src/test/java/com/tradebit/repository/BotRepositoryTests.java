package com.tradebit.repository;

import com.tradebit.models.Bot;
import com.tradebit.repositories.BotRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
public class BotRepositoryTests {
    @Autowired
    private BotRepository botRepository;

    private Bot createTestBot() {
        return Bot.builder()
                .name("Bot1")
                .buyThreshold(2.0)
                .sellThreshold(3.0)
                .stopLossPercentage(5.0)
                .takeProfitPercentage(3.0)
                .tradeSize(0.1)
                .tradingPair("BTCUSDT")
                .userId("userId")
                .enabled(false)
                .isReadyToBuy(true)
                .isReadyToSell(false)
                .hidden(false)
                .build();
    }

    private Bot createTestBotWithUserId(String userId) {
        return Bot.builder()
                .name("Bot1")
                .buyThreshold(2.0)
                .sellThreshold(3.0)
                .stopLossPercentage(5.0)
                .takeProfitPercentage(3.0)
                .tradeSize(0.1)
                .tradingPair("BTCUSDT")
                .userId(userId)
                .enabled(false)
                .isReadyToBuy(true)
                .isReadyToSell(false)
                .hidden(false)
                .build();
    }

    @BeforeEach
    public void setup(){
        Bot bot1 = createTestBotWithUserId("userId1");
        Bot bot2 = createTestBotWithUserId("userId1");
        Bot bot3 = createTestBotWithUserId("userId1");

        botRepository.save(bot1);
        botRepository.save(bot2);
        botRepository.save(bot3);
    }

    @Test
    public void givenBotObject_whenSave_thenReturnSavedBot(){
        Bot bot = createTestBot();

        Bot savedBot = botRepository.save(bot);

        Assertions.assertNotNull(savedBot);
    }

    @Test
    public void givenBotObject_whenFindById_thenReturnFoundBot(){
        Bot bot = createTestBot();
        botRepository.save(bot);

        Bot foundBot = botRepository.findById(bot.getId()).orElse(null);

        Assertions.assertNotNull(foundBot);
        Assertions.assertEquals(bot, foundBot);
    }

    @Test
    public void givenBotObject_whenUpdate_thenReturnUpdatedBot(){
        Bot bot = createTestBot();

        Bot savedBot = botRepository.save(bot);
        savedBot.setName("Updated name");
        Bot updatedBot = botRepository.save(savedBot);
        Assertions.assertNotNull(updatedBot);
        Assertions.assertEquals("Updated name", updatedBot.getName());
    }

    @Test
    public void givenUserId_whenFindByUserId_thenReturnFoundBots(){
        String userId = "userId1";

        List<Bot> bots = botRepository.findAllByUserId(userId);

        Assertions.assertEquals(3, bots.size());
    }

    @Test
    public void givenBotObject_whenDeleteById_thenRemoveBot(){
        Bot bot = createTestBot();
        botRepository.save(bot);

        botRepository.delete(bot);
        Bot deletedBot = botRepository.findById(bot.getId()).orElse(null);

        Assertions.assertNull(deletedBot);
    }

}
