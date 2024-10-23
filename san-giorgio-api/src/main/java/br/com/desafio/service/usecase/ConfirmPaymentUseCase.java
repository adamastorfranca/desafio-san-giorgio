package br.com.desafio.service.usecase;

import br.com.desafio.domain.dto.ClientPaymentsDTO;

public interface ConfirmPaymentUseCase {

    ClientPaymentsDTO confirm(ClientPaymentsDTO clientPaymentsDTO);

}
