package com.tradebit.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tradebit.config.SecurityConfig;
import com.tradebit.services.BinanceApiService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BinanceController.class)
@Import(SecurityConfig.class)
public class BinanceControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BinanceApiService binanceApiService;

    @Test
    public void getPriceChange_ReturnsPriceChange() throws Exception {
        String tradingPair = "BTCUSDT";
        when(binanceApiService.getPriceChange(tradingPair, 24)).thenReturn(100.0);

        mockMvc.perform(get("/binance/priceChange/" + tradingPair))
                .andExpect(status().isOk())
                .andExpect(content().string("100.0"));
    }

    @Test
    public void getAveragePriceByPeriod_ReturnsAveragePrice() throws Exception {
        String tradingPair = "BTCUSDT";

        when(binanceApiService.getAveragePriceByPeriod(tradingPair, 24)).thenReturn(40000.0);

        mockMvc.perform(get("/binance/averagePriceByPeriod/" + tradingPair))
                .andExpect(status().isOk())
                .andExpect(content().string("40000.0"));
    }

    @Test
    public void getCurrentPrice_ReturnsCurrentPrice() throws Exception {
        String tradingPair = "BTCUSDT";
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode price = objectMapper.createObjectNode();
        price.put("symbol", "BTCUSDT");
        price.put("price", "41000");

        when(binanceApiService.getCurrentPriceForCrypto(tradingPair)).thenReturn(price);

        mockMvc.perform(get("/binance/currentPrice?tradingPair=" + tradingPair))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.symbol").value("BTCUSDT"))
                .andExpect(jsonPath("$.price").value("41000"));
    }
}
