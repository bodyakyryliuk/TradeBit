package com.tradebit.models.order;

import com.tradebit.models.Bot;
import com.tradebit.models.order.BuyOrder;
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

    @OneToOne
    @JoinColumn(name = "buy_order_id", referencedColumnName = "id")
    private BuyOrder buyOrder;

    @ManyToOne
    @JoinColumn(name = "bot_id", referencedColumnName = "id")
    private Bot bot;

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
