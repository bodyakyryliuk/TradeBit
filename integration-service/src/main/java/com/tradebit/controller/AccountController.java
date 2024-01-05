package com.tradebit.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.tradebit.dto.BinanceLinkDTO;
import com.tradebit.models.wallet.WalletInfo;
import com.tradebit.services.BinanceAccountService;
import com.tradebit.services.BinanceLinkService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {
    private final BinanceAccountService binanceAccountService;
    private final BinanceLinkService binanceLinkService;

    //TODO: add get mapping for getting account secret and api keys ?

    @PostMapping("/link")
    public ResponseEntity<Map<String, String>> linkAccount(@RequestBody @Valid BinanceLinkDTO binanceLinkDTO,
                                                           Authentication authentication) {
        String userId = binanceLinkService.getUserIdFromAuthentication(authentication);
        binanceLinkService.linkAccount(binanceLinkDTO, userId);

        return ResponseEntity.ok(
                Map.of
                        ("status", "success",
                                "message", "Binance wallet has been linked successfully!"));
    }

    @GetMapping("/myTrades")
    public ResponseEntity<JsonNode> getAllOrders(@RequestBody @Valid BinanceLinkDTO binanceLinkDTO,
                                                 @RequestParam String symbol){
        JsonNode response = binanceAccountService.getAllOrders(binanceLinkDTO, symbol);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/wallet")
    public ResponseEntity<WalletInfo> getWallet(@RequestBody @Valid BinanceLinkDTO binanceLinkDTO){
        WalletInfo response = binanceAccountService.getWallet(binanceLinkDTO);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/totalBalance")
    public ResponseEntity<JsonNode> getTotalBalance(@RequestBody @Valid BinanceLinkDTO binanceLinkDTO){
        JsonNode response = binanceAccountService.getTotalBalance(binanceLinkDTO);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/balanceHistory")
    public ResponseEntity<JsonNode> getBalanceHistory(
            @RequestBody @Valid BinanceLinkDTO binanceLinkDTO,
            @RequestParam (name = "period", required = false, defaultValue = "24") int period){
        JsonNode response = binanceAccountService.getTotalBalanceHistory(binanceLinkDTO, period);

        return ResponseEntity.ok(response);
    }

}
