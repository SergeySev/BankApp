package com.example.telproject.controller;

import com.example.telproject.CreateManagerEntity;
import com.example.telproject.dto.ManagerCreateDto;
import com.example.telproject.dto.ManagerDTO;
import com.example.telproject.exception.ManagerRequestException;
import com.example.telproject.service.ManagerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(ManagerController.class)
@DisplayName("ManagerController test class")
@WithMockUser
class ManagerControllerTest {

    @MockBean
    ManagerService managerService;
    @Autowired
    MockMvc mockMvc;

    String name = "John";

    CreateManagerEntity createManager = new CreateManagerEntity();



    @Test
    void getManager() throws Exception {
        List<ManagerDTO> managers = new ArrayList<>();
        ManagerDTO managerDTO = createManager.createManagerDto();
        managers.add(managerDTO);
        Mockito.when(managerService.findManagerByName(name)).thenReturn(managers);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/managers/" + name))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].first_name").value(name))
                .andExpect(jsonPath("$[0].last_name").value(managerDTO.getLast_name()))
                .andExpect(jsonPath("$[0].status").value(managerDTO.getStatus()))
                .andExpect(jsonPath("$[0].email").value(managerDTO.getEmail()))
                .andExpect(jsonPath("$[0].phone_number").value(managerDTO.getPhone_number()))
                .andExpect(jsonPath("$[0].birth_date").value("03.04.2023"))
                .andExpect(jsonPath("$[0].created_at").value("03.04.2023 13:01"))
                .andExpect(jsonPath("$[0].updated_at").value("03.04.2023 13:01"));
        Mockito.verify(managerService, Mockito.times(1)).findManagerByName(name);
    }

    @Test
    void findManagerByNameNotFoundException() throws Exception {
        Mockito.when(managerService.findManagerByName(name)).thenThrow(new ManagerRequestException("Error"));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/managers/" + name))
                .andExpect(status().isBadRequest());
        Mockito.verify(managerService, Mockito.times(1)).findManagerByName(name);
    }

    @Test
    void findManagerById() throws Exception {
        ManagerDTO manager = createManager.createManagerDto();
        Mockito.when(managerService.findById(1L)).thenReturn(manager);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/managers/findById/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.first_name").value(manager.getFirst_name()))
                .andExpect(jsonPath("$.last_name").value(manager.getLast_name()))
                .andExpect(jsonPath("$.status").value(manager.getStatus()))
                .andExpect(jsonPath("$.email").value(manager.getEmail()))
                .andExpect(jsonPath("$.phone_number").value(manager.getPhone_number()))
                .andExpect(jsonPath("$.birth_date").value("03.04.2023"))
                .andExpect(jsonPath("$.created_at").value("03.04.2023 13:01"))
                .andExpect(jsonPath("$.updated_at").value("03.04.2023 13:01"));
        Mockito.verify(managerService, Mockito.times(1)).findById(1L);
    }

    @Test
    void findManagerByIdNotFoundException() throws Exception {
        Mockito.when(managerService.findById(1L)).thenThrow(new ManagerRequestException("Error"));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/managers/findById/1"))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.message").value("Error"));
        Mockito.verify(managerService, Mockito.times(1)).findById(1L);
    }

    @Test
    void addNewManager() throws Exception {
        String managerJson = "{\"firstName\":\"John\",\"lastName\":\"Doe\",\"status\":\"ACTIVE\",\"email\":\"manager@gmail.com\",\"birth_date\":\"1993-02-16T00:00:00.000Z\"}";

        ManagerDTO managerDTO = createManager.createManagerDto();
        Mockito.when(managerService.addNewManager(Mockito.any())).thenReturn(managerDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/managers/registerManager").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(managerJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.first_name").value(managerDTO.getFirst_name()))
                .andExpect(jsonPath("$.last_name").value(managerDTO.getLast_name()))
                .andExpect(jsonPath("$.status").value(managerDTO.getStatus()))
                .andExpect(jsonPath("$.email").value(managerDTO.getEmail()))
                .andExpect(jsonPath("$.phone_number").value(managerDTO.getPhone_number()))
                .andExpect(jsonPath("$.birth_date").value("03.04.2023"))
                .andExpect(jsonPath("$.created_at").value("03.04.2023 13:01"))
                .andExpect(jsonPath("$.updated_at").value("03.04.2023 13:01"));
        Mockito.verify(managerService, Mockito.times(1)).addNewManager(Mockito.any());
    }

    @Test
    void addNewManagerException() throws Exception {
        ManagerCreateDto manager = createManager.createManagerCreateDto();

        Mockito.when(managerService.addNewManager(manager)).thenThrow(new ManagerRequestException("Error"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/managers/registerManager").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(manager)))
                .andExpect(status().isBadRequest());
        Mockito.verify(managerService, Mockito.times(0)).addNewManager(Mockito.any());
    }

    @Test
    void addNewManagerWithoutCSRFForbidden() throws Exception {
        String managerJson = "{\"firstName\":\"John\",\"lastName\":\"Doe\",\"status\":\"ACTIVE\",\"email\":\"manager@gmail.com\",\"birth_date\":\"1993-02-16T00:00:00.000Z\"}";
        ManagerCreateDto manager = createManager.createManagerCreateDto();
        ManagerDTO managerDTO = createManager.createManagerDto();

        Mockito.when(managerService.addNewManager(manager)).thenReturn(managerDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/managers/registerManager")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(managerJson))
                .andExpect(status().isForbidden());
        Mockito.verify(managerService, Mockito.times(0)).addNewManager(Mockito.any());
    }

    @Test
    void update() throws Exception {
        String managerJson = "{\"firstName\":\"John\",\"lastName\":\"Doe\",\"status\":\"ACTIVE\",\"email\":\"manager@gmail.com\",\"birth_date\":\"1993-02-16T00:00:00.000Z\"}";
        ManagerDTO managerDTO = createManager.createManagerDto();

        Mockito.when(managerService.updateManager(Mockito.any())).thenReturn(managerDTO);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/managers/update").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(managerJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.first_name").value(managerDTO.getFirst_name()))
                .andExpect(jsonPath("$.last_name").value(managerDTO.getLast_name()))
                .andExpect(jsonPath("$.status").value(managerDTO.getStatus()))
                .andExpect(jsonPath("$.email").value(managerDTO.getEmail()))
                .andExpect(jsonPath("$.phone_number").value(managerDTO.getPhone_number()))
                .andExpect(jsonPath("$.birth_date").value("03.04.2023"))
                .andExpect(jsonPath("$.created_at").value("03.04.2023 13:01"))
                .andExpect(jsonPath("$.updated_at").value("03.04.2023 13:01"));
        Mockito.verify(managerService, Mockito.times(1)).updateManager(Mockito.any());
    }

    @Test
    void updateWithoutCSRFForbidden() throws Exception {
        String managerJson = "{\"firstName\":\"John\",\"lastName\":\"Doe\",\"status\":\"ACTIVE\",\"email\":\"manager@gmail.com\",\"birth_date\":\"1993-02-16T00:00:00.000Z\"}";
        ManagerDTO managerDTO = createManager.createManagerDto();

        Mockito.when(managerService.updateManager(Mockito.any())).thenReturn(managerDTO);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/managers/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(managerJson))
                .andExpect(status().isForbidden());
        Mockito.verify(managerService, Mockito.times(0)).updateManager(Mockito.any());
    }
}