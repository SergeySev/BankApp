package com.example.telproject.service;

import com.example.telproject.entity.Manager;
import com.example.telproject.repository.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ManagerService {
    private final ManagerRepository managerRepository;

    @Autowired
    public ManagerService(ManagerRepository managerRepository) {
        this.managerRepository = managerRepository;
    }

    public List<Manager> getManagers() {
        return managerRepository.findAll();
    }

    public Optional<Manager> findManagerByName(String name) {
        Optional<Manager> manager = managerRepository.findManagerByFirstName(name);

        if (manager.isPresent()) return manager;
        else throw new IllegalStateException("Manager with name: " + name + " doesn't exists in database");
    }

    public String addNewManager(Manager manager) {
        Manager newManager = new Manager(manager.getFirstName(), manager.getLastName(), manager.getStatus());
        managerRepository.save(newManager);
        return "The manager " + newManager + " was added to db";
    }

    //TODO: fix an error with update Manager
    @Transactional
    public String updateManager(String firstName, String lastName) {
        Manager manager = managerRepository.
                findManagerByFullName(firstName, lastName).
                orElseThrow(() -> new IllegalStateException(
                        "The manager with name: " +
                                firstName + " and last name: " +
                                lastName + " doesn't exists in database"));
        manager.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
        managerRepository.save(manager);
        return "The manager: " + "\n" + manager + "\nWas successfully updated";
    }
}
