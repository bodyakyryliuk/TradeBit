package com.tradebit.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.tradebit.exceptions.BinanceRequestException;
import lombok.RequiredArgsConstructor;
import okhttp3.HttpUrl;
import okhttp3.Request;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class BinanceApiServiceImpl implements BinanceApiService{
    private final BinanceRequestService binanceRequestService;
    private final BinanceResponseProcessingService responseProcessingService;
    public JsonNode getCurrentPrices() {
        HttpUrl url = binanceRequestService.buildRequestUrl("/api/v3/ticker/price");
        Request request = binanceRequestService.buildRequest(url);
        String response = binanceRequestService.executeRequest(request);

        return responseProcessingService.processResponse(response);
    }

    @Override
    public JsonNode getCurrentPriceForCrypto(String tradingPair) {
        HttpUrl url = binanceRequestService.buildRequestUrl("/api/v3/ticker/price?symbol=" + tradingPair);
        Request request = binanceRequestService.buildRequest(url);
        String response = binanceRequestService.executeRequest(request);

        return responseProcessingService.processResponse(response);

    }

    /*
        Method returns json node of all cryptocurrencies with USDT with prices
        [
            {
                "symbol": "ETHBTC",
                "price": "0.05367000"
            },
            {
                "symbol": "LTCBTC",
                "price": "0.00169800"
            },
        ]
     */
    @Override
    public JsonNode getAllCryptocurrenciesWithUSDT() {
        JsonNode allCryptos = getCurrentPrices();
        ObjectMapper mapper = new ObjectMapper();
        // This will hold the filtered results
        ArrayNode result = mapper.createArrayNode();

        if (allCryptos.isArray()) {
            for (JsonNode cryptoNode : allCryptos) {
                String symbol = cryptoNode.get("symbol").asText();
                if (symbol.contains("USDT")) {
                    result.add(cryptoNode);
                }
            }
        }

        return result;
    }


    @Override
    public Double getPriceChange(String tradingPair, int period) {
        long periodAgoMillis = Instant.now().minusMillis(period * 3600000L).toEpochMilli();
        Double historicalPrice = getHistoricalPriceForTimestamp(tradingPair, periodAgoMillis);
        JsonNode jsonNode = getCurrentPriceForCrypto(tradingPair);
        Double currentPrice = jsonNode.get("price").asDouble();

        if (!historicalPrice.isNaN() && !currentPrice.isNaN())
            return (currentPrice - historicalPrice) / historicalPrice * 100;
        else
            throw new BinanceRequestException("No data available for the specified period");
    }

    @Override
    public Double getHistoricalPriceForTimestamp(String tradingPair, long timestamp) {
        long adjustedTimestamp = timestamp - (timestamp % (3600000L));
        HttpUrl url = binanceRequestService.buildKlineUrl(tradingPair, "1m", adjustedTimestamp);
        Request request = binanceRequestService.buildRequest(url);
        String response = binanceRequestService.executeRequest(request);

        JsonNode jsonResponse = responseProcessingService.processResponse(response);
        if (jsonResponse.isArray() && !jsonResponse.isEmpty()) {
            return jsonResponse.get(0).get(4).asDouble(); // Close price of the kline
        } else {
            throw new BinanceRequestException("No data available for the specified timestamp.");
        }
    }

    @Override
    public JsonNode getHistoricalPricesForPeriod(String tradingPair, int period) {
        long endTimeMillis = Instant.now().toEpochMilli();
        long startTimeMillis = Instant.now().minusMillis(period * 3600000L).toEpochMilli();

        HttpUrl url = binanceRequestService.buildKlineUrl(tradingPair, "1m", startTimeMillis, endTimeMillis);
        Request request = binanceRequestService.buildRequest(url);
        String response = binanceRequestService.executeRequest(request);

        return responseProcessingService.processClosePrices(response);
    }


}
