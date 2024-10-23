package br.com.desafio.service.messaging;

import br.com.desafio.domain.dto.PaymentQueueMessage;
import br.com.desafio.exception.BadRequestAlertException;
import io.awspring.cloud.messaging.core.QueueMessagingTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {

    private final QueueMessagingTemplate queueMessagingTemplate;

    /**
     * Sends a message to the specified SQS queue.
     * <p>
     * This method is responsible for sending a message to an AWS SQS queue. The message is
     * created from a PaymentQueueMessage object, which will be automatically serialized to JSON format
     * by the QueueMessagingTemplate.
     * </p>
     *
     * @param queueName the name of the SQS queue to which the message will be sent.
     *                  It should be a valid queue name or URL configured within AWS.
     * @param paymentQueueMessage the PaymentQueueMessage object containing the payment details that will
     *                   be sent as a message. This object will be serialized to JSON.
     */
    public void sendMessageToQueue(String queueName, PaymentQueueMessage paymentQueueMessage) {
        log.info("[MESSAGE-SERVICE] Preparing to send message to queue: {}", queueName);

        try {
            queueMessagingTemplate.convertAndSend(queueName, paymentQueueMessage);
            log.info("[MESSAGE-SERVICE] Successfully sent message to queue: {}", queueName);

        } catch (MessagingException e) {
            log.error("[MESSAGE-SERVICE] An unexpected error occurred while sending payment message to processing queue: {}", queueName);
            throw new BadRequestAlertException("Unexpected error while sending payment message to processing queue");
        }
    }
}
