package com.example.telproject.service;

import com.example.telproject.CreateManagerEntity;
import com.example.telproject.dto.ManagerCreateDto;
import com.example.telproject.dto.ManagerDTO;
import com.example.telproject.entity.Manager;
import com.example.telproject.exception.ManagerRequestException;
import com.example.telproject.mapper.ManagerMapper;
import com.example.telproject.mapper.ManagerMapperImpl;
import com.example.telproject.repository.ManagerRepository;
import com.example.telproject.security.CheckingEmail;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class ManagerServiceTest {

    CheckingEmail checkingEmail = new CheckingEmail();
    ManagerRepository managerRepository = Mockito.mock(ManagerRepository.class);
    ManagerMapper managerMapper = new ManagerMapperImpl();
    ManagerService managerService = new ManagerService(managerRepository, managerMapper, checkingEmail);
    String name = "John";

    CreateManagerEntity createManager = new CreateManagerEntity();

    @Test
    void findManagerByName() {
        Manager manager = createManager.createManager();
        List<Manager> managers = new ArrayList<>();
        managers.add(manager);

        ManagerDTO managerDTO = createManager.createManagerDto();
        List<ManagerDTO> expected = new ArrayList<>();
        expected.add(managerDTO);

        Mockito.when(managerRepository.findManagerByFirstName(name)).thenReturn(managers);

        List<ManagerDTO> actual = managerService.findManagerByName(name);

        Mockito.verify(managerRepository, Mockito.times(1)).findManagerByFirstName(name);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void findByNameException() {
        ManagerRequestException managerRequestException = Assertions.
                assertThrows(
                        ManagerRequestException.class,
                        () -> managerService.findManagerByName(name));
        Assertions.
                assertEquals(
                        "Manager with name John doesn't exist in the database",
                        managerRequestException.getMessage());
        Mockito.verify(managerRepository, Mockito.times(1)).findManagerByFirstName(name);

    }

    @Test
    void findManagerById() {
        Optional<Manager> manager = Optional.ofNullable(createManager.createManager());
        ManagerDTO expected = createManager.createManagerDto();
        Long id = 1L;

        Mockito.when(managerRepository.findById(id)).thenReturn(manager);

        ManagerDTO actual = managerService.findById(id);
        Mockito.verify(managerRepository, Mockito.times(1)).findById(id);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void findManagerByIdException() {
        Long id = 1L;

        ManagerRequestException managerRequestException = Assertions.
                assertThrows(
                        ManagerRequestException.class,
                        () -> managerService.findById(id));
        Assertions.
                assertEquals(
                        "Manager with id 1 doesn't exist in the database",
                        managerRequestException.getMessage());
        Mockito.verify(managerRepository, Mockito.times(1)).findById(id);
    }

    @Test
    void addNewManager() {
        ManagerCreateDto manager = createManager.createManagerCreateDto();

        ManagerDTO expected = createManager.createManagerDto();

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
        ManagerCreateDto manager = createManager.createManagerCreateDto();
        Optional<Manager> managerOptional = Optional.ofNullable(createManager.createManager());

        Mockito.when(managerRepository.
                findManagerByFullNameAndEmail(
                        manager.getFirst_name(),
                        manager.getLast_name(),
                        manager.getEmail()
                )).thenReturn(managerOptional);

        ManagerRequestException managerRequestException = Assertions.
                assertThrows(
                        ManagerRequestException.class,
                        () -> managerService.addNewManager(manager));
        Assertions.
                assertEquals(
                        "This manager is in the DB",
                        managerRequestException.getMessage());
        Mockito.verify(managerRepository, Mockito.times(1)).
                findManagerByFullNameAndEmail(
                        manager.getFirst_name(),
                        manager.getLast_name(),
                        manager.getEmail());
    }

    @Test
    void addNewManagerEmailRegisteredException() {
        ManagerCreateDto manager = createManager.createManagerCreateDto();
        Manager managerReturn = createManager.createManager();

        Mockito.when(managerRepository.findManagerByEmail(manager.getEmail())).thenReturn(Optional.of(managerReturn));

        ManagerRequestException managerRequestException = Assertions.
                assertThrows(
                        ManagerRequestException.class,
                        () -> managerService.addNewManager(manager));
        Assertions.
                assertEquals(
                        "Manager with this email already registered",
                        managerRequestException.getMessage());
        Mockito.verify(managerRepository, Mockito.times(1)).findManagerByEmail(manager.getEmail());

    }

    @Test
    void addNewManagerEmailException() {
        ManagerCreateDto manager = createManager.createManagerCreateDto();
        manager.setEmail("email");
        ManagerRequestException managerRequestException = Assertions.
                assertThrows(
                        ManagerRequestException.class,
                        () -> managerService.addNewManager(manager));
        Assertions.
                assertEquals(
                        "Email is not valid",
                        managerRequestException.getMessage());
    }

    @Test
    void updateManager() {
        ManagerCreateDto manager = createManager.createManagerCreateDto();
        ManagerDTO expected = createManager.createManagerDto();
        Manager managerForSearch = createManager.createManager();

        Mockito.when(managerRepository.findManagerByFullNameAndEmail(manager.getFirst_name(),
                manager.getLast_name(),
                manager.getEmail())).thenReturn(Optional.of(managerForSearch));

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
        ManagerCreateDto manager = createManager.createManagerCreateDto();
        ManagerRequestException managerRequestException = Assertions.
                assertThrows(
                        ManagerRequestException.class,
                        () -> managerService.updateManager(manager));
        Assertions.
                assertEquals(
                        "Manager with name John Doe doesn't exist in the database",
                        managerRequestException.getMessage());
        Mockito.verify(managerRepository, Mockito.times(1)).
                findManagerByFullNameAndEmail(
                        manager.getFirst_name(),
                        manager.getLast_name(),
                        manager.getEmail());
    }

}