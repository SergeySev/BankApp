package com.example.telproject.service;

import com.example.telproject.dto.ManagerDTO;
import com.example.telproject.entity.Manager;
import com.example.telproject.entity.enums.ManagerStatus;
import com.example.telproject.exception.ManagerRequestException;
import com.example.telproject.mapper.ManagerMapper;
import com.example.telproject.mapper.ManagerMapperImpl;
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
    ManagerMapper managerMapper = new ManagerMapperImpl();
    ManagerService managerService = new ManagerService(managerRepository, managerMapper, checkingEmail);
    String name = "John";

    Manager createManager() {
        Manager manager = new Manager();
        manager.setId(1L);
        manager.setFirst_name("John");
        manager.setLast_name("Doe");
        manager.setStatus(ManagerStatus.ACTIVE);
        manager.setEmail("manager@gmail.com");
        manager.setPhone_number("123123123");
        manager.setBirth_date(Timestamp.valueOf("2023-04-03 13:01:36.968"));
        manager.setCreated_at(Timestamp.valueOf("2023-04-03 13:01:36.968"));
        manager.setUpdated_at(Timestamp.valueOf("2023-04-03 13:01:36.968"));
        return manager;
    }

    ManagerDTO createManagerDto() {
        Manager manager = createManager();
        return new ManagerDTO(manager.getFirst_name(),
                manager.getLast_name(),
                manager.getStatus().getValue(),
                manager.getEmail(),
                manager.getPhone_number(),
                Timestamp.valueOf("2023-04-03 13:01:36.968").toLocalDateTime(),
                Timestamp.valueOf("2023-04-03 13:01:36.968").toLocalDateTime(),
                Timestamp.valueOf("2023-04-03 13:01:36.968").toLocalDateTime());
    }

    @Test
    void findManagerByName() {
        Manager manager = createManager();
        List<Manager> managers = new ArrayList<>();
        managers.add(manager);

        ManagerDTO managerDTO = createManagerDto();
        List<ManagerDTO> expected = new ArrayList<>();
        expected.add(managerDTO);

        Mockito.when(managerRepository.findManagerByFirstName(name)).thenReturn(managers);

        List<ManagerDTO> actual = managerService.findManagerByName(name);

        Mockito.verify(managerRepository, Mockito.times(1)).findManagerByFirstName(name);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void findByNameException() {
        String name = "John";

        ManagerRequestException illegalStateException = Assertions.
                assertThrows(
                        ManagerRequestException.class,
                        () -> managerService.findManagerByName(name));
        Assertions.
                assertEquals(
                        "Manager with name John doesn't exist in the database",
                        illegalStateException.getMessage());
    }

    @Test
    void findManagerById() {
        Optional<Manager> manager = Optional.ofNullable(createManager());
        ManagerDTO expected = createManagerDto();
        Long id = 1L;

        Mockito.when(managerRepository.findById(id)).thenReturn(manager);

        ManagerDTO actual = managerService.findById(id);
        Mockito.verify(managerRepository, Mockito.times(1)).findById(id);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void findManagerByIdException() {
        Long id = 1L;

        ManagerRequestException illegalStateException = Assertions.
                assertThrows(
                        ManagerRequestException.class,
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

        ManagerDTO expected = createManagerDto();

        ManagerDTO actual = managerService.addNewManager(manager);
        Mockito.verify(managerRepository, Mockito.times(1)).findManagerByFullNameAndEmail(manager.getFirst_name(), manager.getLast_name(), manager.getEmail());
        Mockito.verify(managerRepository, Mockito.times(1)).findManagerByEmail(manager.getEmail());
        Mockito.verify(managerRepository, Mockito.times(1)).save(Mockito.any());

        Assertions.assertEquals(expected.getFirst_name(), actual.getFirst_name());
        Assertions.assertEquals(expected.getLast_name(), actual.getLast_name());
        Assertions.assertEquals(expected.getEmail(), actual.getEmail());
        Assertions.assertEquals(expected.getStatus(), actual.getStatus());
        Assertions.assertEquals(expected.getPhone_number(), actual.getPhone_number());
        Assertions.assertNotEquals(expected.getCreated_at(), actual.getCreated_at());
        Assertions.assertNotEquals(expected.getUpdated_at(), actual.getUpdated_at());
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

        ManagerRequestException illegalStateException = Assertions.
                assertThrows(
                        ManagerRequestException.class,
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

        ManagerRequestException illegalStateException = Assertions.
                assertThrows(
                        ManagerRequestException.class,
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
        ManagerRequestException illegalStateException = Assertions.
                assertThrows(
                        ManagerRequestException.class,
                        () -> managerService.addNewManager(manager));
        Assertions.
                assertEquals(
                        "Email is not valid",
                        illegalStateException.getMessage());
    }

    @Test
    void updateManager() {
        Manager manager = createManager();
        ManagerDTO expected = createManagerDto();

        Mockito.when(managerRepository.findManagerByFullNameAndEmail(manager.getFirst_name(), manager.getLast_name(), manager.getEmail())).thenReturn(Optional.of(manager));

        ManagerDTO actual = managerService.updateManager(manager);

        Mockito.verify(managerRepository, Mockito.times(1)).findManagerByFullNameAndEmail(manager.getFirst_name(), manager.getLast_name(), manager.getEmail());
        Assertions.assertEquals(expected.getFirst_name(), actual.getFirst_name());
        Assertions.assertEquals(expected.getLast_name(), actual.getLast_name());
        Assertions.assertEquals(expected.getEmail(), actual.getEmail());
        Assertions.assertEquals(expected.getStatus(), actual.getStatus());
        Assertions.assertEquals(expected.getPhone_number(), actual.getPhone_number());
        Assertions.assertEquals(expected.getCreated_at(), actual.getCreated_at());
        Assertions.assertNotEquals(expected.getUpdated_at(), actual.getUpdated_at());
    }

    @Test
    void updateManagerException() {
        Manager manager = createManager();
        ManagerRequestException illegalStateException = Assertions.
                assertThrows(
                        ManagerRequestException.class,
                        () -> managerService.updateManager(manager));
        Assertions.
                assertEquals(
                        "Manager with name John Doe doesn't exist in the database",
                        illegalStateException.getMessage());
    }

}