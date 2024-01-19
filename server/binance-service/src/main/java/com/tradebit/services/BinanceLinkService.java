package com.tradebit.services;

import com.tradebit.dto.BinanceLinkDTO;
import com.tradebit.models.BinanceAccountLink;
import org.springframework.security.core.Authentication;

public interface BinanceLinkService {
    void linkAccount(BinanceLinkDTO binanceLinkDTO, String userId);

    String getUserIdFromAuthentication(Authentication authentication);

    void unlinkAccount(String userId);

    BinanceLinkDTO getBinanceLink(String userId);

}
