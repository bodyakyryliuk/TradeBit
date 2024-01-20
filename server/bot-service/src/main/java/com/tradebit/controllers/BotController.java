package com.tradebit.controllers;

import com.tradebit.dto.BotDTO;
import com.tradebit.services.bots.BotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bot")
public class BotController {
    private final BotService botService;
// todo: rename endpoint without create
    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> createBot(@RequestBody BotDTO botDTO,
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
    public ResponseEntity<Map<String, ?>> toggleBot(@RequestParam Long botId,
                                                         Authentication authentication){
        try {
            String userId = botService.getUserIdFromAuthentication(authentication);
            boolean botEnabled = botService.toggleBot(botId, userId);
            return ResponseEntity.ok(
                    Map.of("status", "success",
                            "enabled", botEnabled));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "failure",
                            "message", e.getMessage()));
        }

    }
}