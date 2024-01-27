package com.tradebit.configuration;

import com.tradebit.config.SecurityConfig;
import com.tradebit.controllers.BotController;
import com.tradebit.controllers.PredictionsController;
import com.tradebit.services.bots.BotService;
import com.tradebit.services.predictions.PredictionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {BotController.class, PredictionsController.class})
@Import(SecurityConfig.class)
@ExtendWith(SpringExtension.class)
public class SecurityConfigTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BotService botService;
    @MockBean
    private PredictionService predictionService;

    @Test
    public void whenUnauthenticated_thenAccessDeniedForProtectedEndpoint() throws Exception {
        mockMvc.perform(post("/bots"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void whenUnauthenticated_thenAccessGrantedForPublicEndpoint() throws Exception {
        // not found - no prediction in db
        mockMvc.perform(get("/predictions").param("tradingPair", "BTCUSDT"))
                .andExpect(status().isNotFound());
    }
}
