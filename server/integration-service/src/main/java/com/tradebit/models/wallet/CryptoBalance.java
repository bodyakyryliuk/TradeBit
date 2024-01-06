package com.tradebit.models.wallet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CryptoBalance {
    private String asset;
    private double free;
    private double locked;
    private Double priceChange;
}
