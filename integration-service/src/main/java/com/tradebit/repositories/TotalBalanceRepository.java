package com.tradebit.repositories;

import com.tradebit.models.TotalBalance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TotalBalanceRepository extends JpaRepository<TotalBalance, String> {
    List<TotalBalance> findAllByUserId(String userId);
}
