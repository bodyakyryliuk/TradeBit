package com.tradebit.repositories;

import com.tradebit.models.BinanceAccountLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BinanceAccountLinkRepository extends JpaRepository<BinanceAccountLink, Long> {
    boolean existsByUserId(String userId);

    void deleteByUserId(String userId);

    BinanceAccountLink findByApiKeyHash(String apiKeyHash);

    Optional<BinanceAccountLink> findByUserId(String userId);
}
