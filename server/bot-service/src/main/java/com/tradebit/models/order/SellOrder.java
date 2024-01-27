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
@Table(name = "sell_orders")
public class SellOrder {
    @Column
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buy_order_id", referencedColumnName = "id")
    private BuyOrder buyOrder;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bot_id", referencedColumnName = "id")
    private Bot bot;

    @Column(name = "buy_order_id", insertable = false, updatable = false)
    private Long buyOrderId;

    @Column(name = "bot_id", insertable = false, updatable = false)
    private Long botId;

    @Column(name = "trading_pair")
    private String tradingPair;

    @Column(name = "sell_price")
    private Double sellPrice;

    @Column
    private Double quantity;

    @Column
    private Double profit;

    @Column
    private LocalDateTime timestamp;
}
