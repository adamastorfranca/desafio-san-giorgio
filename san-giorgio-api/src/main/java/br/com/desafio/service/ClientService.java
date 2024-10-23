package br.com.desafio.service;

import br.com.desafio.exception.NotFoundAlertException;
import br.com.desafio.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientService {

    private final ClientRepository clientRepository;

    /**
     * Ensures that a client exists in the repository by its ID.
     *
     * @param cliendId the UUID of the client to verify
     * @throws NotFoundAlertException if the client does not exist
     */
    public void ensureExistsById(UUID cliendId) {
        if (!clientRepository.existsById(cliendId)) {
            log.error("[CLIENT-SERVICE] Client not found with ID: {}", cliendId);
            throw new NotFoundAlertException("Client not found with ID: " + cliendId);
        }
    }

}
