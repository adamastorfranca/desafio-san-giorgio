package br.com.desafio.service;

import br.com.desafio.domain.Client;
import br.com.desafio.domain.Payment;
import br.com.desafio.domain.dto.ClientPaymentsDTO;
import br.com.desafio.domain.dto.PaymentDTO;
import br.com.desafio.domain.dto.PaymentQueueMessage;
import br.com.desafio.domain.enumeration.PaymentStatus;
import br.com.desafio.exception.NotFoundAlertException;
import br.com.desafio.repository.PaymentRepository;
import br.com.desafio.service.messaging.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    private static final BigDecimal ORIGINAL_PAYMENT_VALUE = BigDecimal.valueOf(100);

    @InjectMocks
    private PaymentService paymentService;

    @Mock
    private ClientService clientService;

    @Mock
    private MessageService messageService;

    @Mock
    private PaymentRepository paymentRepository;

    private UUID clientId;
    private UUID paymentId;
    private Payment payment;
    private ClientPaymentsDTO clientPaymentsDTO;

    @BeforeEach
    void setUp() {
        clientId = UUID.randomUUID();
        paymentId = UUID.randomUUID();

        Client client = Client.builder().clientId(clientId).name("Test Client").build();
        payment = Payment.builder()
                .paymentId(paymentId)
                .client(client)
                .paymentValue(ORIGINAL_PAYMENT_VALUE)
                .build();

        PaymentDTO paymentDTO = new PaymentDTO(paymentId, ORIGINAL_PAYMENT_VALUE, null);
        clientPaymentsDTO = new ClientPaymentsDTO(clientId, List.of(paymentDTO));
    }

    @DisplayName("Should process payments with different values and expected statuses")
    @ParameterizedTest(name = "{index} => paymentValue={0}, expectedStatus={1}, expectedQueue={2}")
    @CsvSource({
            "100, TOTAL, total-payments",
            "50, PARTIAL, partial-payments",
            "150, SURPLUS, surplus-payments"
    })
    void shouldProcessPaymentsWithDifferentValues(BigDecimal paymentValue, PaymentStatus expectedStatus, String expectedQueue) {
        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));
        doNothing().when(clientService).ensureExistsById(clientId);
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
        clientPaymentsDTO.getPayments().get(0).setPaymentValue(paymentValue);

        ClientPaymentsDTO result = paymentService.confirm(clientPaymentsDTO);

        assertNotNull(result);
        assertEquals(1, result.getPayments().size());
        PaymentDTO paymentResult = result.getPayments().get(0);
        assertEquals(expectedStatus, paymentResult.getPaymentStatus());

        verify(messageService, times(1)).sendMessageToQueue(eq(expectedQueue), any(PaymentQueueMessage.class));
        verifyNoMoreInteractions(messageService);
    }

    @DisplayName("Should throw exception when client is not found")
    @Test
    void shouldThrowExceptionWhenClientNotFound() {
        UUID invalidClientId = UUID.randomUUID();
        doThrow(new NotFoundAlertException("Client not found for ID: " + invalidClientId))
                .when(clientService).ensureExistsById(invalidClientId);

        clientPaymentsDTO.setClientId(invalidClientId);

        NotFoundAlertException exception = assertThrows(NotFoundAlertException.class, () -> {
            paymentService.confirm(clientPaymentsDTO);
        });

        assertEquals("Client not found for ID: " + invalidClientId, exception.getMessage());
        verify(clientService, times(1)).ensureExistsById(invalidClientId);
        verifyNoInteractions(paymentRepository, messageService);
    }

    @DisplayName("Should throw exception when payment code is not found")
    @Test
    void shouldThrowExceptionWhenPaymentCodeNotFound() {
        UUID invalidPaymentId = UUID.randomUUID();
        when(paymentRepository.findById(invalidPaymentId)).thenReturn(Optional.empty());
        doNothing().when(clientService).ensureExistsById(clientId);

        PaymentDTO invalidPayment = new PaymentDTO();
        invalidPayment.setPaymentId(invalidPaymentId);
        clientPaymentsDTO.setPayments(List.of(invalidPayment));

        NotFoundAlertException exception = assertThrows(NotFoundAlertException.class, () -> {
            paymentService.confirm(clientPaymentsDTO);
        });

        assertEquals("Payment not found for ID: " + invalidPaymentId, exception.getMessage());
        verify(paymentRepository, times(1)).findById(invalidPaymentId);
        verifyNoInteractions(messageService);
    }

}
