package com.tradebit.repositories;

import com.tradebit.models.CryptoPrediction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CryptoPredictionRepository extends JpaRepository<CryptoPrediction, String> {
    List<CryptoPrediction> getCryptoPredictionsByTradingPair(String tradingPair);
}
