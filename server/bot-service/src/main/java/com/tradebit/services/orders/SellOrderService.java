package com.tradebit.services.orders;

import com.tradebit.models.Bot;
import com.tradebit.models.order.BuyOrder;
import com.tradebit.models.order.SellOrder;

import java.util.List;
import java.util.Optional;

public interface SellOrderService {
    SellOrder save(Bot bot, BuyOrder buyOrder, double sellPrice);
    Optional<SellOrder> getSellOrderByBotAndSold(Bot bot, boolean sold);
    List<SellOrder> getSellOrdersByBotId(Long botId);
}
