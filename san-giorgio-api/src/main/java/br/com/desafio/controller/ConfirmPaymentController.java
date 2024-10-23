package br.com.desafio.controller;

import static br.com.desafio.util.ApiPaths.API_CONTEXT_PATH;
import static br.com.desafio.util.ApiPaths.RESOURCE_PAYMENTS;

import br.com.desafio.domain.dto.ClientPaymentsDTO;
import br.com.desafio.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(API_CONTEXT_PATH)
@RequiredArgsConstructor
public class ConfirmPaymentController {

    private final PaymentService paymentService;

    /**
     * Confirm payments for a client and determine their status (partial, total, or surplus).
     *
     * @param clientPaymentsDTO Request body containing client ID and list of payments
     * @return a {@link ResponseEntity} with updated payment statuses
     */
    @PostMapping(path = RESOURCE_PAYMENTS)
    public ResponseEntity<ClientPaymentsDTO> confirmPayments(@Valid @RequestBody ClientPaymentsDTO clientPaymentsDTO) {

        ClientPaymentsDTO response = paymentService.confirm(clientPaymentsDTO);
        return ResponseEntity.ok(response);

    }

}
