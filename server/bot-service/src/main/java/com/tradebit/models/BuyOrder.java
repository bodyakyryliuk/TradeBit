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
@Table(name = "buy_orders")
public class BuyOrder {
    @Column
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "bot_id", referencedColumnName = "id")
    private Bot bot;
    @Column(name = "trading_pair")
    private String tradingPair;
    @Column(name = "buy_price")
    private Double buyPrice;
    @Column
    private Double quantity;
    @Column
    private String timestamp;
    @Column
    private Boolean sold;
}
