package com.tradebit.models.wallet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalletInfo {
    private List<CryptoBalance> balances;
    private boolean canTrade;
    private boolean canWithdraw;
    private boolean canDeposit;
}