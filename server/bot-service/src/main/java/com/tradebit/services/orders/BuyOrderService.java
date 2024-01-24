package com.tradebit.services.orders;

import com.tradebit.models.Bot;
import com.tradebit.models.order.BuyOrder;

import java.util.List;

public interface BuyOrderService {
    BuyOrder save(Bot bot, double buyPrice);
    BuyOrder getRecentBuyOrderByBot(Bot bot);
    void updateBuyOrder(BuyOrder buyOrder);
    List<BuyOrder> getBuyOrdersByBotId(Long botId);
}
