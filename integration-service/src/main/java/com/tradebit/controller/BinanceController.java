package com.tradebit.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tradebit.dto.BinanceLinkDTO;
import com.tradebit.dto.BinanceOrderDTO;
import com.tradebit.dto.BinanceRequestWrapper;
import com.tradebit.models.wallet.WalletInfo;
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

    //TODO: add get mapping for getting account secret and api keys ?

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

    @GetMapping("/wallet")
    public ResponseEntity<WalletInfo> getWallet(@RequestBody @Valid BinanceLinkDTO binanceLinkDTO){
        WalletInfo response = binanceApiService.getWallet(binanceLinkDTO);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/totalBalance")
    public ResponseEntity<JsonNode> getTotalBalance(@RequestBody @Valid BinanceLinkDTO binanceLinkDTO){
        double totalBalance = binanceApiService.getTotalBalance(binanceLinkDTO);
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode responseNode = mapper.createObjectNode();
        responseNode.put("balance", totalBalance);
        return ResponseEntity.ok(responseNode);
    }

    //TODO: add get mapping for getting price change for some period
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
        JsonNode response = binanceApiService.getHistoricalPrices(tradingPair, period);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cryptocurrencies")
    public ResponseEntity<JsonNode> getAllCryptocurrencies(){
        JsonNode response = binanceApiService.getAllCryptocurrenciesWithUSDT();
        return ResponseEntity.ok(response);
    }


}
