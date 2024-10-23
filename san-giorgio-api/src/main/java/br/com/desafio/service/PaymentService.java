package br.com.desafio.service;

import br.com.desafio.domain.Payment;
import br.com.desafio.domain.dto.ClientPaymentsDTO;
import br.com.desafio.domain.dto.PaymentDTO;
import br.com.desafio.domain.dto.PaymentQueueMessage;
import br.com.desafio.domain.enumeration.PaymentStatus;
import br.com.desafio.exception.BadRequestAlertException;
import br.com.desafio.exception.NotFoundAlertException;
import br.com.desafio.repository.PaymentRepository;
import br.com.desafio.service.messaging.MessageService;
import br.com.desafio.service.usecase.ConfirmPaymentUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static br.com.desafio.util.QueueConstants.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService implements ConfirmPaymentUseCase {

    private final PaymentRepository paymentRepository;
    private final ClientService clientService;
    private final MessageService messageService;

    /**
     * Confirms the payments for a specified client, validating each payment and determining its status
     * (partial, total, or excess) based on the amount paid compared to the original amount.
     * <p>
     * Sends the payment to the corresponding SQS queue based on its status and returns the updated
     * payments with their statuses.
     * </p>
     *
     * @param clientPaymentsDTO the client and payments data to be confirmed
     * @return the updated {@link ClientPaymentsDTO} containing payments with confirmed statuses
     * @throws BadRequestAlertException if a payment does not belong to the specified client
     * @throws NotFoundAlertException   if a payment or client does not exist
     */
    @Override
    public ClientPaymentsDTO confirm(ClientPaymentsDTO clientPaymentsDTO) {
        log.info("[PAYMENT-SERVICE] Starting payment confirmation for Client ID: {}", clientPaymentsDTO.getClientId());

        UUID clientId = clientPaymentsDTO.getClientId();
        clientService.ensureExistsById(clientId);

        List<PaymentDTO> updatedPayments = clientPaymentsDTO.getPayments().stream()
            .map(paymentDTO -> {
                Payment payment = getById(paymentDTO.getPaymentId());

                if (!clientId.equals(payment.getClient().getClientId())) {
                    log.error("[PAYMENT-SERVICE] Payment ID {} does not belong to the specified client ID {}.", payment.getPaymentId(), clientId);
                    throw new BadRequestAlertException("Payment with ID: " + payment.getPaymentId() + " does not belong to client with ID: " + clientId);
                }

                BigDecimal originalAmount = payment.getPaymentValue();
                BigDecimal paidAmount = paymentDTO.getPaymentValue();

                PaymentStatus status = determinePaymentStatus(originalAmount, paidAmount);

                payment.setPaymentStatus(status);
                paymentRepository.save(payment);

                paymentDTO.setPaymentStatus(status);

                sendPaymentMessage(new PaymentQueueMessage(clientId, payment.getPaymentId(),payment.getPaymentValue(), status));

                return paymentDTO;
            })
            .toList();

        clientPaymentsDTO.setPayments(updatedPayments);

        log.info("[PAYMENT-SERVICE] Payment confirmation completed for Client ID: {}", clientPaymentsDTO.getClientId());
        return clientPaymentsDTO;

    }

    /**
     * Retrieves a payment by its ID, throwing an exception if not found.
     *
     * @param paymentId the payment ID
     * @return the found {@link Payment}
     * @throws NotFoundAlertException if the payment does not exist
     */
    public Payment getById(UUID paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> {
                    log.error("[PAYMENT-SERVICE] Payment not found for ID: {}", paymentId);
                    return new NotFoundAlertException("Payment not found for ID: " + paymentId);
                });
    }

    /**
     * Sends a payment message to the corresponding SQS queue based on the payment status.
     *
     * @param paymentQueueMessage the payment data to be sent
     */
    private void sendPaymentMessage(PaymentQueueMessage paymentQueueMessage) {
        switch (paymentQueueMessage.paymentStatus()) {
            case PARTIAL:
                messageService.sendMessageToQueue(PARTIAL_PAYMENTS_QUEUE, paymentQueueMessage);
                break;
            case TOTAL:
                messageService.sendMessageToQueue(TOTAL_PAYMENTS_QUEUE, paymentQueueMessage);
                break;
            case SURPLUS:
                messageService.sendMessageToQueue(SURPLUS_PAYMENTS_QUEUE, paymentQueueMessage);
                break;
        }
        log.info("[PAYMENT-SERVICE] Sent payment message to {} queue for Payment ID: {}", paymentQueueMessage.paymentStatus(), paymentQueueMessage.paymentId());
    }

    /**
     * Determines the payment status (partial, total, or excess) based on the original and paid amounts.
     *
     * @param originalAmount the original amount of the payment
     * @param paidAmount the amount paid
     * @return the {@link PaymentStatus}
     */
    private PaymentStatus determinePaymentStatus(BigDecimal originalAmount, BigDecimal paidAmount) {
        if (paidAmount.compareTo(originalAmount) < 0) {
            return PaymentStatus.PARTIAL;
        } else if (paidAmount.compareTo(originalAmount) == 0) {
            return PaymentStatus.TOTAL;
        } else {
            return PaymentStatus.SURPLUS;
        }
    }

}
