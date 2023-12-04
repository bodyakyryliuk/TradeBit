package com.tradebit.controller;

import com.tradebit.models.CryptoPrediction;
import com.tradebit.repositories.CryptoPredictionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/prediction")
@RequiredArgsConstructor
public class PredictionController {
    private final CryptoPredictionRepository cryptoPredictionRepository;

    @PostMapping("/add")
    public ResponseEntity<Map<String, String>> addPrediction(@RequestBody List<CryptoPrediction> cryptoPredictions){
        try {
            cryptoPredictionRepository.deleteAll();
            cryptoPredictionRepository.saveAll(cryptoPredictions);
            return ResponseEntity.ok(Map.of("status", "success", "message", "Predicted data published successfully"));
        }catch (Exception e){
            return new ResponseEntity<>(
                    Map.of("status", "failure", "message", e.getMessage()),
                    HttpStatus.BAD_REQUEST);        }
    }

    @GetMapping("/get")
    public ResponseEntity<?> getPrediction(@RequestParam String tradingPair){
        try {
            return ResponseEntity.ok(cryptoPredictionRepository.getCryptoPredictionsByTradingPair(tradingPair));
        }catch (Exception e){
            return new ResponseEntity<>(
                    Map.of("status", "failure", "message", e.getMessage()),
                    HttpStatus.BAD_REQUEST);        }
    }

}