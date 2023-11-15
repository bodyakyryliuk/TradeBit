package com.tradebit.controller;

import com.tradebit.dto.BinanceLinkDTO;
import com.tradebit.services.BinanceLinkService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/binance")
@RequiredArgsConstructor
public class BinanceLinkController {
    private final BinanceLinkService binanceLinkService;

    @PostMapping("/link")
    public ResponseEntity<Map<String, String>> linkAccount(@RequestBody @Valid BinanceLinkDTO binanceLinkDTO,
                                                           Authentication authentication){
        try {
            String userId = binanceLinkService.getUserIdFromAuthentication(authentication);
            binanceLinkService.linkAccount(binanceLinkDTO, userId);

            return ResponseEntity.ok(
                    Map.of
                            ("status", "success",
                        "message", "Binance wallet has been linked successfully!"));
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    Map.of(
                    "status", "failure",
                    "message", ex.getMessage()));
        }

    }

}
