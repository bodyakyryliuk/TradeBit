package com.tradebit.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.tradebit.services.BinanceApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/binance")
@RequiredArgsConstructor
public class BinanceController {
    private final BinanceApiService binanceApiService;

    @GetMapping("/priceChange/{tradingPair}")
    public ResponseEntity<Double> getPriceChange(
            @PathVariable String tradingPair,
            //period in hours
            @RequestParam (name = "period", required = false, defaultValue = "24") int period){
        Double priceChange = binanceApiService.getPriceChange(tradingPair, period);
        return ResponseEntity.ok(priceChange);
    }

    @GetMapping("/historicalPrices/{tradingPair}")
    public ResponseEntity<JsonNode> getHistoricalPrices(
            @PathVariable String tradingPair,
            //period in hours
            @RequestParam (name = "period", required = false, defaultValue = "24") int period){
        JsonNode response = binanceApiService.getHistoricalPricesForPeriod(tradingPair, period);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cryptocurrencies")
    public ResponseEntity<JsonNode> getAllCryptocurrencies(){
        JsonNode response = binanceApiService.getAllCryptocurrenciesWithUSDT();
        return ResponseEntity.ok(response);
    }

}
