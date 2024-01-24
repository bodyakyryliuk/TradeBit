package com.tradebit.services.orders;

import com.tradebit.exceptions.BuyOrderNotFoundException;
import com.tradebit.models.Bot;
import com.tradebit.models.order.BuyOrder;
import com.tradebit.models.order.SellOrder;
import com.tradebit.repositories.SellOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SellOrderServiceImpl implements SellOrderService{
    private final SellOrderRepository sellOrderRepository;
    @Override
    public SellOrder save(Bot bot, BuyOrder buyOrder, double sellPrice) {
        double quantity = bot.getTradeSize();

        SellOrder sellOrder = SellOrder.builder()
                .bot(bot)
                .buyOrder(buyOrder)
                .tradingPair(bot.getTradingPair())
                .quantity(quantity)
                .timestamp(LocalDateTime.now())
                .sellPrice(sellPrice)
                .profit(getProfit(buyOrder, quantity, sellPrice))
                .build();

        return sellOrderRepository.save(sellOrder);
    }

    private double getProfit(BuyOrder buyOrder, double quantity, double sellPrice){
        Double buyOrderAmount = buyOrder.getBuyPrice() * buyOrder.getQuantity();
        Double sellOrderAmount = sellPrice * quantity;
        return sellOrderAmount - buyOrderAmount;
    }

    @Override
    public Optional<SellOrder> getSellOrderByBotAndSold(Bot bot, boolean sold) {
        return Optional.empty();
    }

    @Override
    public List<SellOrder> getSellOrdersByBotId(Long botId) {
        List<SellOrder> sellOrders = sellOrderRepository.findAllByBotId(botId);
        if (sellOrders.isEmpty())
            throw new BuyOrderNotFoundException(botId);

        return sellOrders;
    }
}
