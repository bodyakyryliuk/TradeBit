package com.tradebit.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tradebit.exceptions.*;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class BinanceResponseProcessingServiceImpl implements BinanceResponseProcessingService {

    public JsonNode processClosePrices(String response) {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode closePricesWithTimeNode = objectMapper.createArrayNode();

        try {
            JsonNode rootNode = objectMapper.readTree(response);
            handleError(rootNode);
            if (rootNode.isArray()) {
                Map<Integer, String> fieldMapping = new HashMap<>();
                fieldMapping.put(1, "openPrice");
                fieldMapping.put(2, "highPrice");
                fieldMapping.put(3, "lowPrice");
                fieldMapping.put(4, "closePrice");
                fieldMapping.put(5, "volume");

                for (JsonNode klineNode : rootNode) {
                    long timestamp = klineNode.get(6).asLong();
                    ObjectNode priceAndTimeNode = objectMapper.createObjectNode();
                    for (Map.Entry<Integer, String> entry : fieldMapping.entrySet()) {
                        priceAndTimeNode.put(entry.getValue(), klineNode.get(entry.getKey()).asDouble());
                    }
                    priceAndTimeNode.put("timestamp", timestamp);

                    closePricesWithTimeNode.add(priceAndTimeNode);
                }
            }

            return closePricesWithTimeNode;
        } catch (JsonProcessingException e) {
            throw new UnexpectedException("Error processing JSON" + e);
        }
    }

    private void handleError(JsonNode rootNode){
        if (rootNode.has("code")) {
            int errorCode = rootNode.get("code").asInt();
            switch (errorCode) {
                case -1121:
                    throw new InvalidSymbolException("Invalid trading pair");
                default:
                    throw new UnexpectedException(rootNode.get("msg").asText());
            }
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
