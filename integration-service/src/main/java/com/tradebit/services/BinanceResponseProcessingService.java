package com.tradebit.services;

import com.fasterxml.jackson.databind.JsonNode;

public interface BinanceResponseProcessingService {
    JsonNode processClosePrices(String response);
    JsonNode processResponse(String response);
}
