package com.tradebit.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.tradebit.dto.order.BinanceOrderDTO;
import com.tradebit.exceptions.CurrentPriceFetchException;
import com.tradebit.exceptions.CustomClientException;
import com.tradebit.exceptions.HighestPriceFetchException;
import com.tradebit.models.CurrentPriceResponse;
import com.tradebit.models.HighestPriceResponse;
import com.tradebit.models.order.OrderSide;
import com.tradebit.models.order.OrderType;
import com.tradebit.services.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class BinanceUtilServiceImpl implements BinanceUtilService{
    private final AuthService authService;
    //    @Value("${api.gateway.host}")
    private final String baseUrl = "http://localhost:8080";
    @Override
    public Double getCurrentPrice(String tradingPair){
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

    @Override
    public Double getHighestPriceForTimePeriod(String tradingPair, int period){
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

    @Override
    public BinanceOrderDTO createBinanceOrderDto(
            String tradingPair, OrderSide side, OrderType type, BigDecimal quantity){
        return BinanceOrderDTO.builder()
                .symbol(tradingPair)
                .side(side)
                .type(type)
                .quantity(quantity)
                .build();
    }

    @Override
    public JsonNode sendOrderRequest(String path, BinanceOrderDTO orderDTO, String userId){
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
}
