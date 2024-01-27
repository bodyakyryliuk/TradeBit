package com.tradebit.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class PredictionsDTO {
    private Map<String, List<PredictionDTO>> predictions;
}
