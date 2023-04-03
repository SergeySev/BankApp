package com.example.telproject.service;

import com.example.telproject.dto.ManagerDTO;
import com.example.telproject.entity.Manager;
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

    public List<ManagerDTO> findManagerByName(String name) {
        List<ManagerDTO> result =  managerMapper.
                listToDTO(managerRepository.
                        findManagerByFirstName(name));
        if (result.isEmpty()) throw new IllegalStateException(String.format(MANAGER_NOT_FOUND, "name", name));
        return result;
    }

    public ManagerDTO findById(Long id) {
        return managerMapper.
                toDto(managerRepository.
                        findById(id).
                        orElseThrow(() ->
                                new IllegalStateException(String.format(MANAGER_NOT_FOUND, "id", id))));
    }

    public ManagerDTO addNewManager(Manager manager) {
        Optional<Manager> manager1 = managerRepository.
                findManagerByFullNameAndEmail(
                        manager.getFirst_name(),
                        manager.getLast_name(),
                        manager.getEmail()
                );

        if (manager1.isPresent()) {
            throw new IllegalStateException("This manager is in the DB");
        }
        if (managerRepository.findManagerByEmail(manager.getEmail()).isPresent()) {
            throw new IllegalStateException("Manager with this email already registered");
        }
        if (!checkingEmail.test((manager.getEmail()))) {
            throw new IllegalStateException("Email is not valid");
        }
        Manager newManager = new Manager(
                manager.getFirst_name(),
                manager.getLast_name(),
                manager.getStatus(),
                manager.getBirth_date(),
                manager.getEmail());
        managerRepository.save(newManager);
        return managerMapper.toDto(newManager);
    }

    @Transactional
    public ManagerDTO updateManager(Manager manager) {
        Manager managerInDb = managerRepository.
                findManagerByFullNameAndEmail(
                        manager.getFirst_name(),
                        manager.getLast_name(),
                        manager.getEmail()
                ).
                orElseThrow(
                        () -> new IllegalStateException(
                                String.format(MANAGER_NOT_FOUND,
                                        "name",
                                        manager.getFirst_name() + " " + manager.getLast_name())));
        managerInDb.setUpdated_at(Timestamp.valueOf(LocalDateTime.now()));
        return managerMapper.toDto(managerInDb);
    }
}
