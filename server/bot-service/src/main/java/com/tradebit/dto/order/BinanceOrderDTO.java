package com.tradebit.dto.order;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.tradebit.models.order.OrderSide;
import com.tradebit.models.order.OrderType;
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
    private OrderSide side;
    private OrderType type;
    private BigDecimal quantity;
}
