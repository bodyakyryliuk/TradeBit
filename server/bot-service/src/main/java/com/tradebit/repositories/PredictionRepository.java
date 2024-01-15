package com.tradebit.repositories;

import com.tradebit.models.Prediction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PredictionRepository extends JpaRepository<Prediction, String> {
    List<Prediction> findByTradingPair(String tradingPair);
}
