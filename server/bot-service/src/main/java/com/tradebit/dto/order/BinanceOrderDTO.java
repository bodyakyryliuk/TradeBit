package com.tradebit.dto.order;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
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
