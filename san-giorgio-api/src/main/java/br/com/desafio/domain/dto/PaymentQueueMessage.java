package br.com.desafio.domain.dto;

import br.com.desafio.domain.enumeration.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

public record PaymentQueueMessage(
    @JsonProperty("client_id") UUID clientId,
    @JsonProperty("payment_id") UUID paymentId,
    @JsonProperty("payment_value") BigDecimal paymentValue,
    @JsonProperty("payment_status") PaymentStatus paymentStatus
) implements Serializable {

    private static final long serialVersionUID = 1L;

}
