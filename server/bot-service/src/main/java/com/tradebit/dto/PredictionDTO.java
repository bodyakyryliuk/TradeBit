package com.tradebit.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.util.Date;

@Data
@AllArgsConstructor
public class PredictionDTO {
    private Double predictedPrice;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date timestamp;
}