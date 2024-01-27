package com.tradebit.repositories;

import com.tradebit.models.Bot;
import com.tradebit.models.order.BuyOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BuyOrderRepository extends JpaRepository<BuyOrder, Long> {
    Optional<BuyOrder> findFirstByBotOrderByTimestampDesc(Bot bot);
    boolean existsByBot(Bot bot);
    List<BuyOrder> findAllByBotId(Long botId);
    Optional<BuyOrder> findById(Long orderId);
}
