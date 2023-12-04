package com.tradebit.controllers;

import com.tradebit.dto.BotDTO;
import com.tradebit.services.BotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class BotController {
    private final BotService botService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> createBot(@RequestBody @Valid BotDTO botDTO,
                                                         Authentication authentication){
        //TODO: add validation to botDTO
        try {
            String userId = botService.getUserIdFromAuthentication(authentication);
            botService.createBot(botDTO, userId);
            return ResponseEntity.ok(
                    Map.of
                            ("status", "success",
                                    "message", "Bot has been created successfully!"));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(Map.of("status", "failure",
                                        "message", e.getMessage()));
        }
    }

    @PostMapping("/toggleBot")
    public ResponseEntity<Map<String, String>> toggleBot(@RequestParam @Valid Long botId,
                                                         Authentication authentication){
        try {
            String userId = botService.getUserIdFromAuthentication(authentication);
            botService.toggleBot(botId, userId);
            return ResponseEntity.ok(
                    Map.of("status", "success",
                            "message", "Bot state has been toggled successfully!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "failure",
                            "message", e.getMessage()));
        }

    }
}
