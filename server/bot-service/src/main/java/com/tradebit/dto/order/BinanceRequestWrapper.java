package com.tradebit.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BinanceRequestWrapper {
    private BinanceOrderDTO orderDTO;
    private BinanceLinkDTO linkDTO;
}