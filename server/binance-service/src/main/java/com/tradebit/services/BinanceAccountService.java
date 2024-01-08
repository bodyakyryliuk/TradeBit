package com.tradebit.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.tradebit.dto.BinanceLinkDTO;
import com.tradebit.models.TopUpCoin;
import com.tradebit.models.wallet.WalletInfo;

public interface BinanceAccountService {
    WalletInfo getWallet(BinanceLinkDTO binanceLinkDTO);
    JsonNode getTotalBalance(BinanceLinkDTO binanceLinkDTO);
    JsonNode getAllOrders(BinanceLinkDTO binanceLinkDTO, String symbol);
    JsonNode getAccountData(BinanceLinkDTO binanceLinkDTO);
    JsonNode getTotalBalanceHistory(BinanceLinkDTO binanceLinkDTO, int period);
    JsonNode getTopUpCode(BinanceLinkDTO binanceLinkDTO, TopUpCoin coin);
}
