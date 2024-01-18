package com.tradebit.services.bots;

import com.tradebit.dto.order.BinanceOrderDTO;
import com.tradebit.exceptions.BotNotFoundException;
import com.tradebit.exceptions.CurrentPriceFetchException;
import com.tradebit.exceptions.HighestPriceFetchException;
import com.tradebit.models.Bot;
import com.tradebit.models.CurrentPriceResponse;
import com.tradebit.models.HighestPriceResponse;
import com.tradebit.models.order.OrderSide;
import com.tradebit.models.order.OrderType;
import com.tradebit.repositories.BotRepository;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BotTradingServiceImpl implements BotTradingService{
    private final BotManager botManager;
    private final BotRepository botRepository;

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
                if(shouldBuy(currentPrice, highestPrice, bot.getBuyThreshold())){
                    // buy
                }


                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return; // Exit if the thread is interrupted
            }

        }
    }

    private boolean shouldBuy(double currentPrice, double highestPrice, double buyThreshold){
        return currentPrice < highestPrice * (100 - buyThreshold);
    }

    private void executeBuyOrder(String userId, String tradingPair, BigDecimal quantity){
        BinanceOrderDTO orderDTO = createBinanceOrderDto(
                tradingPair, OrderSide.BUY, OrderType.MARKET, quantity);

        //TODO: send a request to binance-service with orderDTO and userId
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
                        .build(tradingPair))                .retrieve()
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
