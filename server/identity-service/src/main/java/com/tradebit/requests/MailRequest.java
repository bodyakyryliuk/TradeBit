package com.tradebit.requests;

import com.tradebit.user.models.EmailType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MailRequest {
    String to;
    String message;
    EmailType emailType;
}