package com.tradebit.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "binance_account_linking")
public class BinanceAccountLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(name = "api_key")
    private String apiKey;

    @Column(name = "secret_key")
    private String secret_key;

    @Column(name = "user_id", unique = true)
    private String userId;

    @Column(name = "api_key_hash")
    private String apiKeyHash;

}




