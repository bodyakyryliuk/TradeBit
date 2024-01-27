package com.tradebit.repository;

import com.tradebit.models.Prediction;
import com.tradebit.repositories.PredictionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@DataJpaTest
public class PredictionRepositoryTests {
    @Autowired
    private PredictionRepository predictionRepository;
    private String tradingPair;

    @BeforeEach
    public void setup(){
        tradingPair = "BTCUSDT";

        Prediction prediction1 = createTestPrediction(2, tradingPair);
        Prediction prediction2 = createTestPrediction(3, tradingPair);
        Prediction prediction3 = createTestPrediction(4, tradingPair);

        predictionRepository.save(prediction1);
        predictionRepository.save(prediction2);
        predictionRepository.save(prediction3);
    }

    private Prediction createTestPrediction(int days, String tradingPair){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, days);
        Date nextDay = calendar.getTime();

        Prediction prediction = new Prediction();
        prediction.setTradingPair(tradingPair);
        prediction.setPredictedPrice(42000.0);
        prediction.setTimestamp(nextDay);

        return prediction;
    }

    private Prediction createTestPrediction(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        Date nextDay = calendar.getTime();

        Prediction prediction = new Prediction();
        prediction.setTradingPair("BTCUSDT");
        prediction.setPredictedPrice(42000.0);
        prediction.setTimestamp(nextDay);

        return prediction;
    }

    @Test
    public void givenPredictionObject_whenSave_thenReturnSavedPrediction() {
        Prediction prediction = createTestPrediction();

        Prediction savedPrediction = predictionRepository.save(prediction);

        Assertions.assertNotNull(savedPrediction);
    }

    @Test
    public void givenPredictionObject_whenSave_thenReturnGeneratedId(){
        Prediction prediction = createTestPrediction();

        Prediction savedPrediction = predictionRepository.save(prediction);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String expectedId = prediction.getTradingPair() + "_" + sdf.format(prediction.getTimestamp());

        Assertions.assertNotNull(savedPrediction.getId());
        Assertions.assertEquals(expectedId, savedPrediction.getId());
    }

    @Test
    public void givenTradingPair_whenFindByTradingPair_thenReturnPredictions(){
        List<Prediction> predictions = predictionRepository.findByTradingPair(tradingPair);

        Assertions.assertEquals(3, predictions.size());
    }

    @Test
    public void givenDuplicatePrediction_whenSave_thenThrowException() {
        Prediction prediction = createTestPrediction();
        predictionRepository.save(prediction);

        Prediction duplicatePrediction = createTestPrediction();

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            predictionRepository.saveAndFlush(duplicatePrediction);
        });
    }
}





