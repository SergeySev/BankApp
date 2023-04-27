package com.example.telproject.controller;

import com.example.telproject.dto.ManagerDTO;
import com.example.telproject.entity.Manager;
import com.example.telproject.service.ManagerService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/managers")
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerService managerService;

    /**
     * Finds a manager by his name.
     *
     * @param name the name of the manager to search for.
     * @return a list of ManagerDTO objects representing the searched manager
     *
     * @throws ManagerNotFoundException if no manager with the given name was found.
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval"),
            @ApiResponse(responseCode = "400", description = "Manager with name {name} not found"),
    })
    @GetMapping(path = "{name}")
    public List<ManagerDTO> getManager(@PathVariable("name") String name) {
        return managerService.findManagerByName(name);
    }

    /**
     * Finds a manager by his name.
     *
     * @param id the id of the manager to search for.
     * @return a ManagerDTO object representing the searched manager
     *
     * @throws ManagerNotFoundException if no manager with the given id was found.
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval"),
            @ApiResponse(responseCode = "400", description = "Manager with id {id} not found"),
    })
    @GetMapping(path = "/findById/{id}")
    public ManagerDTO findManagerById(@PathVariable("id") Long id) {
        return managerService.findById(id);
    }

    /**
     * Registers a new manager with the provided details.
     *
     * @param manager the Manager object containing the details of the new manager to be added.
     * @return a ManagerDTO object containing the details of the newly added manager.
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful registered"),
            @ApiResponse(responseCode = "400", description = "Manager already exists or some problem with manager data"),
    })
    @PostMapping(path = "/registerManager")
    public ManagerDTO addNewManager(@RequestBody Manager manager) {
        return managerService.addNewManager(manager);
    }

    /**
     * Update a manager's details.
     *
     * @param manager The updated manager object.
     * @return The updated manager object as a ManagerDTO.
     *
     * @throws ManagerRequestException if the manager is not found in the database.
     *
     * @apiNote This API updates the details of a manager in the database.
     * The manager's details are provided in the request body as a Manager object.
     * The response contains the updated manager details as a ManagerDTO object.
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Manager details updated successfully"),
            @ApiResponse(responseCode = "400", description = "Manager with given name not found in the database")
    })
    @PutMapping(path = "/update")
    @Transactional
    public ManagerDTO update(@RequestBody Manager manager) {
        return managerService.updateManager(manager);
    }

}
