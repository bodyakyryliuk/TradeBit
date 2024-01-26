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
        try {
            log.info("Sending message of " + emailRequest.getEmailType() + " type");
            switch (emailRequest.getEmailType()) {
                case VERIFICATION_EMAIL:
                    emailService.sendVerificationMail(emailRequest);
                    break;
                case RESET_PASSWORD_EMAIL:
                    emailService.sendResetPasswordMail(emailRequest);
                    break;
                case BUY_ORDER:
                    emailService.sendBuyOrderMail(emailRequest);
                    break;
                case SELL_ORDER:
                    emailService.sendSellOrderMail(emailRequest);
                    break;
                default:
                    log.warn("Unhandled email type: {}", emailRequest.getEmailType());
            }
        } catch (Exception e) {
            log.error("Error processing message", e);
        }
    }

}