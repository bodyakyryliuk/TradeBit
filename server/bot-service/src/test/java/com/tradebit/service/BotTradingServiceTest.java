package com.tradebit.service;

import com.tradebit.services.BinanceUtilService;
import com.tradebit.services.bots.BotServiceImpl;
import com.tradebit.services.bots.BotTradingService;
import com.tradebit.services.bots.BotTradingServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BotTradingServiceTest {
    @Mock
    private BinanceUtilService binanceUtilService;

    @InjectMocks
    private BotTradingServiceImpl botService;

    @Test
    public void givenConditions_whenShouldBuy_thenReturnTrue() {
        String tradingPair = "BTCUSDT";
        double buyThreshold = 5.0;
        double averagePrice = 100.0;
        double currentPrice = 95.0;

        when(binanceUtilService.getCurrentPrice(tradingPair)).thenReturn(currentPrice);

        boolean shouldBuy = botService.shouldBuy(tradingPair, buyThreshold, averagePrice);

        assertTrue(shouldBuy);
    }

    @Test
    public void givenConditions_whenShouldBuy_thenReturnFalse() {
        String tradingPair = "BTCUSDT";
        double buyThreshold = 5.0;
        double averagePrice = 100.0;
        double currentPrice = 102.0;

        when(binanceUtilService.getCurrentPrice(tradingPair)).thenReturn(currentPrice);

        boolean shouldBuy = botService.shouldBuy(tradingPair, buyThreshold, averagePrice);

        assertFalse(shouldBuy);
    }

    @Test
    public void givenConditions_whenShouldSell_thenReturnTrue() {
        String tradingPair = "BTCUSDT";
        double sellThreshold = 5.0;
        double stopLossPercentage = 10.0;
        double buyPrice = 100.0;
        double currentPrice = 110.0;

        when(binanceUtilService.getCurrentPrice(tradingPair)).thenReturn(currentPrice);

        boolean shouldBuy = botService.shouldSell(tradingPair, buyPrice, sellThreshold, stopLossPercentage);

        assertTrue(shouldBuy);
    }

    @Test
    public void givenConditions_whenShouldSell_thenReturnFalse() {
        String tradingPair = "BTCUSDT";
        double sellThreshold = 5.0;
        double stopLossPercentage = 10.0;
        double buyPrice = 100.0;
        double currentPrice = 95.0;

        when(binanceUtilService.getCurrentPrice(tradingPair)).thenReturn(currentPrice);

        boolean shouldBuy = botService.shouldSell(tradingPair, buyPrice, sellThreshold, stopLossPercentage);

        assertFalse(shouldBuy);
    }

}
