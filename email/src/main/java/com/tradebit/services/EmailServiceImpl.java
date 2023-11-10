package com.tradebit.services;

import com.tradebit.EmailRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService{
    private final JavaMailSender mailSender;
    @Value("${gateway.hostname}")
    private String hostName;
    @Override
    public void sendVerificationToken(EmailRequest emailRequest) {
        MimeMessage mail = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setTo(emailRequest.getTo());
            helper.setSubject("Email Verification");

            String content = "To confirm your email, please click the link below:\n"
                    + "http://" + hostName + "/identity-service/auth/registrationConfirm?token=" + emailRequest.getMessage();
            helper.setText(content, true); // set to true to indicate the text content is HTML

            // TODO: implement email validation (regex)

            mailSender.send(mail);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}