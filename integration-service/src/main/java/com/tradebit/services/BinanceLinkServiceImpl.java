package com.tradebit.services;

import com.tradebit.dto.BinanceLinkDTO;
import com.tradebit.encryption.EncryptionUtil;
import com.tradebit.models.BinanceAccountLink;
import com.tradebit.repositories.BinanceAccountLinkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class BinanceLinkServiceImpl implements BinanceLinkService {
    private final BinanceAccountLinkRepository repository;
    private final EncryptionUtil encryptionUtil;
    @Override
    public void linkAccount(BinanceLinkDTO binanceLinkDTO, String userId) {
        String encryptedApiKey = encryptionUtil.encrypt(binanceLinkDTO.getApiKey());
        String encryptedSecretKey = encryptionUtil.encrypt(binanceLinkDTO.getSecretApiKey());
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
