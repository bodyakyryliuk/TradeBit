package com.tradebit.requests;

import com.tradebit.EmailType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuyOrderEmailRequest extends OrderEmailRequest {
    private EmailType emailType;
    private String to;
    private String botName;
    private Long botId;
    private String tradingPair;
    private double buyPrice;
    private double quantity;
    private LocalDateTime timestamp;
}
