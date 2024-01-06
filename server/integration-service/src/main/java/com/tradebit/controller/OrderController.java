package com.tradebit.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.tradebit.dto.BinanceLinkDTO;
import com.tradebit.dto.BinanceOrderDTO;
import com.tradebit.dto.BinanceRequestWrapper;
import com.tradebit.services.BinanceOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {
    private final BinanceOrderService binanceOrderService;

    @PostMapping("/order")
    public ResponseEntity<JsonNode> createOrder(@RequestBody @Valid BinanceRequestWrapper wrapper){
        BinanceOrderDTO orderDTO = wrapper.getOrderDTO();
        BinanceLinkDTO linkDTO = wrapper.getLinkDTO();
        JsonNode response = binanceOrderService.makeOrder(orderDTO, linkDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/order/test")
    public ResponseEntity<JsonNode> createTestOrder(@RequestBody @Valid BinanceRequestWrapper wrapper){
        BinanceOrderDTO orderDTO = wrapper.getOrderDTO();
        BinanceLinkDTO linkDTO = wrapper.getLinkDTO();
        JsonNode response = binanceOrderService.testNewOrder(orderDTO, linkDTO);
        return ResponseEntity.ok(response);
    }
}
