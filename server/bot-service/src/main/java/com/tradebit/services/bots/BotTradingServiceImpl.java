package com.tradebit.services.bots;

import com.fasterxml.jackson.databind.JsonNode;
import com.tradebit.dto.order.BinanceOrderDTO;
import com.tradebit.exceptions.BotNotFoundException;
import com.tradebit.exceptions.CurrentPriceFetchException;
import com.tradebit.exceptions.HighestPriceFetchException;
import com.tradebit.models.Bot;
import com.tradebit.models.BuyOrder;
import com.tradebit.models.CurrentPriceResponse;
import com.tradebit.models.HighestPriceResponse;
import com.tradebit.models.order.OrderSide;
import com.tradebit.models.order.OrderType;
import com.tradebit.repositories.BotRepository;
import com.tradebit.repositories.BuyOrderRepository;
import com.tradebit.services.auth.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
//import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class BotTradingServiceImpl implements BotTradingService{
    private final BotManager botManager;
    private final BotRepository botRepository;
    private final AuthService authService;
    private final BuyOrderRepository buyOrderRepository;
//    @Value("${api.gateway.host}")
    private final String baseUrl = "http://localhost:8080";

    // The bot monitors the trading pair's price.
    // if price < recent highest price by buyThreshold percents -> bot buys tradeSize of tradingPair
    // if bot bought something:
    //      remember the price
    //      if current price > buy price by sellThreshold -> sell
    //      if current price < buy price by stop loss percentage -> sell

    @Override
    public void trade(Long botId) {
        Bot bot = botRepository.findById(botId).orElseThrow(() -> new BotNotFoundException(botId));
        String tradingPair = bot.getTradingPair();
        while (botManager.getBotEnabledState(botId)) {
            // firstly to check if bot has bought smth
            double highestPrice = getHighestPriceForTimePeriod(tradingPair, 168);
            double currentPrice = getCurrentPrice(tradingPair);
            try {
                System.out.println("highest price for " + tradingPair + ": " + highestPrice);
                System.out.println("current price for " + tradingPair + ": " + currentPrice);
                System.out.println("current difference: " + ((highestPrice - currentPrice) / highestPrice * 100) +
                        ", buyThreshold: " + bot.getBuyThreshold());
                if(shouldBuy(currentPrice, highestPrice, bot.getBuyThreshold())){
                    // buy
                    executeBuyTestOrder(bot.getUserId(), bot.getTradingPair(), bot.getTradeSize());
                    System.out.println("EXECUTED BUY ORDER");
                    saveBuyOrder(bot, currentPrice);
                    System.out.println("Saved to database");
                }


                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return; // Exit if the thread is interrupted
            }

        }
    }

    private void saveBuyOrder(Bot bot, double buyPrice){
        BuyOrder buyOrder = BuyOrder.builder()
                .bot(bot)
                .buyPrice(buyPrice)
                .tradingPair(bot.getTradingPair())
                .quantity(bot.getTradeSize())
                .timestamp(LocalDateTime.now())
                .sold(Boolean.FALSE)
                .build();

        buyOrderRepository.save(buyOrder);
    }

    private boolean shouldBuy(double currentPrice, double highestPrice, double buyThreshold){
        return ((highestPrice - currentPrice) / highestPrice * 100) >= buyThreshold;
    }

    private JsonNode executeBuyTestOrder(String userId, String tradingPair, double quantity){
        BinanceOrderDTO orderDTO = createBinanceOrderDto(
                tradingPair, OrderSide.BUY, OrderType.MARKET, BigDecimal.valueOf(quantity));

        return sendBuyRequest("binance-service/order/testWithUser", orderDTO, userId);
    }

    private JsonNode sendBuyRequest(String path, BinanceOrderDTO orderDTO, String userId){
        String accessToken = authService.getAccessToken();

        WebClient webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        Mono<JsonNode> responseMono = webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(path)
                        .build())
                .attribute("userId", userId)
                .bodyValue(orderDTO)
                .retrieve()
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
                .bodyToMono(CurrentPriceResponse.class);

        CurrentPriceResponse response = responseMono.block();

        if (response == null) {
            throw new CurrentPriceFetchException(tradingPair);
        }

        return response.getPrice();
    }
}
