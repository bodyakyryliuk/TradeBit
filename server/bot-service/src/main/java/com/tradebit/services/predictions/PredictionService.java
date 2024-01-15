package com.tradebit.services.predictions;

import com.tradebit.dto.PredictionsDTO;
import com.tradebit.models.Prediction;

import java.util.List;

public interface PredictionService {
    void addPredictions(PredictionsDTO predictionsDTO);
    List<Prediction> getPredictionsByTradingPair(String tradingPair);
}
