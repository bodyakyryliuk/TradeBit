package com.tradebit.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Entity
@Table(name = "predictions")
public class Prediction {
    @Id
    private String id;
    @Column(name = "trading_pair")
    private String tradingPair;
    @Column(name = "predicted_price")
    private Double predictedPrice;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date timestamp;

    public Prediction() {
    }

    @PrePersist
    @PostLoad
    private void generateId() {
        if (this.tradingPair != null && this.timestamp != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            this.id = this.tradingPair + "_" + sdf.format(this.timestamp);
        }
    }
}