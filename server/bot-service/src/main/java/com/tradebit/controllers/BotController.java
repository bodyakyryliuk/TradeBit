package com.tradebit.controllers;

import com.tradebit.dto.BotDTO;
import com.tradebit.exceptions.BotNotFoundException;
import com.tradebit.models.Bot;
import com.tradebit.services.bots.BotService;
import jakarta.validation.Valid;
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
    @PostMapping
    public ResponseEntity<Bot> createBot(@Valid @RequestBody BotDTO botDTO,
                                                         Authentication authentication){
        Bot bot = botService.createBot(botDTO, authentication);
        return ResponseEntity.ok(bot);
    }

    @PostMapping("/toggleBot")
    public ResponseEntity<Boolean> toggleBot(@RequestParam Long botId,
                                                         Authentication authentication){
        boolean botEnabled = botService.toggleBot(botId, authentication);
        return ResponseEntity.ok(botEnabled);
    }

    @GetMapping
    public ResponseEntity<List<Bot>> getBotsByUser(Authentication authentication){
        List<Bot> bots = botService.getAllByUserId(authentication);

        return ResponseEntity.ok(bots);
    }

    @GetMapping("/bot")
    public ResponseEntity<Bot> getBotById(@RequestParam Long botId){
        return ResponseEntity.ok(botService.getBot(botId));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteBotById(@RequestParam Long botId){
        botService.deleteBotById(botId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Bot> updateBot(@RequestParam Long botId, @Valid @RequestBody BotDTO botDTO){
        Bot bot = botService.updateBot(botId, botDTO);
        return ResponseEntity.ok(bot);
    }
}