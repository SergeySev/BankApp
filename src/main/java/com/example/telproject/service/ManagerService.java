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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ManagerService {
    private final ManagerRepository managerRepository;
    private final ManagerMapper managerMapper;

    public ManagerDTO findManagerByName(String name) {
        return managerMapper.
                toDto(managerRepository.
                        findManagerByFirstName(name).
                        orElseThrow(() ->
                                new IllegalStateException(
                                        "Manager with name: " + name + " doesn't exists in database")));
    }

    public ManagerDTO findById(Long id) {
        return managerMapper.
                toDto(managerRepository.
                        findById(id).
                        orElseThrow(() ->
                                new IllegalStateException(
                                        "Manager with id: " + id + " doesn't exists in database")));
    }

    public String addNewManager(Manager manager) {
        Optional<Manager> manager1 = managerRepository.
                findManagerByFullNameAndBirthDay(
                        manager.getFirst_name(),
                        manager.getLast_name(),
                        manager.getBirth_date());

        if (manager1.isPresent()) {
            throw new IllegalStateException("This manager is in the DB");
        }
        Manager newManager = new Manager(
                manager.getFirst_name(),
                manager.getLast_name(),
                manager.getStatus(),
                manager.getBirth_date());
        managerRepository.save(newManager);
        return "The manager " + newManager + " was added to db";
    }

    @Transactional
    public String updateManager(String firstName, String lastName, Timestamp birthDate) {
        Manager manager = managerRepository.
                findManagerByFullNameAndBirthDay(firstName, lastName, birthDate).
                orElseThrow(() -> new IllegalStateException(
                        "The manager with name: " +
                                firstName + " and last name: " +
                                lastName + " doesn't exists in database"));
        manager.setUpdated_at(Timestamp.valueOf(LocalDateTime.now()));
        return "The manager: " + "\n" + manager + "\nWas successfully updated";
    }
}
