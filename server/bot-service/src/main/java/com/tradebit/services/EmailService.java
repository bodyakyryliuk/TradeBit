package com.tradebit.services;

import com.tradebit.emailrequests.BuyOrderEmailRequest;
import com.tradebit.emailrequests.SellOrderEmailRequest;
import com.tradebit.models.EmailType;
import com.tradebit.models.order.BuyOrder;
import com.tradebit.models.order.SellOrder;

public interface EmailService {
    void sendBuyOrderEmail(BuyOrder buyOrder, String to);
    void sendSellOrderEmail(SellOrder sellOrder, String to);
}
