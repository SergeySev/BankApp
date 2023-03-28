package com.example.telproject.controller;

import com.example.telproject.dto.ClientDTO;
import com.example.telproject.service.ClientService;
import com.example.telproject.service.RequestClient;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/client")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping
    public List<ClientDTO> showClients() {
        return clientService.getClients();
    }

    @GetMapping(path = "/getClients")
    public List<ClientDTO> showClient(@PathParam("first_name") String first_name,
                                @PathParam("last_name") String last_name) {
        return clientService.findClientsByName(first_name, last_name);
    }

    @PostMapping(path = "/registration")
    public String registerClient(@RequestBody RequestClient client) {
        return clientService.register(client);
    }

    @GetMapping(path = "/confirm")
    public String confirm(@RequestParam("token") String token){
        return clientService.confirmToken(token);
    }

}
