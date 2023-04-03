package com.example.telproject.controller;

import com.example.telproject.dto.ManagerDTO;
import com.example.telproject.entity.Manager;
import com.example.telproject.entity.enums.ManagerStatus;
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

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(ManagerController.class)
@DisplayName("ManagerController test class")
@WithMockUser
class ManagerControllerTest {

    @Autowired
    ManagerController managerController;

    @MockBean
    ManagerService managerService;

    @Autowired
    MockMvc mockMvc;

    Manager createManager() {
        Manager manager = new Manager();
        manager.setId(1L);
        manager.setFirst_name("John");
        manager.setLast_name("Doe");
        manager.setStatus(ManagerStatus.ACTIVE);
        manager.setEmail("manager@gmail.com");
        manager.setBirth_date(Timestamp.valueOf(LocalDateTime.now()));
        manager.setCreated_at(Timestamp.valueOf(LocalDateTime.now()));
        manager.setUpdated_at(Timestamp.valueOf(LocalDateTime.now()));
        return manager;
    }

    ManagerDTO createManagerDto() {
        return new ManagerDTO("John",
                "Doe",
                ManagerStatus.ACTIVE.getValue(),
                "manager@gmail.com",
                "123123123",
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now());
    }

    private void testEndPoint(String path) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(path))
                .andExpect(status().isOk());
    }

    @Test
    void getManager() throws Exception {
        List<ManagerDTO> managers = new ArrayList<>();
        Mockito.when(managerService.findManagerByName("John")).thenReturn(managers);
        testEndPoint("/api/v1/managers/John");
    }

    @Test
    void findManagerById() throws Exception {
        ManagerDTO manager = createManagerDto();
        Mockito.when(managerService.findById(1L)).thenReturn(manager);
        testEndPoint("/api/v1/managers/findById/1");
    }

    @Test
    void addNewManager() throws Exception {
        String managerJson = "{\"firstName\":\"John\",\"lastName\":\"Doe\",\"status\":\"ACTIVE\",\"email\":\"manager@gmail.com\",\"birth_date\":\"1993-02-16T00:00:00.000Z\"}";
        Manager manager = createManager();
        ManagerDTO managerDTO = createManagerDto();

        Mockito.when(managerService.addNewManager(manager)).thenReturn(managerDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/managers/registerManager").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(managerJson))
                .andExpect(status().isOk());
    }

    @Test
    void update() throws Exception {
        String managerJson = "{\"firstName\":\"John\",\"lastName\":\"Doe\",\"status\":\"ACTIVE\",\"email\":\"manager@gmail.com\",\"birth_date\":\"1993-02-16T00:00:00.000Z\"}";
        Manager manager = createManager();
        ManagerDTO managerDTO = createManagerDto();

        Mockito.when(managerService.updateManager(manager)).thenReturn(managerDTO);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/managers/update").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(managerJson))
                .andExpect(status().isOk());
    }
}