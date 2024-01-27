package com.tradebit.repositories;

import com.tradebit.models.Bot;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BotRepository extends JpaRepository<Bot, Long> {
    Optional<Bot> findByIdAndUserId(Long botId, String userId);
    Optional<Bot> findById(Long botId);
    int countAllByUserIdAndHidden(String userId, boolean hidden);
    int countAllByUserIdAndEnabled(String userId, boolean enabled);
    boolean existsByNameAndUserIdAndHidden(String name, String userId, boolean hidden);
    void deleteById(Long id);
    List<Bot> findAllByUserId(String userId);
}
