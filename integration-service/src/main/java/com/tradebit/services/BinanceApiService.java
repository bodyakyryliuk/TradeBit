package com.tradebit.services;

import com.tradebit.dto.BinanceLinkDTO;
import com.tradebit.dto.BinanceOrderDTO;

public interface BinanceApiService {
    String getAccountData(BinanceLinkDTO binanceLinkDTO, String endpoint);

    String makeOrder(BinanceOrderDTO orderDTO, BinanceLinkDTO binanceLinkDTO);

    String testNewOrder(BinanceOrderDTO orderDTO, BinanceLinkDTO binanceLinkDTO);

    String getAllOrders(BinanceLinkDTO binanceLinkDTO, String symbol);
}
