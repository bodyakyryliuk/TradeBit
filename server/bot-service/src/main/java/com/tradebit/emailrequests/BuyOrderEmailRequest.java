package com.tradebit.emailrequests;

import com.tradebit.models.EmailType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
public class BuyOrderEmailRequest extends OrderEmailRequest {
}