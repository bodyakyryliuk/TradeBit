package com.tradebit.models.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tradebit.models.Bot;
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
@Table(name = "buy_orders")
public class BuyOrder {
    @Column
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bot_id", referencedColumnName = "id")
    private Bot bot;

    @Column(name = "bot_id", insertable = false, updatable = false)
    private Long botId;

    @Column(name = "trading_pair")
    private String tradingPair;

    @Column(name = "buy_price")
    private Double buyPrice;

    @Column
    private Double quantity;

    @Column
    private LocalDateTime timestamp;
}
