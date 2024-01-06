package com.tradebit.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.tradebit.dto.BinanceLinkDTO;
import com.tradebit.dto.BinanceOrderDTO;

public interface BinanceOrderService {
    JsonNode testNewOrder(BinanceOrderDTO orderDTO, BinanceLinkDTO binanceLinkDTO);

    JsonNode makeOrder(BinanceOrderDTO orderDTO, BinanceLinkDTO binanceLinkDTO);

}
