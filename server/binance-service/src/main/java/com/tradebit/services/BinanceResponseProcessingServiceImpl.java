package com.tradebit.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.tradebit.exceptions.BinanceRequestException;
import com.tradebit.exceptions.InsufficientBalanceException;
import com.tradebit.exceptions.InvalidQuantityException;
import com.tradebit.exceptions.UnexpectedException;
import org.springframework.stereotype.Service;

@Service
public class BinanceResponseProcessingServiceImpl implements BinanceResponseProcessingService {

    public JsonNode processClosePrices(String response) {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode closePricesNode = objectMapper.createArrayNode();

        try {
            JsonNode rootNode = objectMapper.readTree(response);
            if (rootNode.isArray()) {
                for (JsonNode klineNode : rootNode) {
                    // The close price is the 4th element in the kline array
                    JsonNode closePrice = klineNode.get(4);
                    closePricesNode.add(closePrice);
                }
            }

            return closePricesNode;
        } catch (JsonProcessingException e) {
            throw new UnexpectedException("Error processing JSON" + e);
        }
    }

    public JsonNode processResponse(String response) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            if (response.startsWith("["))
                return objectMapper.readTree(response);

            JsonNode responseNode = objectMapper.readTree(response);

            if (responseNode.has("code") && responseNode.has("msg")) {
                String message = responseNode.get("msg").asText();
                switch (message) {
                    case "Filter failure: LOT_SIZE", "Filter failure: NOTIONAL" -> throw new InvalidQuantityException("Invalid quantity");
                    case "Account has insufficient balance for requested action." -> throw new InsufficientBalanceException();
                    default -> throw new BinanceRequestException(message);
                }
            }

            return responseNode;
        } catch (JsonProcessingException e) {
            throw new UnexpectedException(e.getMessage());
        }
    }
}
