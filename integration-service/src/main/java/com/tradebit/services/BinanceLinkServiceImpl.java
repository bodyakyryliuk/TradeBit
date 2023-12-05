package com.tradebit.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.tradebit.dto.BinanceLinkDTO;
import com.tradebit.encryption.EncryptionUtil;
import com.tradebit.exceptions.BinanceLinkException;
import com.tradebit.models.BinanceAccountLink;
import com.tradebit.repositories.BinanceAccountLinkRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class BinanceLinkServiceImpl implements BinanceLinkService {
    private final BinanceAccountLinkRepository repository;
    private final EncryptionUtil encryptionUtil;
    private final BinanceApiService binanceApiService;
    @Override
    @Transactional
    public void linkAccount(BinanceLinkDTO binanceLinkDTO, String userId) {
        String encryptedApiKey = encryptionUtil.encrypt(binanceLinkDTO.getApiKey());
        String encryptedSecretKey = encryptionUtil.encrypt(binanceLinkDTO.getSecretApiKey());

        JsonNode response = binanceApiService.getAccountData(binanceLinkDTO, "/api/v3/account");

        if (!response.has("uid"))
            throw new BinanceLinkException("Invalid API or secret key");

        if (repository.existsByUserId(userId))
            repository.deleteByUserId(userId);

        BinanceAccountLink binanceAccountLink = BinanceAccountLink.builder()
                .apiKey(encryptedApiKey)
                .secret_key(encryptedSecretKey)
                .userId(userId)
                .build();
        repository.save(binanceAccountLink);
    }

    @Override
    public String getUserIdFromAuthentication(Authentication authentication) {
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
        return jwtAuthenticationToken.getToken().getClaimAsString("sub");
    }
}
