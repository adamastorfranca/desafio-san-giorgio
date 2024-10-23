package br.com.desafio.listener;

import br.com.desafio.domain.dto.PaymentQueueMessage;
import br.com.desafio.util.QueueConstants;
import io.awspring.cloud.messaging.listener.annotation.SqsListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static br.com.desafio.util.QueueConstants.*;

@Component
@Slf4j
public class PaymentStatusListener {

    /**
     * Listens for messages on the Partial Payments SQS queue and processes them.
     * <p>
     * This method is triggered whenever a message is received on the queue defined by
     * {@link QueueConstants#PARTIAL_PAYMENTS_QUEUE}. It logs the received message and can be
     * extended to include business logic for handling partial payments.
     * </p>
     *
     * @param paymentQueueMessage the PaymentQueueMessage object received from the SQS queue, representing a partial payment.
     *                   The object is automatically deserialized from the JSON message.
     */
    @SqsListener(PARTIAL_PAYMENTS_QUEUE)
    public void handlePartialPayment(PaymentQueueMessage paymentQueueMessage) {
        log.info("[PAYMENT-STATUS-LISTENER] Received Partial Payment Message: {}", paymentQueueMessage);
    }

    /**
     * Listens for messages on the Total Payments SQS queue and processes them.
     * <p>
     * This method is triggered whenever a message is received on the queue defined by
     * {@link QueueConstants#TOTAL_PAYMENTS_QUEUE}. It logs the received message and can be
     * extended to include business logic for handling total payments.
     * </p>
     *
     * @param paymentQueueMessage the PaymentQueueMessage object received from the SQS queue, representing a total payment.
     *                   The object is automatically deserialized from the JSON message.
     */
    @SqsListener(TOTAL_PAYMENTS_QUEUE)
    public void handleTotalPayment(PaymentQueueMessage paymentQueueMessage) {
        log.info("[PAYMENT-STATUS-LISTENER] Received Total Payment Message: {}", paymentQueueMessage);
    }

    /**
     * Listens for messages on the Surplus Payments SQS queue and processes them.
     * <p>
     * This method is triggered whenever a message is received on the queue defined by
     * {@link QueueConstants#SURPLUS_PAYMENTS_QUEUE}. It logs the received message and can be
     * extended to include business logic for handling excess payments.
     * </p>
     *
     * @param paymentQueueMessage the PaymentQueueMessage object received from the SQS queue, representing a surplus payment.
     *                   The object is automatically deserialized from the JSON message.
     */
    @SqsListener(SURPLUS_PAYMENTS_QUEUE)
    public void handleSurplusPayment(PaymentQueueMessage paymentQueueMessage) {
        log.info("[PAYMENT-STATUS-LISTENER] Received Surplus Payment Message: {}", paymentQueueMessage);
    }

}
