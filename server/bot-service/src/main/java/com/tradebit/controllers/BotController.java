package com.tradebit.controllers;

import com.tradebit.dto.BotDTO;
import com.tradebit.exceptions.BotNotFoundException;
import com.tradebit.models.Bot;
import com.tradebit.services.bots.BotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bots")
public class BotController {
    private final BotService botService;
// todo: rename endpoint without create
    @PostMapping("/create")
    public ResponseEntity<Bot> createBot(@RequestBody BotDTO botDTO,
                                                         Authentication authentication){
        //TODO: add validation to botDTO
        String userId = botService.getUserIdFromAuthentication(authentication);
        Bot bot = botService.createBot(botDTO, userId);
        return ResponseEntity.ok(bot);
    }

    @PostMapping("/toggleBot")
    public ResponseEntity<Boolean> toggleBot(@RequestParam Long botId,
                                                         Authentication authentication){
        String userId = botService.getUserIdFromAuthentication(authentication);
        boolean botEnabled = botService.toggleBot(botId, userId);
        return ResponseEntity.ok(botEnabled);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Bot>> getBotsByUserId(@PathVariable String userId){
        return ResponseEntity.ok(botService.getAllByUserId(userId));
    }

    @GetMapping("/bot/{botId}")
    public ResponseEntity<Bot> getBotById(@PathVariable Long botId){
        return  ResponseEntity.ok(botService.getBot(botId));
    }

    @DeleteMapping("/{botId}")
    public ResponseEntity<Map<String, String>> deleteBotById(@PathVariable Long botId){
        botService.deleteBotById(botId);
        return ResponseEntity.ok(
                Map.of("status", "success",
                        "message", "Bot with id: " + botId + " has been successfully deleted"));
    }

    @PutMapping("/{botId}")
    public ResponseEntity<Bot> updateBot(@PathVariable Long botId, @RequestBody BotDTO botDTO){
        Bot bot = botService.updateBot(botId, botDTO);
        return ResponseEntity.ok(bot);
    }
}