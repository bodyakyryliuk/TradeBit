package com.tradebit.services;

import com.tradebit.dto.BinanceOrderDTO;

public interface BinanceApiService {
    String getAccountData(String apiKey, String apiSecret);

    String buyCurrency(BinanceOrderDTO orderDTO, String apiKey, String apiSecret);
}
