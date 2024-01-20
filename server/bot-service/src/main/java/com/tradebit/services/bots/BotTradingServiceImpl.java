package com.tradebit.services.bots;

import com.fasterxml.jackson.databind.JsonNode;
import com.tradebit.dto.order.BinanceOrderDTO;
import com.tradebit.models.Bot;
import com.tradebit.models.order.BuyOrder;
import com.tradebit.models.order.OrderSide;
import com.tradebit.models.order.OrderType;
import com.tradebit.services.BinanceUtilService;
import com.tradebit.services.orders.BuyOrderService;
import com.tradebit.services.orders.SellOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
@RequiredArgsConstructor
@Slf4j
public class BotTradingServiceImpl implements BotTradingService {
    private final BotManager botManager;
    private final BuyOrderService buyOrderService;
    private final SellOrderService sellOrderService;
    private final BinanceUtilService binanceUtilService;
//    @Value("${api.gateway.host}")
    private final String baseUrl = "http://localhost:8080";

    // The bot monitors the trading pair's price.
    // if price < recent highest price by buyThreshold percents -> bot buys tradeSize of tradingPair
    // if bot bought something:
    //      remember the price
    //      if current price > buy price by sellThreshold -> sell
    //      if current price < buy price by stop loss percentage -> sell

    @Override
    public void trade(Bot bot) {
        BuyOrder buyOrder = null;
        if (bot.getIsReadyToSell())
            buyOrder = buyOrderService.getRecentBuyOrderByBot(bot);

        while (botManager.getBotEnabledState(bot.getId())) {
            if (botManager.isBotReadyToSell(bot.getId())){
                handleSelling(bot, buyOrder);
            } else if (botManager.isBotReadyToBuy(bot.getId())){
                BuyOrder executedOrder = handleBuying(bot);
                if (executedOrder != null)
                    buyOrder = executedOrder;
            }
            sleep();
        }
    }

    private BuyOrder handleBuying(Bot bot){
        if (shouldBuy(bot.getTradingPair(), bot.getBuyThreshold())) {
            executeBuyTestOrder(bot.getUserId(), bot.getTradingPair(), bot.getTradeSize());
            log.info("Executed buy order");
            //TODO: fetch buy price from previously executed order
            botManager.updateBotTradingState(bot, false, true);
            return buyOrderService.save(bot, binanceUtilService.getCurrentPrice(bot.getTradingPair()));
        }
        return null;
    }

    private void handleSelling(Bot bot, BuyOrder buyOrder){
        if (shouldSell(bot.getTradingPair(), buyOrder.getBuyPrice(), bot.getSellThreshold(), bot.getStopLossPercentage())){
            executeSellTestOrder(bot.getUserId(), buyOrder.getTradingPair(), buyOrder.getQuantity());
            log.info("Executed sell order");
            //TODO: fetch sell price from previously executed order
            sellOrderService.save(bot, buyOrder, binanceUtilService.getCurrentPrice(bot.getTradingPair()));
            botManager.updateBotTradingState(bot, true, false);
        }
    }

    private void sleep(){
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }


    private boolean shouldSell(String tradingPair, double buyPrice, double sellThreshold, double stopLossPercentage){
        double currentPrice = binanceUtilService.getCurrentPrice(tradingPair);
        if((currentPrice - buyPrice) / currentPrice * 100 >= sellThreshold)
            return true;
        else return (buyPrice - currentPrice) / buyPrice * 100 >= stopLossPercentage;
    }

    private boolean shouldBuy(String tradingPair, double buyThreshold){
        double highestPrice = binanceUtilService.getHighestPriceForTimePeriod(tradingPair, 168);
        double currentPrice = binanceUtilService.getCurrentPrice(tradingPair);

        return ((highestPrice - currentPrice) / highestPrice * 100) >= buyThreshold;
    }

    private JsonNode executeBuyTestOrder(String userId, String tradingPair, double quantity){
        BinanceOrderDTO orderDTO = binanceUtilService.createBinanceOrderDto(
                tradingPair, OrderSide.BUY, OrderType.MARKET, BigDecimal.valueOf(quantity));

        return binanceUtilService.sendOrderRequest("binance-service/order/testWithUser", orderDTO, userId);
    }

    private JsonNode executeSellTestOrder(String userId, String tradingPair, Double quantity) {
        BinanceOrderDTO orderDTO = binanceUtilService.createBinanceOrderDto(
                tradingPair, OrderSide.SELL, OrderType.MARKET, BigDecimal.valueOf(quantity));

        return binanceUtilService.sendOrderRequest("binance-service/order/testWithUser", orderDTO, userId);
    }


}
