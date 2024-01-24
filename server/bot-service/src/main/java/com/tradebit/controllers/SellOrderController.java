package com.tradebit.controllers;

import com.tradebit.models.order.BuyOrder;
import com.tradebit.models.order.SellOrder;
import com.tradebit.services.orders.SellOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sellOrders")
@RequiredArgsConstructor
public class SellOrderController {
    private final SellOrderService sellOrderService;

    @GetMapping
    public ResponseEntity<List<SellOrder>> getBuyOrdersByBotId(@RequestParam Long botId){
        return ResponseEntity.ok(sellOrderService.getSellOrdersByBotId(botId));
    }
}
