package com.tradebit.services;

import com.tradebit.models.order.BuyOrder;
import com.tradebit.models.order.SellOrder;

public interface EmailService {
    void sendBuyOrderEmail(BuyOrder buyOrder, String userId);
    void sendSellOrderEmail(SellOrder sellOrder, String userId);
}
