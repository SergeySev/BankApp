package com.example.telproject.service;

import com.example.telproject.entity.Client;
import com.example.telproject.entity.Manager;
import com.example.telproject.repository.ClientRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ClientService {

    private final ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<Client> getClients() {
        return clientRepository.findAll();
    }

    @Transactional
    public Client findClientByName(String name) {
        Client client = clientRepository.findClientByFirstName(name).orElseThrow(() -> new IllegalStateException("Client with name: " + name + " doesn't exists in database"));
        log.info("asd {}", client.getManager());
        return client;
    }
}
