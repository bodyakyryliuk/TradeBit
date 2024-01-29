package com.tradebit.service;

import com.tradebit.dto.AuthorizationDTO;
import com.tradebit.responses.TokenResponse;

public interface AuthorizationService {
    TokenResponse login(AuthorizationDTO authorizationDTO);
    void forgotPassword(String email);

}
