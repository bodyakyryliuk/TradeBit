package com.tradebit.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.tradebit.dto.BinanceLinkDTO;
import com.tradebit.dto.BinanceOrderDTO;
import com.tradebit.models.wallet.WalletInfo;
import org.json.JSONObject;

public interface BinanceApiService {
    JsonNode getAccountData(BinanceLinkDTO binanceLinkDTO, String endpoint);

    JsonNode makeOrder(BinanceOrderDTO orderDTO, BinanceLinkDTO binanceLinkDTO);

    JsonNode testNewOrder(BinanceOrderDTO orderDTO, BinanceLinkDTO binanceLinkDTO);

    JsonNode getAllOrders(BinanceLinkDTO binanceLinkDTO, String symbol);

    WalletInfo getWallet(BinanceLinkDTO binanceLinkDTO);

    double getTotalBalance(BinanceLinkDTO binanceLinkDTO);

    Double getPriceChange(String tradingPair, int period);

    Double getHistoricalPrice(String tradingPair, long timestamp);

    JsonNode getCurrentPriceForCrypto(String tradingPair);

    JsonNode getCurrentPrices();
}
