package com.tradebit.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bot")
public class Bot {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "buy_threshold")
    private Double buyThreshold;
    @Column(name = "sell_threshold")
    private Double sellThreshold;
    @Column(name = "take_profit_percentage")
    private Double takeProfitPercentage;
    @Column(name = "stop_loss_percentage")
    private Double stopLossPercentage;
    @Column(name = "trade_size")
    private Double tradeSize;
    @Column(name = "trading_pair")
    private String tradingPair;
    @Column(name = "user_id")
    private String userId;
    @Column
    private volatile Boolean enabled;
}







