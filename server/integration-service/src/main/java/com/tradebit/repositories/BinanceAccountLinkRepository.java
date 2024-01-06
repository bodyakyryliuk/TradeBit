package com.tradebit.repositories;

import com.tradebit.models.BinanceAccountLink;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BinanceAccountLinkRepository extends JpaRepository<BinanceAccountLink, Long> {
    boolean existsByUserId(String userId);

    void deleteByUserId(String userId);

    BinanceAccountLink findByApiKeyHash(String apiKeyHash);
}
