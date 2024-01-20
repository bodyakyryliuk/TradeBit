package com.tradebit.services.orders;

import com.tradebit.models.Bot;
import com.tradebit.models.order.BuyOrder;
import com.tradebit.models.order.SellOrder;
import com.tradebit.repositories.SellOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SellOrderServiceImpl implements SellOrderService{
    private final SellOrderRepository sellOrderRepository;
    @Override
    public SellOrder save(Bot bot, BuyOrder buyOrder, double sellPrice) {
        SellOrder sellOrder = SellOrder.builder()
                .bot(bot)
                .buyOrder(buyOrder)
                .tradingPair(bot.getTradingPair())
                .quantity(bot.getTradeSize())
                .timestamp(LocalDateTime.now())
                .sellPrice(sellPrice)
                .build();

        return sellOrderRepository.save(sellOrder);
    }

    @Override
    public Optional<SellOrder> getSellOrderByBotAndSold(Bot bot, boolean sold) {
        return Optional.empty();
    }

    @Override
    public void updateSellOrder(SellOrder sellOrder) {

    }
}
