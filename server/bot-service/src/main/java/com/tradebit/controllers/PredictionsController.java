package com.tradebit.controllers;

import com.tradebit.dto.PredictionsDTO;
import com.tradebit.models.Prediction;
import com.tradebit.services.predictions.PredictionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/prediction")
@RequiredArgsConstructor
public class PredictionsController {
    private final PredictionService predictionService;

    @PostMapping("/add")
    public ResponseEntity<Map<String, String>> addPredictions(@RequestBody PredictionsDTO predictionsDTO){
        try {
            System.out.println(predictionsDTO);
            predictionService.addPredictions(predictionsDTO);
            return ResponseEntity.ok(Map.of("status", "success", "message", "Predicted data have been published successfully"));
        }catch (Exception e){
            return new ResponseEntity<>(
                    Map.of("status", "failure", "message", e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get")
    public ResponseEntity<?> getPrediction(@RequestParam String tradingPair){
        List<Prediction> predictions = predictionService.getPredictionsByTradingPair(tradingPair);
        if (predictions.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(predictions);
    }

}
