package com.tradebit.rabbitmq;

import com.tradebit.requests.EmailRequest;
import com.tradebit.services.EmailService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class EmailConsumer {
    private final EmailService emailService;

    @RabbitListener(queues = "${rabbitmq.queue.email}")
    public void consumer(EmailRequest emailRequest){
        log.info("Consumed {} from queue", emailRequest);
        switch (emailRequest.getEmailType()){
            case VERIFICATION_EMAIL -> emailService.sendVerificationMail(emailRequest);
            case RESET_PASSWORD_EMAIL -> emailService.sendResetPasswordMail(emailRequest);

        }
    }

}