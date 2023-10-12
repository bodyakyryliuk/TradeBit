package com.example.TradeBit.registration.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService{
    private final JavaMailSender mailSender;
    @Override
    public void sendVerificationToken(String recipientEmail, String token) {
        MimeMessage mail = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setTo(recipientEmail);
            helper.setSubject("Email Verification");

            String content = "To confirm your email, please click the link below:\n"
                    + "http://localhost:8080/registrationConfirm?token=" + token;
            helper.setText(content, true); // set to true to indicate the text content is HTML

            // TODO: implement email validation (regex)

            mailSender.send(mail);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
