package com.tradebit.repositories;

import com.tradebit.models.order.SellOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellOrderRepository extends JpaRepository<SellOrder, Long> {
}
