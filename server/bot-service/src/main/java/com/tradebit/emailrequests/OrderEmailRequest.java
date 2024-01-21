package com.tradebit.emailrequests;

import com.tradebit.models.EmailType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class OrderEmailRequest {
    private EmailType emailType;
    private String to;
    private String botName;
    private Long botId;
    private String tradingPair;
    private double buyPrice;
    private double quantity;
    private LocalDateTime timestamp;
}
