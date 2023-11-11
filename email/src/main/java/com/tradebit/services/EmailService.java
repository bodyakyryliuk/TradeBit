package com.tradebit.services;

import com.tradebit.EmailRequest;

public interface EmailService {
    void sendVerificationMail(EmailRequest emailRequest);

    void sendResetPasswordMail(EmailRequest emailRequest);
}
