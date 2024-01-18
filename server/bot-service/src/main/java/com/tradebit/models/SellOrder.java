package com.tradebit.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sell_orders")
public class SellOrder {
    @Column
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "buy_order_id", referencedColumnName = "id")
    private BuyOrder buyOrder;
    @Column(name = "trading_pair")
    private String tradingPair;
    @Column(name = "sell_price")
    private Double sellPrice;
    @Column
    private Double quantity;
    @Column
    private LocalDateTime timestamp;
}
