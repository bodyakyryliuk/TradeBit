package com.tradebit.dto;

import com.tradebit.models.Prediction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PredictionsDTO {
    private Map<String, List<PredictionDTO>> predictions;
}
