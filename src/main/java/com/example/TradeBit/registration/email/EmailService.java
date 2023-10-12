package com.example.TradeBit.registration.email;

public interface EmailService {
    void sendVerificationToken(String recipientEmail, String token);
}
