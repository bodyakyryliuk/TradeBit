package com.tradebit.services.bots;

import com.fasterxml.jackson.databind.JsonNode;
import com.tradebit.dto.order.BinanceOrderDTO;
import com.tradebit.exceptions.*;
import com.tradebit.models.Bot;
import com.tradebit.models.order.BuyOrder;
import com.tradebit.models.CurrentPriceResponse;
import com.tradebit.models.HighestPriceResponse;
import com.tradebit.models.order.OrderSide;
import com.tradebit.models.order.OrderType;
import com.tradebit.repositories.BotRepository;
import com.tradebit.services.auth.AuthService;
import com.tradebit.services.orders.BuyOrderService;
import com.tradebit.services.orders.SellOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
//import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class BotTradingServiceImpl implements BotTradingService {
    private final BotManager botManager;
    private final AuthService authService;
    private final BuyOrderService buyOrderService;
    private final SellOrderService sellOrderService;
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
            return buyOrderService.save(bot, getCurrentPrice(bot.getTradingPair()));
        }
        return null;
    }

    private void handleSelling(Bot bot, BuyOrder buyOrder){
        if (shouldSell(bot.getTradingPair(), buyOrder.getBuyPrice(), bot.getSellThreshold(), bot.getStopLossPercentage())){
            executeSellTestOrder(bot.getUserId(), buyOrder.getTradingPair(), buyOrder.getQuantity());
            log.info("Executed sell order");
            //TODO: fetch sell price from previously executed order
            sellOrderService.save(bot, buyOrder, getCurrentPrice(bot.getTradingPair()));
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
        double currentPrice = getCurrentPrice(tradingPair);
        if((currentPrice - buyPrice) / currentPrice * 100 >= sellThreshold)
            return true;
        else return (buyPrice - currentPrice) / buyPrice * 100 >= stopLossPercentage;
    }

    private boolean shouldBuy(String tradingPair, double buyThreshold){
        double highestPrice = getHighestPriceForTimePeriod(tradingPair, 168);
        double currentPrice = getCurrentPrice(tradingPair);

        return ((highestPrice - currentPrice) / highestPrice * 100) >= buyThreshold;
    }

    private JsonNode executeBuyTestOrder(String userId, String tradingPair, double quantity){
        BinanceOrderDTO orderDTO = createBinanceOrderDto(
                tradingPair, OrderSide.BUY, OrderType.MARKET, BigDecimal.valueOf(quantity));

        return sendOrderRequest("binance-service/order/testWithUser", orderDTO, userId);
    }

    private JsonNode executeSellTestOrder(String userId, String tradingPair, Double quantity) {
        BinanceOrderDTO orderDTO = createBinanceOrderDto(
                tradingPair, OrderSide.SELL, OrderType.MARKET, BigDecimal.valueOf(quantity));

        return sendOrderRequest("binance-service/order/testWithUser", orderDTO, userId);
    }

    private JsonNode sendOrderRequest(String path, BinanceOrderDTO orderDTO, String userId){
        String accessToken = authService.getAccessToken();

        WebClient webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        Mono<JsonNode> responseMono = webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(path)
                        .queryParam("userId", userId)
                        .build())
                .bodyValue(orderDTO)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        response.bodyToMono(String.class).flatMap(errorBody ->
                                Mono.error(new CustomClientException("Client Error: " + errorBody, response.statusCode().value())))
                )
                .onStatus(HttpStatusCode::is5xxServerError, response ->
                        response.bodyToMono(String.class).flatMap(errorBody ->
                                Mono.error(new CustomClientException("Server Error: " + errorBody, response.statusCode().value())))
                )
                .bodyToMono(JsonNode.class);

        return responseMono.block();
    }

    private BinanceOrderDTO createBinanceOrderDto(
            String tradingPair, OrderSide side, OrderType type, BigDecimal quantity){
        return BinanceOrderDTO.builder()
                .symbol(tradingPair)
                .side(side)
                .type(type)
                .quantity(quantity)
                .build();
    }

    private double getHighestPriceForTimePeriod(String tradingPair, int period){
        Mono<HighestPriceResponse> responseMono = WebClient.builder()
                .baseUrl(baseUrl)
                .build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/binance-service/binance/highestPriceByPeriod/{tradingPair}")
                        .queryParam("period", period)
                        .build(tradingPair))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        response.bodyToMono(String.class).flatMap(errorBody ->
                                Mono.error(new CustomClientException("Client Error: " + errorBody, response.statusCode().value())))
                )
                .onStatus(HttpStatusCode::is5xxServerError, response ->
                        response.bodyToMono(String.class).flatMap(errorBody ->
                                Mono.error(new CustomClientException("Server Error: " + errorBody, response.statusCode().value())))
                )
                .bodyToMono(HighestPriceResponse.class);

        HighestPriceResponse response = responseMono.block();

        if (response == null) {
            throw new HighestPriceFetchException(tradingPair, period);
        }

        return response.getHighestPrice();
    }

    private double getCurrentPrice(String tradingPair){
        Mono<CurrentPriceResponse> responseMono = WebClient.builder()
                .baseUrl(baseUrl)
                .build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/binance-service/binance/currentPrice")
                        .queryParam("tradingPair", tradingPair)
                        .build(tradingPair))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        response.bodyToMono(String.class).flatMap(errorBody ->
                                Mono.error(new CustomClientException("Client Error: " + errorBody, response.statusCode().value())))
                )
                .onStatus(HttpStatusCode::is5xxServerError, response ->
                        response.bodyToMono(String.class).flatMap(errorBody ->
                                Mono.error(new CustomClientException("Server Error: " + errorBody, response.statusCode().value())))
                )
                .bodyToMono(CurrentPriceResponse.class);

        CurrentPriceResponse response = responseMono.block();

        if (response == null) {
            throw new CurrentPriceFetchException(tradingPair);
        }

        return response.getPrice();
    }

}
