package com.example.telproject.controller;

import com.example.telproject.dto.ClientDTO;
import com.example.telproject.entity.Client;
import com.example.telproject.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/client")
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public List<Client> showClients() {
        return clientService.getClients();
    }

    @GetMapping(path = "{findClient}")
    public ClientDTO showClient(@PathVariable ("findClient") String name) {
        return clientService.findClientByName(name);
    }

}
