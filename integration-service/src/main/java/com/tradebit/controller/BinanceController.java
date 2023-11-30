package com.tradebit.controller;

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
    public ResponseEntity<Map<String, String>> createOrder(@RequestBody @Valid BinanceRequestWrapper wrapper){
        BinanceOrderDTO orderDTO = wrapper.getOrderDTO();
        BinanceLinkDTO linkDTO = wrapper.getLinkDTO();
        String response = binanceApiService.makeOrder(orderDTO, linkDTO);
        System.out.println(response);
        return ResponseEntity.ok(
                Map.of
                        ("status", "success",
                                "message", response));
    }

    @PostMapping("/order/test")
    public ResponseEntity<Map<String, String>> createTestOrder(@RequestBody @Valid BinanceRequestWrapper wrapper){
        BinanceOrderDTO orderDTO = wrapper.getOrderDTO();
        BinanceLinkDTO linkDTO = wrapper.getLinkDTO();
        String response = binanceApiService.testNewOrder(orderDTO, linkDTO);
        System.out.println(response);
        return ResponseEntity.ok(
                Map.of
                        ("status", "success",
                                "message", response));
    }

    @GetMapping("/myTrades")
    public ResponseEntity<Map<String, String>> getAllOrders(@RequestBody @Valid BinanceLinkDTO binanceLinkDTO,
                                                            @RequestParam String symbol){
        String response = binanceApiService.getAllOrders(binanceLinkDTO, symbol);

        return ResponseEntity.ok(
                Map.of
                        ("status", "success",
                                "message", response));
    }

}
