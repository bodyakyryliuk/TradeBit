package com.tradebit.services;

import com.fasterxml.jackson.databind.JsonNode;

public interface BinanceApiService {

    Double getPriceChange(String tradingPair, int period);

    Double getHistoricalPriceForTimestamp(String tradingPair, long timestamp);

    JsonNode getHistoricalPricesForPeriod(String tradingPair, int period);

    JsonNode getCurrentPriceForCrypto(String tradingPair);

    JsonNode getCurrentPrices();

    JsonNode getAllCryptocurrenciesWithUSDT();

    JsonNode getHighestPriceByPeriod(String tradingPair, int period);

    JsonNode getAveragePriceByPeriod(String tradingPair, int period);

    JsonNode getAllTradingPairs();

}
