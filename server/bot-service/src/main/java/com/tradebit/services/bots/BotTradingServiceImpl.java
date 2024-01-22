package com.tradebit.services.bots;

import com.fasterxml.jackson.databind.JsonNode;
import com.tradebit.dto.order.BinanceOrderDTO;
import com.tradebit.exceptions.CustomClientException;
import com.tradebit.exceptions.CustomServerException;
import com.tradebit.models.Bot;
import com.tradebit.models.order.BuyOrder;
import com.tradebit.models.order.OrderSide;
import com.tradebit.models.order.OrderType;
import com.tradebit.models.order.SellOrder;
import com.tradebit.services.BinanceUtilService;
import com.tradebit.services.EmailService;
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
    private final EmailService emailService;

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
            try {
                JsonNode order = executeBuyOrder(bot.getUserId(), bot.getTradingPair(), bot.getTradeSize());
                log.info("Executed buy order");
                Double buyPrice = binanceUtilService.getPriceFromOrder(order);
                BuyOrder buyOrder = buyOrderService.save(bot, buyPrice);
                botManager.updateBotTradingState(bot, false, true);
                emailService.sendBuyOrderEmail(buyOrder, bot.getUserId());
                return buyOrder;
            }catch (CustomClientException | CustomServerException e){
                log.warn(e.getMessage());
                botManager.setBotEnabledState(bot, false);
            }

        }
        return null;
    }

    private void handleSelling(Bot bot, BuyOrder buyOrder){
        if (shouldSell(bot.getTradingPair(), buyOrder.getBuyPrice(), bot.getSellThreshold(), bot.getStopLossPercentage())){
            try {
                JsonNode order = executeSellOrder(bot.getUserId(), buyOrder.getTradingPair(), buyOrder.getQuantity());
                log.info("Executed sell order");
                Double sellPrice = binanceUtilService.getPriceFromOrder(order);
                SellOrder sellOrder = sellOrderService.save(bot, buyOrder, sellPrice);
                botManager.updateBotTradingState(bot, true, false);
                emailService.sendSellOrderEmail(sellOrder, bot.getUserId());
            }catch (CustomClientException e){
                log.warn(e.getMessage());
                botManager.setBotEnabledState(bot, false);
            }
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

    private JsonNode executeBuyOrder(String userId, String tradingPair, double quantity) {
        BinanceOrderDTO orderDTO = binanceUtilService.createBinanceOrderDto(
                tradingPair, OrderSide.BUY, OrderType.MARKET, BigDecimal.valueOf(quantity));

        return binanceUtilService.sendOrderRequest("binance-service/order/createWithUser", orderDTO, userId);
    }

    private JsonNode executeSellOrder(String userId, String tradingPair, Double quantity) {
        BinanceOrderDTO orderDTO = binanceUtilService.createBinanceOrderDto(
                tradingPair, OrderSide.SELL, OrderType.MARKET, BigDecimal.valueOf(quantity));

        return binanceUtilService.sendOrderRequest("binance-service/order/createWithUser", orderDTO, userId);
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
