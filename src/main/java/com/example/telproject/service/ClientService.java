package com.example.telproject.service;

import com.example.telproject.dto.ClientDTO;
import com.example.telproject.entity.Client;
import com.example.telproject.entity.Manager;
import com.example.telproject.mapper.ClientMapper;
import com.example.telproject.repository.ClientRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    private final ClientMapper clientMapper;


    public List<Client> getClients() {
        List<Client> result = clientRepository.findAll();
        System.out.println("\n\n\n" + result + "\n\nResult");
        return clientRepository.findAll();
    }

    @Transactional
    public ClientDTO findClientByName(String name) {
        return clientMapper.toDto(clientRepository.findClientByFirstName(name).orElseThrow(() -> new IllegalStateException("Manager with name: " + name + " doesn't exists in database")));
    }
}
