package com.example.telproject.controller;

import com.example.telproject.dto.ClientDTO;
import com.example.telproject.entity.Client;
import com.example.telproject.service.ClientService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    /**
     * Retrieves a list of all clients from the client service.
     * @return a List of ClientDTO objects representing all clients in the system.
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval"),
            @ApiResponse(responseCode = "400", description = "Manager with id {id} not found"),
    })
    @GetMapping
    public List<ClientDTO> showClients() {
        return clientService.getClients();
    }

    /**
     * Retrieves a list of clients with a matching first and last name.
     * @param first_name the first name to search for.
     * @param last_name the last name to search for.
     * @return a List of ClientDTO objects representing the clients matching the provided first and last name.
     */
    @GetMapping(path = "/getClients")
    public List<ClientDTO> showClientByName(@PathParam("first_name") String first_name,
                                            @PathParam("last_name") String last_name) {
        return clientService.findClientsByName(first_name, last_name);
    }

    /**
     * This method registers a new client with the provided details.
     * <p>
     * It checks if a client with the given email already exists in the database.
     * <p>
     * If the client is already registered and active, it throws an exception.
     * <p>
     * Otherwise, if the client is already registered but inactive, it generates a new confirmation token and returns it.
     * <p>
     * If the client is not registered, it encodes the provided password, saves the client to the database,
     * generates a new confirmation token, and returns it.
     * @param client the Client object containing the details of the new client to be added.
     * @param manager_id the manager_id who will work with new client
     * @return a ClientDTO object containing the details of the new client.
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "When a new confirmation token is generated and returned successfully"),
            @ApiResponse(responseCode = "400", description = "Manager already exists or some problem with manager data"),
            @ApiResponse(responseCode = "500", description = "Mail server connection failed. Couldn't connect to host, port: localhost, 1025; timeout 5000;")
    })
    @PostMapping(path = "/registration")
    public String registerClient(@RequestBody Client client,
                                 @RequestParam Long manager_id) {
        return clientService.register(client, manager_id);
    }

    /**

     Retrieves the confirmation token from the client's email and activates their account.
     If the token is not valid or the client does not exist, an exception will be thrown.
     @param token a string representing the confirmation token sent to the client's email
     @return a string indicating whether the client's account was successfully activated or not
     @throws IllegalStateException if the token is not valid or the client does not exist
     */
    @GetMapping(path = "/confirm")
    public String confirm(@RequestParam("token") String token) {
        return clientService.confirmToken(token);
    }

}
