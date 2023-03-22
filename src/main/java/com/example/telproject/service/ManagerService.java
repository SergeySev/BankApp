package com.example.telproject.service;

import com.example.telproject.dto.ManagerDTO;
import com.example.telproject.entity.Manager;
import com.example.telproject.mapper.ManagerMapper;
import com.example.telproject.repository.ManagerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ManagerService implements ManagerServiceInterface {
    private final ManagerRepository managerRepository;

    private final ManagerMapper managerMapper;

    public ManagerDTO findManagerByNameDTO(String name) {
        return managerMapper.toDto(managerRepository.findManagerByFirstName(name).orElseThrow(() -> new IllegalStateException("Manager with name: " + name + " doesn't exists in database")));
    }

    public String addNewManager(Manager manager) {
        Manager newManager = new Manager(manager.getFirstName(), manager.getLastName(), manager.getStatus());
        managerRepository.save(newManager);
        return "The manager " + newManager + " was added to db";
    }

    @Transactional
    public String updateManager(String firstName, String lastName) {
        Manager manager = managerRepository.
                findManagerByFullName(firstName, lastName).
                orElseThrow(() -> new IllegalStateException(
                        "The manager with name: " +
                                firstName + " and last name: " +
                                lastName + " doesn't exists in database"));
        manager.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
        return "The manager: " + "\n" + manager + "\nWas successfully updated";
    }
}
