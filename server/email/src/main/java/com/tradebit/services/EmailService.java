package com.tradebit.services;

import com.tradebit.requests.EmailRequest;

public interface EmailService {
    void sendVerificationMail(EmailRequest emailRequest);

    void sendResetPasswordMail(EmailRequest emailRequest);

    void sendBuyOrderMail(EmailRequest emailRequest);

    void sendSellOrderMail(EmailRequest emailRequest);
}