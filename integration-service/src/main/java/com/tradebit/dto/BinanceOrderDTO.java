package com.tradebit.dto;

import com.tradebit.models.OrderSide;
import com.tradebit.models.OrderType;
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
