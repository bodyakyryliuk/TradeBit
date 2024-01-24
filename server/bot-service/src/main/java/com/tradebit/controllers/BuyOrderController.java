package com.tradebit.controllers;

import com.tradebit.models.order.BuyOrder;
import com.tradebit.services.orders.BuyOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/buyOrders")
@RequiredArgsConstructor
public class BuyOrderController {
    private final BuyOrderService buyOrderService;

    @GetMapping
    public ResponseEntity<List<BuyOrder>> getBuyOrdersByBotId(@RequestParam Long botId){
        return ResponseEntity.ok(buyOrderService.getBuyOrdersByBotId(botId));
    }
}
