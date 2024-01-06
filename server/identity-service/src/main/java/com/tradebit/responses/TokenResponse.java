package com.tradebit.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponse {
    private String access_token;
    private int expires_in;
    private int refresh_expires_in;
    private String refresh_token;
    private String token_type;
    private String id_token;
    private int not_before_policy;
    private String session_state;
    private String scope;
    private String error;
    private String error_description;
    private String error_uri;
}
