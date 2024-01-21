package com.tradebit.services;

import com.tradebit.RabbitMQMessageProducer;
import com.tradebit.emailrequests.BuyOrderEmailRequest;
import com.tradebit.emailrequests.OrderEmailRequest;
import com.tradebit.emailrequests.SellOrderEmailRequest;
import com.tradebit.models.EmailType;
import com.tradebit.models.order.BuyOrder;
import com.tradebit.models.order.SellOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService{
    private final RabbitMQMessageProducer messageProducer;

    @Override
    public void sendBuyOrderEmail(BuyOrder buyOrder, String to) {
        messageProducer.publish(generateBuyOrderEmailRequest(buyOrder, to),
                "internal.exchange",
                "internal.email.routing-key");
    }

    @Override
    public void sendSellOrderEmail(SellOrder sellOrder, String to) {
        messageProducer.publish(generateSellOrderEmailRequest(sellOrder, to),
                "internal.exchange",
                "internal.email.routing-key");
    }

    private BuyOrderEmailRequest generateBuyOrderEmailRequest(BuyOrder buyOrder, String to){
        return BuyOrderEmailRequest.builder()
                .to(to)
                .emailType(EmailType.BUY_ORDER)
                .botId(buyOrder.getBot().getId())
                .botName(buyOrder.getBot().getName())
                .tradingPair(buyOrder.getTradingPair())
                .buyPrice(buyOrder.getBuyPrice())
                .quantity(buyOrder.getQuantity())
                .timestamp(buyOrder.getTimestamp())
                .build();
    }

    private SellOrderEmailRequest generateSellOrderEmailRequest(SellOrder sellOrder, String to){
        return SellOrderEmailRequest.builder()
                .to(to)
                .emailType(EmailType.SELL_ORDER)
                .botId(sellOrder.getBot().getId())
                .botName(sellOrder.getBot().getName())
                .tradingPair(sellOrder.getTradingPair())
                .buyPrice(sellOrder.getSellPrice())
                .quantity(sellOrder.getQuantity())
                .profit(sellOrder.getProfit())
                .timestamp(sellOrder.getTimestamp())
                .build();
    }
}
