package com.tradebit.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.tradebit.dto.BinanceLinkDTO;
import com.tradebit.dto.BinanceOrderDTO;
import com.tradebit.dto.BinanceRequestWrapper;
import com.tradebit.services.BinanceApiService;
import com.tradebit.services.BinanceLinkService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/binance")
@RequiredArgsConstructor
public class BinanceController {
    private final BinanceLinkService binanceLinkService;
    private final BinanceApiService binanceApiService;

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

    @PostMapping("/order")
    public ResponseEntity<JsonNode> createOrder(@RequestBody @Valid BinanceRequestWrapper wrapper){
        BinanceOrderDTO orderDTO = wrapper.getOrderDTO();
        BinanceLinkDTO linkDTO = wrapper.getLinkDTO();
        JsonNode response = binanceApiService.makeOrder(orderDTO, linkDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/order/test")
    public ResponseEntity<JsonNode> createTestOrder(@RequestBody @Valid BinanceRequestWrapper wrapper){
        BinanceOrderDTO orderDTO = wrapper.getOrderDTO();
        BinanceLinkDTO linkDTO = wrapper.getLinkDTO();
        JsonNode response = binanceApiService.testNewOrder(orderDTO, linkDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/myTrades")
    public ResponseEntity<JsonNode> getAllOrders(@RequestBody @Valid BinanceLinkDTO binanceLinkDTO,
                                                            @RequestParam String symbol){
        JsonNode response = binanceApiService.getAllOrders(binanceLinkDTO, symbol);

        return ResponseEntity.ok(response);
    }

    //TODO: add endpoint for getting wallet data
    @GetMapping("/wallet")
    public ResponseEntity<JsonNode> getWallet(@RequestBody @Valid BinanceLinkDTO binanceLinkDTO){
        JsonNode response = binanceApiService.getWallet(binanceLinkDTO);

        return ResponseEntity.ok(response);
    }

//    @GetMapping("/totalBalance")
//    public ResponseEntity<Map<String, String>> getAccountData(@RequestBody @Valid BinanceLinkDTO binanceLinkDTO){
//        Double response = binanceApiService.getTotalBalance(binanceLinkDTO);
//
//        return ResponseEntity.ok(
//                Map.of
//                        ("status", "success",
//                                "message", response.toString()));
//    }

}
