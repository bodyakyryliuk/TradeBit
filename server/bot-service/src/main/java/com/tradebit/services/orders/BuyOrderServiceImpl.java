package com.tradebit.services.orders;

import com.tradebit.exceptions.BuyOrderNotFoundException;
import com.tradebit.models.Bot;
import com.tradebit.models.order.BuyOrder;
import com.tradebit.repositories.BuyOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
        return buyOrderRepository.findFirstByBotOrderByTimestampDesc(bot).orElseThrow(() -> new BuyOrderNotFoundException(bot.getId()));
    }

    @Override
    public void updateBuyOrder(BuyOrder buyOrder) {

    }
}
