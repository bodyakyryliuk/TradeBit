package com.tradebit.services;

import com.tradebit.requests.EmailRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Year;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService{
    private final JavaMailSender mailSender;
    private final ResourceLoader resourceLoader;

    @Value("${gateway.hostname}")
    private String hostName;
    @Override
    public void sendVerificationMail(EmailRequest emailRequest) {
        MimeMessage mail = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setTo(emailRequest.getTo());
            helper.setSubject("Email Verification");

            String content = loadTemplate("verification-email.html");
            content = content.replace("${verificationLink}",
                    "http://" + hostName + "/identity-service/auth/registrationConfirm?token=" + emailRequest.getMessage());
            helper.setText(content, true); // set to true to indicate the text content is HTML

            mailSender.send(mail);
        } catch (MessagingException | IOException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    @Override
    public void sendResetPasswordMail(EmailRequest emailRequest) {
        MimeMessage mail = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setTo(emailRequest.getTo());
            helper.setSubject("Password reset");

            String content = loadTemplate("reset-password-email.html");
            content = content.replace("${resetPasswordLink}",
                    "http://" + hostName + "/identity-service/auth/reset-password?token=" + emailRequest.getMessage());
            helper.setText(content, true); // set to true to indicate the text content is HTML

            mailSender.send(mail);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendOrderMail(EmailRequest emailRequest, String templateName) {
        MimeMessage mail = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setTo(emailRequest.getTo());
            helper.setSubject("Order executed");

            String content = loadTemplate(templateName);

            content = content.replace("[BotName]", emailRequest.getBotName());
            content = content.replace("[BotID]", String.valueOf(emailRequest.getBotId()));
            content = content.replace("[TradingPair]", emailRequest.getTradingPair());
            content = content.replace("[BuyPrice]", String.valueOf(emailRequest.getBuyPrice()));
            content = content.replace("[Quantity]", String.valueOf(emailRequest.getQuantity()));
            content = content.replace("[Timestamp]", emailRequest.getTimestamp());
            content = content.replace("[Current Year]", String.valueOf(Year.now().getValue()));

            if (emailRequest.getProfit() != null){
                content = content.replace("[Profit]", String.valueOf(emailRequest.getProfit()));
            }

            helper.setText(content, true); // set to true to indicate the text content is HTML

            mailSender.send(mail);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void sendBuyOrderMail(EmailRequest emailRequest) {
        sendOrderMail(emailRequest, "buy-order.html");
    }

    @Override
    public void sendSellOrderMail(EmailRequest emailRequest) {
        sendOrderMail(emailRequest, "sell-order.html");
    }


    private String loadTemplate(String filename) throws IOException {
        var resource = resourceLoader.getResource("classpath:templates/" + filename);
        try (InputStream inputStream = resource.getInputStream()) {
            return StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        }
    }
}