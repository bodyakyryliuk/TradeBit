package com.tradebit.models.wallet;

import com.tradebit.models.wallet.CryptoBalance;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class WalletInfo {
    private List<CryptoBalance> balances;
    private boolean canTrade;
    private boolean canWithdraw;
    private boolean canDeposit;
}