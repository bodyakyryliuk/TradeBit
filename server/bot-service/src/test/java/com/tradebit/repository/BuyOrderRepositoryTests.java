package com.tradebit.repository;

import com.tradebit.models.Bot;
import com.tradebit.models.order.BuyOrder;
import com.tradebit.repositories.BuyOrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
public class BuyOrderRepositoryTests {
    @Autowired
    private BuyOrderRepository buyOrderRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Long botId;

    @BeforeEach
    public void setup(){
        Bot bot = createTestBot();
        botId = bot.getId();

        BuyOrder buyOrder1 = createTestBuyOrderByBot(bot);
        BuyOrder buyOrder2 = createTestBuyOrderByBot(bot);
        BuyOrder buyOrder3 = createTestBuyOrderByBot(bot);

        buyOrderRepository.save(buyOrder1);
        buyOrderRepository.save(buyOrder2);
        buyOrderRepository.save(buyOrder3);
    }

    private Bot createTestBot(){
        Bot bot = Bot.builder()
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

        return entityManager.persist(bot);
    }

    private BuyOrder createTestBuyOrder(){
        Bot bot = createTestBot();

        return BuyOrder.builder()
                .bot(bot)
                .buyPrice(100.0)
                .tradingPair(bot.getTradingPair())
                .quantity(bot.getTradeSize())
                .timestamp(LocalDateTime.now())
                .build();
    }

    private BuyOrder createTestBuyOrderByBot(Bot bot){
        return BuyOrder.builder()
                .bot(bot)
                .buyPrice(100.0)
                .tradingPair(bot.getTradingPair())
                .quantity(bot.getTradeSize())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Test
    public void givenBuyOrderObject_whenSave_thenReturnSavedBuyOrder(){
        BuyOrder buyOrder = createTestBuyOrder();

        BuyOrder savedBuyOrder = buyOrderRepository.save(buyOrder);

        Assertions.assertNotNull(savedBuyOrder);
        Assertions.assertTrue(savedBuyOrder.getId() > 0);
    }

    @Test
    public void givenBuyOrderId_whenFindById_thenReturnFoundBuyOrder() {
        BuyOrder buyOrder = createTestBuyOrder();
        buyOrderRepository.save(buyOrder);

        BuyOrder foundBuyOrder = buyOrderRepository.findById(buyOrder.getId()).orElse(null);

        Assertions.assertNotNull(foundBuyOrder);
        Assertions.assertEquals(buyOrder, foundBuyOrder);
    }

    @Test
    public void givenBotId_whenFindAllByBotId_thenReturnFoundBuyOrders() {
        List<BuyOrder> foundBuyOrders = buyOrderRepository.findAllByBotId(botId);

        Assertions.assertEquals(foundBuyOrders.size(), 3);
    }
}







