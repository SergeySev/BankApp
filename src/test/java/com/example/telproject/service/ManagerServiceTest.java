package com.example.telproject.service;

import com.example.telproject.dto.ManagerDTO;
import com.example.telproject.entity.Manager;
import com.example.telproject.entity.enums.ManagerStatus;
import com.example.telproject.mapper.ManagerMapper;
import com.example.telproject.repository.ManagerRepository;
import com.example.telproject.security.CheckingEmail;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class ManagerServiceTest {

    CheckingEmail checkingEmail = new CheckingEmail();

    ManagerRepository managerRepository = Mockito.mock(ManagerRepository.class);
    ManagerMapper managerMapper = Mockito.mock(ManagerMapper.class);

    ManagerService managerService = new ManagerService(managerRepository, managerMapper, checkingEmail);


    Manager createManager() {
        Manager manager = new Manager();
        manager.setId(1L);
        manager.setFirst_name("John");
        manager.setLast_name("Doe");
        manager.setStatus(ManagerStatus.ACTIVE);
        manager.setEmail("manager@gmail.com");
        manager.setBirth_date(Timestamp.valueOf("2023-04-03 13:01:36.968924"));
        manager.setCreated_at(Timestamp.valueOf("2023-04-03 13:01:36.968924"));
        manager.setUpdated_at(Timestamp.valueOf("2023-04-03 13:01:36.968924"));
        return manager;
    }

    ManagerDTO createManagerDto() {
        return new ManagerDTO("John",
                "Doe",
                ManagerStatus.ACTIVE.getValue(),
                "manager@gmail.com",
                "123123123",
                Timestamp.valueOf("2023-04-03 13:01:36.968924").toLocalDateTime(),
                Timestamp.valueOf("2023-04-03 13:01:36.968924").toLocalDateTime(),
                Timestamp.valueOf("2023-04-03 13:01:36.968924").toLocalDateTime());
    }

    @Test
    void findManagerByName() {
        Manager manager = createManager();
        List<Manager> managers = new ArrayList<>();
        managers.add(manager);

        ManagerDTO managerDTO = createManagerDto();
        List<ManagerDTO> managersDto = new ArrayList<>();
        managersDto.add(managerDTO);

        String name = "John";

        Mockito.when(managerRepository.findManagerByFirstName(name)).thenReturn(managers);
        Mockito.when(managerMapper.listToDTO(managers)).thenReturn(managersDto);

        managerService.findManagerByName(name);
        Mockito.verify(managerRepository, Mockito.times(1)).findManagerByFirstName(name);
    }

    @Test
    void findByNameException() {
        String name = "John";

        IllegalStateException illegalStateException = Assertions.
                assertThrows(
                        IllegalStateException.class,
                        () -> managerService.findManagerByName(name));
        Assertions.
                assertEquals(
                        "Manager with name John doesn't exist in the database",
                        illegalStateException.getMessage());
    }

    @Test
    void findManagerById() {
        Optional<Manager> manager = Optional.ofNullable(createManager());
        ManagerDTO managerDTO = createManagerDto();
        Long id = 1L;

        Mockito.when(managerRepository.findById(id)).thenReturn(manager);
        Mockito.when(managerMapper.toDto(manager.get())).thenReturn(managerDTO);

        managerService.findById(id);
        Mockito.verify(managerRepository, Mockito.times(1)).findById(id);
    }

    @Test
    void findManagerByIdException() {
        Long id = 1L;

        IllegalStateException illegalStateException = Assertions.
                assertThrows(
                        IllegalStateException.class,
                        () -> managerService.findById(id));
        Assertions.
                assertEquals(
                        "Manager with id 1 doesn't exist in the database",
                        illegalStateException.getMessage());
    }

    @Test
    void addNewManager() {
        Manager manager = createManager();
        manager.setId(1L);

        managerService.addNewManager(manager);
        Mockito.verify(managerRepository, Mockito.times(1)).findManagerByFullNameAndEmail(manager.getFirst_name(), manager.getLast_name(), manager.getEmail());
        Mockito.verify(managerRepository, Mockito.times(1)).findManagerByEmail(manager.getEmail());
        Mockito.verify(managerRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void addNewManagerExistsException() {
        Manager manager = createManager();
        Optional<Manager> managerOptional = Optional.ofNullable(createManager());

        Mockito.when(managerRepository.
                findManagerByFullNameAndEmail(
                        manager.getFirst_name(),
                        manager.getLast_name(),
                        manager.getEmail()
                )).thenReturn(managerOptional);

        IllegalStateException illegalStateException = Assertions.
                assertThrows(
                        IllegalStateException.class,
                        () -> managerService.addNewManager(manager));
        Assertions.
                assertEquals(
                        "This manager is in the DB",
                        illegalStateException.getMessage());
    }

    @Test
    void addNewManagerEmailRegisteredException() {
        Manager manager = createManager();

        Mockito.when(managerRepository.findManagerByEmail(manager.getEmail())).thenReturn(Optional.of(manager));

        IllegalStateException illegalStateException = Assertions.
                assertThrows(
                        IllegalStateException.class,
                        () -> managerService.addNewManager(manager));
        Assertions.
                assertEquals(
                        "Manager with this email already registered",
                        illegalStateException.getMessage());
    }

    @Test
    void addNewManagerEmailException() {
        Manager manager = createManager();
        manager.setEmail("email");
        IllegalStateException illegalStateException = Assertions.
                assertThrows(
                        IllegalStateException.class,
                        () -> managerService.addNewManager(manager));
        Assertions.
                assertEquals(
                        "Email is not valid",
                        illegalStateException.getMessage());
    }

    @Test
    void updateManager() {
        Manager manager = createManager();
        ManagerDTO managerDTO = createManagerDto();

        Mockito.when(managerRepository.findManagerByFullNameAndEmail(manager.getFirst_name(), manager.getLast_name(), manager.getEmail())).thenReturn(Optional.of(manager));
        Mockito.when(managerMapper.toDto(manager)).thenReturn(managerDTO);

        managerService.updateManager(manager);

        Mockito.verify(managerRepository, Mockito.times(1)).findManagerByFullNameAndEmail(manager.getFirst_name(), manager.getLast_name(), manager.getEmail());
        Mockito.verify(managerMapper, Mockito.times(1)).toDto(manager);
    }

    @Test
    void updateManagerException() {
        Manager manager = createManager();
        IllegalStateException illegalStateException = Assertions.
                assertThrows(
                        IllegalStateException.class,
                        () -> managerService.updateManager(manager));
        Assertions.
                assertEquals(
                        "Manager with name John Doe doesn't exist in the database",
                        illegalStateException.getMessage());
    }

}