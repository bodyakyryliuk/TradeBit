package com.tradebit.services;

import com.tradebit.dto.PredictionsDTO;
import com.tradebit.models.Prediction;
import com.tradebit.repositories.PredictionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PredictionServiceImpl implements PredictionService{
    private final PredictionRepository predictionRepository;
    @Override
    public void addPredictions(PredictionsDTO predictionsDTO) {
        List<Prediction> predictions = new ArrayList<>();
        predictionsDTO.getPredictions().forEach((tradingPair, dataList) -> {
            dataList.forEach(data -> {
                Prediction prediction = new Prediction();
                prediction.setTradingPair(tradingPair);
                prediction.setPredictedPrice(data.getPredictedPrice());
                prediction.setTimestamp(data.getTimestamp());
                predictions.add(prediction);
            });
        });

        predictionRepository.saveAll(predictions);
    }

    @Override
    public List<Prediction> getPredictionsByTradingPair(String tradingPair) {
        return predictionRepository.findByTradingPair(tradingPair);
    }
}
