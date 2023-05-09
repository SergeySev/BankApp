package com.example.telproject.controller;

import com.example.telproject.dto.ClientCreateDto;
import com.example.telproject.dto.ClientDTO;
import com.example.telproject.service.ClientService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
     *
     * @return a List of ClientDTO objects representing all clients in the system.
     */
    @ApiResponse(responseCode = "200", description = "Successful retrieval")
    @GetMapping
    public List<ClientDTO> showClients() {
        return clientService.getClients();
    }

    /**
     * Retrieves a list of all clients from the client service.
     *
     * @return a List of ClientDTO objects representing all clients in the system.
     */
    @GetMapping(path = {"allClientByPage"})
    public Page<ClientDTO> showClients(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "30") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return clientService.getClientsByPage(pageable);
    }

    /**
     * Retrieves a list of clients with a matching first and last name.
     *
     * @param first_name the first name to search for.
     * @param last_name  the last name to search for.
     * @return a List of ClientDTO objects representing the clients matching the provided first and last name.
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval"),
            @ApiResponse(responseCode = "400", description = "Client with name {name} not found"),
    })
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
     *
     * @param client     the Client object containing the details of the new client to be added.
     * @return a ClientDTO object containing the details of the new client.
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "When a new confirmation token is generated and returned successfully." +
                    "\nUse this json:" +
                    "\n{\n" +
                    "    \"manager_id\": 1,\n" +
                    "    \"first_name\": \"Second\",\n" +
                    "    \"last_name\": \"Client\",\n" +
                    "    \"email\": \"third@gmail.com\",\n" +
                    "    \"password\": \"123123\",\n" +
                    "    \"birth_date\": \"1996-02-16T00:00:00.000Z\"\n" +
                    "}"),
            @ApiResponse(responseCode = "400", description = "Client already exists or some problem with client data"),
            @ApiResponse(responseCode = "500", description = "Mail server connection failed. Couldn't connect to host, port: localhost, 1025; timeout 5000;")
    })
    @PostMapping(path = "/registration")
    public String registerClient(@RequestBody ClientCreateDto client) {
        return clientService.register(client);
    }

    /**
     * Retrieves the confirmation token from the client's email and activates their account.
     * If the token is not valid or the client does not exist, an exception will be thrown.
     *
     * @param token a string representing the confirmation token sent to the client's email
     * @return a string indicating whether the client's account was successfully activated or not
     * @throws IllegalStateException if the token is not valid or the client does not exist
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval"),
            @ApiResponse(responseCode = "400", description = "Token doesn't exist"),
    })
    @GetMapping(path = "/confirm")
    public String confirm(@RequestParam("token") String token) {
        return clientService.confirmToken(token);
    }

}
