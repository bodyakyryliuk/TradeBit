package com.tradebit.repositories;

import com.tradebit.models.Bot;
import com.tradebit.models.order.BuyOrder;
import com.tradebit.models.order.SellOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SellOrderRepository extends JpaRepository<SellOrder, Long> {
    boolean existsByBot(Bot bot);
    List<SellOrder> findAllByBotId(Long botId);
}
