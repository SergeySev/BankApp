package com.example.telproject.controller;

import com.example.telproject.dto.ClientDTO;
import com.example.telproject.entity.Client;
import com.example.telproject.service.ClientService;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/client")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:3000/")
public class ClientController {

    private final ClientService clientService;

    @GetMapping
    public List<ClientDTO> showClients() {
        return clientService.getClients();
    }

    @GetMapping(path = "/getClients")
    public List<ClientDTO> showClientByName(@PathParam("first_name") String first_name,
                                            @PathParam("last_name") String last_name) {
        return clientService.findClientsByName(first_name, last_name);
    }

    @PostMapping(path = "/registration")
    public String registerClient(@RequestBody Client client,
                                 @RequestParam Long manager_id) {
        return clientService.register(client, manager_id);
    }

    @GetMapping(path = "/confirm")
    public String confirm(@RequestParam("token") String token) {
        return clientService.confirmToken(token);
    }

}
