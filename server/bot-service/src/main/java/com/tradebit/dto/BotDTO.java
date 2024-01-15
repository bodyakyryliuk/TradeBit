package com.tradebit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BotDTO {
    private Double buyThreshold;
    private Double sellThreshold;
    private Double takeProfitPercentage;
    private Double stopLossPercentage;
    private Double tradeSize;
    private String tradingPair;
}
