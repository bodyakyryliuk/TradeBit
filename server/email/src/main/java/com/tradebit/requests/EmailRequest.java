package com.tradebit.requests;

import com.tradebit.EmailType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class EmailRequest {
    private EmailType emailType;
    private String to;
    private String message;
    private String botName;
    private Long botId;
    private String tradingPair;
    private Double buyPrice;
    private Double quantity;
    private Double profit;
    private String timestamp;
}
