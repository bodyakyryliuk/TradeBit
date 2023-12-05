package com.tradebit.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.tradebit.dto.BinanceLinkDTO;
import com.tradebit.dto.BinanceOrderDTO;
import org.json.JSONObject;

public interface BinanceApiService {
    JsonNode getAccountData(BinanceLinkDTO binanceLinkDTO, String endpoint);

    JsonNode makeOrder(BinanceOrderDTO orderDTO, BinanceLinkDTO binanceLinkDTO);

    JsonNode testNewOrder(BinanceOrderDTO orderDTO, BinanceLinkDTO binanceLinkDTO);

    JsonNode getAllOrders(BinanceLinkDTO binanceLinkDTO, String symbol);

    JsonNode getWallet(BinanceLinkDTO binanceLinkDTO);

//    Double getTotalBalance(BinanceLinkDTO binanceLinkDTO);
}
