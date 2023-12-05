package com.tradebit.models.wallet;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CryptoBalance {
    private String asset;
    private double free;
    private double locked;
}
