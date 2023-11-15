package com.tradebit.services;

import com.tradebit.dto.BinanceLinkDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.Map;

public interface BinanceLinkService {
    void linkAccount(BinanceLinkDTO binanceLinkDTO, String userId);

    String getUserIdFromAuthentication(Authentication authentication);
}
