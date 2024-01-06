package com.tradebit.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.tradebit.dto.BinanceLinkDTO;
import com.tradebit.dto.BinanceOrderDTO;
import lombok.RequiredArgsConstructor;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class BinanceOrderServiceImpl implements BinanceOrderService{
    private final BinanceRequestServiceImpl binanceRequestService;
    private final BinanceResponseProcessingServiceImpl responseProcessingService;
    private JsonNode executeOrder(BinanceOrderDTO orderDTO, String apiKey, String apiSecret, String endpoint) {
        long timeStamp = Instant.now().toEpochMilli();
        String queryString = binanceRequestService.buildQueryString(orderDTO, timeStamp);
        String signature = binanceRequestService.hashHmac(queryString, apiSecret);
        HttpUrl url = binanceRequestService.buildRequestUrl(queryString, signature, endpoint);
        Request request = binanceRequestService.buildRequest(url, apiKey, "POST", RequestBody.create("", null));
        String response = binanceRequestService.executeRequest(request);

        return responseProcessingService.processResponse(response);
    }

    @Override
    public JsonNode makeOrder(BinanceOrderDTO orderDTO, BinanceLinkDTO binanceLinkDTO) {
        return executeOrder(orderDTO, binanceLinkDTO.getApiKey(), binanceLinkDTO.getSecretApiKey(), "/api/v3/order");
    }

    @Override
    public JsonNode testNewOrder(BinanceOrderDTO orderDTO, BinanceLinkDTO binanceLinkDTO) {
        return executeOrder(orderDTO, binanceLinkDTO.getApiKey(), binanceLinkDTO.getSecretApiKey(), "/api/v3/order/test");
    }
}
