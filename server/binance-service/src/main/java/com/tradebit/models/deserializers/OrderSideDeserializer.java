package com.tradebit.models.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.tradebit.exceptions.InvalidSideException;
import com.tradebit.models.OrderSide;

import java.io.IOException;

public class OrderSideDeserializer extends JsonDeserializer<OrderSide> {
    @Override
    public OrderSide deserialize(JsonParser jp, DeserializationContext context) throws IOException {
        String value = jp.getText();
        try {
            return OrderSide.valueOf(value);
        } catch (IllegalArgumentException e) {
            throw new InvalidSideException("Invalid order side: " + value);
        }
    }
}
