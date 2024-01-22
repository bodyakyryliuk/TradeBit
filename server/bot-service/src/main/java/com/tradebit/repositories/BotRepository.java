package com.tradebit.repositories;

import com.tradebit.models.Bot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BotRepository extends JpaRepository<Bot, Long> {
    Optional<Bot> findByIdAndUserId(Long botId, String userId);
    Optional<Bot> findById(Long botId);
    int countAllByUserId(String userId);
    int countAllByUserIdAndEnabled(String userId, boolean enabled);
    boolean existsByNameAndUserId(String name, String userId);
    void deleteById(Long id);
    List<Bot> findAllByUserId(String userId);
}
