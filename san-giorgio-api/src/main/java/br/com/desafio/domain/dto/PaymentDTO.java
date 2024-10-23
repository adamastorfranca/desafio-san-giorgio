package br.com.desafio.domain.dto;

import br.com.desafio.domain.enumeration.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("payment_id")
    @NotNull(message = "Payment ID is required")
    private UUID paymentId;

    @JsonProperty("payment_value")
    @NotNull(message = "Payment value is required")
    @Positive(message = "Payment value must be positive")
    @Digits(integer = 6, fraction = 2, message = "Payment value must have up to 6 digits and 2 decimal places")
    private BigDecimal paymentValue;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private PaymentStatus paymentStatus;

}
