package com.example.telproject.service;

import com.example.telproject.dto.ManagerDTO;
import com.example.telproject.entity.Manager;
import com.example.telproject.exception.ManagerRequestException;
import com.example.telproject.mapper.ManagerMapper;
import com.example.telproject.repository.ManagerRepository;
import com.example.telproject.security.CheckingEmail;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ManagerService {
    private final ManagerRepository managerRepository;
    private final ManagerMapper managerMapper;
    private final CheckingEmail checkingEmail;
    private final String MANAGER_NOT_FOUND = "Manager with %s %s doesn't exist in the database";

    /**
     Retrieves a list of ManagerDTOs based on the given first name.
     @param name The first name of the manager to search for.
     @return A list of ManagerDTOs that match the given name.
     @throws ManagerRequestException if no manager with the given name is found.
     */
    public List<ManagerDTO> findManagerByName(String name) {
        List<ManagerDTO> result = managerMapper.
                listToDTO(managerRepository.
                        findManagerByFirstName(name));
        if (result.isEmpty()) throw new ManagerRequestException(String.format(MANAGER_NOT_FOUND, "name", name));
        return result;
    }

    /**
     Retrieves a ManagerDTO based on the given ID.
     @param id The ID of the manager to retrieve.
     @return A ManagerDTO that matches the given ID.
     @throws ManagerRequestException if no manager with the given ID is found.
     */
    public ManagerDTO findById(Long id) {
        return managerMapper.
                toDto(managerRepository.
                        findById(id).
                        orElseThrow(() ->
                                new ManagerRequestException(String.format(MANAGER_NOT_FOUND, "id", id))));
    }

    /**
     * Adds a new manager to the database with the provided details.
     * Throws ManagerRequestException if any of the required fields are empty, if a manager with the same name and email
     * already exists, if a manager with the same email already exists or if the email provided is not valid.
     *
     * @param manager the Manager object containing the details of the new manager to be added.
     * @return a ManagerDTO object containing the details of the newly added manager.
     * @throws ManagerRequestException if the provided manager object does not meet the requirements for a new manager.
     */
    public ManagerDTO addNewManager(Manager manager) {
        if (manager.getFirst_name() == null || manager.getLast_name() == null || manager.getEmail() == null ||
                manager.getPhone_number() == null || manager.getBirth_date() == null || manager.getStatus() == null) {
            throw new ManagerRequestException("Some fields are empty");
        }

        Optional<Manager> manager1 = managerRepository.
                findManagerByFullNameAndEmail(
                        manager.getFirst_name(),
                        manager.getLast_name(),
                        manager.getEmail()
                );

        if (manager1.isPresent()) {
            throw new ManagerRequestException("This manager is in the DB");
        }
        if (managerRepository.findManagerByEmail(manager.getEmail()).isPresent()) {
            throw new ManagerRequestException("Manager with this email already registered");
        }
        if (!checkingEmail.test((manager.getEmail()))) {
            throw new ManagerRequestException("Email is not valid");
        }
        Manager newManager = new Manager(
                manager.getFirst_name(),
                manager.getLast_name(),
                manager.getStatus(),
                manager.getBirth_date(),
                manager.getEmail(),
                manager.getPhone_number());
        managerRepository.save(newManager);
        return managerMapper.toDto(newManager);
    }

    /**
     * Update a manager's details in the database.
     *
     * @param manager The updated manager object.
     * @return The updated manager object as a ManagerDTO.
     *
     * @throws ManagerRequestException if the manager is not found in the database.
     *
     * @apiNote This method updates the details of a manager in the database.
     * The manager's details are provided as a Manager object.
     * The method returns the updated manager details as a ManagerDTO object.
     */
    @Transactional
    public ManagerDTO updateManager(Manager manager) {
        Manager managerInDb = managerRepository.
                findManagerByFullNameAndEmail(
                        manager.getFirst_name(),
                        manager.getLast_name(),
                        manager.getEmail()
                ).
                orElseThrow(
                        () -> new ManagerRequestException(
                                String.format(MANAGER_NOT_FOUND,
                                        "name",
                                        manager.getFirst_name() + " " + manager.getLast_name())));
        managerInDb.setUpdated_at(Timestamp.valueOf(LocalDateTime.now()));
        return managerMapper.toDto(managerInDb);
    }
}
