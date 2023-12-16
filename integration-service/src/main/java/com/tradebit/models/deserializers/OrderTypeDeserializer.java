package com.tradebit.models.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.tradebit.exceptions.InvalidSideException;
import com.tradebit.models.OrderType;

import java.io.IOException;

public class OrderTypeDeserializer extends JsonDeserializer<OrderType> {
    @Override
    public OrderType deserialize(JsonParser jp, DeserializationContext context) throws IOException {
        String value = jp.getText();
        try {
            return OrderType.valueOf(value);
        } catch (IllegalArgumentException e) {
            throw new InvalidSideException("Invalid order type: " + value);
        }
    }
}
