package br.com.desafio.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientPaymentsDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("client_id")
    @NotNull(message = "Client ID cannot be null")
    private UUID clientId;

    @JsonProperty("payments")
    @NotEmpty(message = "Payments list cannot be empty")
    private List<PaymentDTO> payments;

}
