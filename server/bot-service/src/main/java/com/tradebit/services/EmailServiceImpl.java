package com.tradebit.services;

import com.tradebit.RabbitMQMessageProducer;
import com.tradebit.emailrequests.EmailRequest;
import com.tradebit.models.EmailType;
import com.tradebit.models.order.BuyOrder;
import com.tradebit.models.order.SellOrder;
import com.tradebit.services.identity.IdentityServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService{
    private final RabbitMQMessageProducer messageProducer;
    private final IdentityServiceClient identityServiceClient;

    @Override
    public void sendBuyOrderEmail(BuyOrder buyOrder, String userId) {
        generateBuyOrderEmailRequest(buyOrder, userId)
                .subscribe(buyOrderEmailRequest ->
                        messageProducer.publish(buyOrderEmailRequest,
                                "internal.exchange",
                                "internal.email.routing-key")
                );
    }

    @Override
    public void sendSellOrderEmail(SellOrder sellOrder, String userId) {
        generateSellOrderEmailRequest(sellOrder, userId)
                .subscribe(sellOrderEmailRequest ->
                        messageProducer.publish(sellOrderEmailRequest,
                                "internal.exchange",
                                "internal.email.routing-key")
                );
    }

    public Mono<EmailRequest> generateBuyOrderEmailRequest(BuyOrder buyOrder, String userId){
        return identityServiceClient.getUserEmail(userId)
                .map(email -> EmailRequest.builder()
                        .to(email)
                        .emailType(EmailType.BUY_ORDER)
                        .botId(buyOrder.getBot().getId())
                        .botName(buyOrder.getBot().getName())
                        .tradingPair(buyOrder.getTradingPair())
                        .buyPrice(buyOrder.getBuyPrice())
                        .quantity(buyOrder.getQuantity())
                        .timestamp(buyOrder.getTimestamp().toString())
                        .build());
    }


    public Mono<EmailRequest> generateSellOrderEmailRequest(SellOrder sellOrder, String userId){
        return identityServiceClient.getUserEmail(userId)
                .map(email -> EmailRequest.builder()
                        .to(email)
                        .emailType(EmailType.SELL_ORDER)
                        .botId(sellOrder.getBot().getId())
                        .botName(sellOrder.getBot().getName())
                        .tradingPair(sellOrder.getTradingPair())
                        .buyPrice(sellOrder.getSellPrice())
                        .quantity(sellOrder.getQuantity())
                        .profit(sellOrder.getProfit())
                        .timestamp(sellOrder.getTimestamp().toString())
                        .build());
    }

}
