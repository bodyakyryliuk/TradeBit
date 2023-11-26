package com.tradebit.services;

import com.tradebit.dto.BinanceOrderDTO;

public interface BinanceApiService {
    String getAccountData(String apiKey, String apiSecret);

    String makeOrder(BinanceOrderDTO orderDTO, String apiKey, String apiSecret);

    String testNewOrder(BinanceOrderDTO orderDTO, String apiKey, String apiSecret);
}
