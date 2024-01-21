package com.tradebit.services;

import com.tradebit.requests.BuyOrderEmailRequest;
import com.tradebit.requests.EmailRequest;
import com.tradebit.requests.SellOrderEmailRequest;

public interface EmailService {
    void sendVerificationMail(EmailRequest emailRequest);

    void sendResetPasswordMail(EmailRequest emailRequest);

    void sendBuyOrderMail(BuyOrderEmailRequest emailRequest);

    void sendSellOrderMail(SellOrderEmailRequest emailRequest);

}
