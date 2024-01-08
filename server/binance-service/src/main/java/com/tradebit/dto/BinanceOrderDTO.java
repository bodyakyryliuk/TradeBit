package com.tradebit.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.tradebit.models.OrderSide;
import com.tradebit.models.OrderType;
import com.tradebit.models.deserializers.OrderSideDeserializer;
import com.tradebit.models.deserializers.OrderTypeDeserializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BinanceOrderDTO {
    private String symbol;
    @JsonDeserialize(using = OrderSideDeserializer.class)
    private OrderSide side;
    @JsonDeserialize(using = OrderTypeDeserializer.class)
    private OrderType type;
    private BigDecimal quantity;
}
