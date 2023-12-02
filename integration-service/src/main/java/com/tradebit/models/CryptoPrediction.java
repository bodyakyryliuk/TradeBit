package com.tradebit.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Entity
@Table(name = "crypto_prediction")
public class CryptoPrediction {
    @Id
    private String id;
    @Column(name = "trading_pair")
    private String tradingPair;
    @Column(name = "predicted_price")
    private Double predictedPrice;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date timestamp;

    public CryptoPrediction() {
    }

    @PrePersist
    @PostLoad
    private void generateId() {
        if (this.tradingPair != null && this.timestamp != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            this.id = this.tradingPair + "_" + sdf.format(this.timestamp);
        }
    }
}
