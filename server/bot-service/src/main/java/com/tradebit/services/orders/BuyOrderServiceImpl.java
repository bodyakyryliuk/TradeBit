package com.tradebit.services.orders;

import com.tradebit.exceptions.BuyOrderNotFoundException;
import com.tradebit.exceptions.SellOrderNotFoundException;
import com.tradebit.models.Bot;
import com.tradebit.models.order.BuyOrder;
import com.tradebit.models.order.SellOrder;
import com.tradebit.repositories.BuyOrderRepository;
import com.tradebit.services.bots.BotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BuyOrderServiceImpl implements BuyOrderService{
    private final BuyOrderRepository buyOrderRepository;

    @Override
    public BuyOrder save(Bot bot, double buyPrice) {
        BuyOrder buyOrder = BuyOrder.builder()
                .bot(bot)
                .buyPrice(buyPrice)
                .tradingPair(bot.getTradingPair())
                .quantity(bot.getTradeSize())
                .timestamp(LocalDateTime.now())
                .build();

        return buyOrderRepository.save(buyOrder);
    }

    @Override
    public BuyOrder getRecentBuyOrderByBot(Bot bot) {
        return buyOrderRepository.findFirstByBotOrderByTimestampDesc(bot).orElseThrow(() ->
                new BuyOrderNotFoundException("Buy order not found by bot with id: " + bot.getId()));
    }

    @Override
    public List<BuyOrder> getBuyOrdersByBotId(Long botId) {
        List<BuyOrder> buyOrders = buyOrderRepository.findAllByBotId(botId);
        if (buyOrders.isEmpty())
            throw new BuyOrderNotFoundException("No buy order found by bot with id: " + botId);

        return buyOrders;
    }

    @Override
    public BuyOrder getBuyOrderById(Long orderId) {
        Optional<BuyOrder> buyOrder = buyOrderRepository.findById(orderId);
        if (buyOrder.isEmpty()){
            throw new BuyOrderNotFoundException("Buy order not found with id: " + orderId);
        }
        return buyOrder.get();
    }
}
