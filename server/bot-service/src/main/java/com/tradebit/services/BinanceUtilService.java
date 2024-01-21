package com.tradebit.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.tradebit.dto.order.BinanceOrderDTO;
import com.tradebit.exceptions.CustomClientException;
import com.tradebit.models.order.OrderSide;
import com.tradebit.models.order.OrderType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface BinanceUtilService {
    Double getCurrentPrice(String tradingPair);
    Double getHighestPriceForTimePeriod(String tradingPair, int period);
    BinanceOrderDTO createBinanceOrderDto(
            String tradingPair, OrderSide side, OrderType type, BigDecimal quantity);
    JsonNode sendOrderRequest(String path, BinanceOrderDTO orderDTO, String userId);

    Double getPriceFromOrder(JsonNode jsonNode);
}
