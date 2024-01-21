package com.tradebit.services.orders;

import com.tradebit.models.Bot;
import com.tradebit.models.order.BuyOrder;

import java.util.Optional;

public interface BuyOrderService {
    BuyOrder save(Bot bot, double buyPrice);
    BuyOrder getRecentBuyOrderByBot(Bot bot);
    void updateBuyOrder(BuyOrder buyOrder);
}