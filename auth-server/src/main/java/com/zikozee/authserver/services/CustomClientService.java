package com.zikozee.authserver.services;

import com.zikozee.authserver.entity.Client;
import com.zikozee.authserver.repository.ClientRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Transactional
public class CustomClientService implements RegisteredClientRepository {

    private final ClientRepository clientRepository;

    @Override
    public void save(RegisteredClient registeredClient) {
        clientRepository.save(Client.from(registeredClient));
    }

    @Override
    public RegisteredClient findById(String id) {
        Client client = clientRepository.findById(Integer.valueOf(id))
                .orElseThrow();
        return Client.from(client);
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        Client client = clientRepository.findByClientId(clientId)
                .orElseThrow();
        return Client.from(client);
    }
}
