package com.tradebit.repository;

import com.tradebit.models.Bot;
import com.tradebit.models.order.BuyOrder;
import com.tradebit.models.order.SellOrder;
import com.tradebit.repositories.SellOrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
public class SellOrderRepositoryTests {
    @Autowired
    private SellOrderRepository sellOrderRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Long botId;

    @BeforeEach
    public void setup(){
        Bot bot = createTestBot();
        botId = bot.getId();

        SellOrder sellOrder1 = createTestSellOrderByBot(bot);
        SellOrder sellOrder2 = createTestSellOrderByBot(bot);
        SellOrder sellOrder3 = createTestSellOrderByBot(bot);

        sellOrderRepository.save(sellOrder1);
        sellOrderRepository.save(sellOrder2);
        sellOrderRepository.save(sellOrder3);
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

        BuyOrder buyOrder = BuyOrder.builder()
                            .bot(bot)
                            .buyPrice(100.0)
                            .tradingPair(bot.getTradingPair())
                            .quantity(bot.getTradeSize())
                            .timestamp(LocalDateTime.now())
                            .build();

        return entityManager.persist(buyOrder);
    }

    private SellOrder createTestSellOrder(){
        Bot bot = createTestBot();
        BuyOrder buyOrder = createTestBuyOrder();

        return SellOrder.builder()
                .bot(bot)
                .buyOrder(buyOrder)
                .sellPrice(110.0)
                .profit(10.0)
                .tradingPair(bot.getTradingPair())
                .quantity(bot.getTradeSize())
                .timestamp(LocalDateTime.now())
                .build();
    }

    private SellOrder createTestSellOrderByBot(Bot bot){
        BuyOrder buyOrder = createTestBuyOrder();

        return SellOrder.builder()
                .bot(bot)
                .buyOrder(buyOrder)
                .sellPrice(110.0)
                .profit(10.0)
                .tradingPair(bot.getTradingPair())
                .quantity(bot.getTradeSize())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Test
    public void givenSellOrderObject_whenSave_thenReturnSavedSellOrder(){
        SellOrder sellOrder = createTestSellOrder();

        SellOrder savedSellOrder = sellOrderRepository.save(sellOrder);

        Assertions.assertNotNull(savedSellOrder);
        Assertions.assertTrue(savedSellOrder.getId() > 0);
    }

    @Test
    public void givenSellOrderId_whenFindById_thenReturnFoundSellOrder(){
        SellOrder sellOrder = createTestSellOrder();
        sellOrderRepository.save(sellOrder);

        SellOrder foundSellOrder = sellOrderRepository.findById(sellOrder.getId()).orElse(null);

        Assertions.assertNotNull(foundSellOrder);
        Assertions.assertEquals(sellOrder, foundSellOrder);
    }

    @Test
    public void givenBotId_whenFindAllByBotId_thenReturnFoundSellOrders(){
        List<SellOrder> foundSellOrders = sellOrderRepository.findAllByBotId(botId);

        Assertions.assertEquals(3, foundSellOrders.size());
    }


}




























