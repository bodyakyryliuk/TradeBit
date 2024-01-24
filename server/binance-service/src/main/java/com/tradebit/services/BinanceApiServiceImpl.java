package com.tradebit.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
    private final BinanceDataUtils binanceDataUtils;
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
        //TODO: now it gets only first 500 cryptos. Update to fetch all
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
    public JsonNode getHighestPriceByPeriod(String tradingPair, int period) {
        double maxPrice = 0.0;
        String maxTimestamp = "";
        JsonNode historicalPrices = getHistoricalPricesForPeriod(tradingPair, period);

        for(JsonNode priceNode: historicalPrices){
            double price = priceNode.get("closePrice").asDouble();
            String timestamp = priceNode.get("timestamp").asText();

            if(price > maxPrice){
                maxPrice = price;
                maxTimestamp = timestamp;
            }
        }

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode resultNode = mapper.createObjectNode();
        resultNode.put("highestPrice", maxPrice);
        resultNode.put("timestamp", maxTimestamp);
        return resultNode;
    }

    @Override
    public Double getAveragePriceByPeriod(String tradingPair, int period) {
        double totalPrice = 0.0;
        int count = 0;
        JsonNode historicalPrices = getHistoricalPricesForPeriod(tradingPair, period);

        for (JsonNode priceNode : historicalPrices) {
            double price = priceNode.get("closePrice").asDouble();
            totalPrice += price;
            count++;
        }

        if (count == 0)
            throw new BinanceRequestException("No data available for the specified period and trading pair: " + tradingPair + ", " + period);

        return totalPrice / count;
    }

    @Override
    public JsonNode getAllTradingPairs() {
        HttpUrl url = binanceRequestService.buildRequestUrl("/api/v3/exchangeInfo");
        Request request = binanceRequestService.buildRequest(url);
        String response = binanceRequestService.executeRequest(request);

        return responseProcessingService.processAllTradingPairsResponse(response);
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
        ObjectMapper objectMapper = new ObjectMapper();

        ArrayNode allPricesNode = objectMapper.createArrayNode();
        String interval = binanceDataUtils.getIntervalFromPeriod(period);
        HttpUrl url = binanceRequestService.buildKlineUrl(tradingPair, interval, startTimeMillis, endTimeMillis - binanceDataUtils.getMillisFromInterval(interval));
        Request request = binanceRequestService.buildRequest(url);
        String response = binanceRequestService.executeRequest(request);

        JsonNode pricesNode = responseProcessingService.processClosePrices(response);

        allPricesNode.addAll((ArrayNode) pricesNode);

        return allPricesNode;
    }



}
