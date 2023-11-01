package com.tradebit.services;

import com.tradebit.EmailRequest;

public interface EmailService {
    void sendVerificationToken(EmailRequest emailRequest);

}
