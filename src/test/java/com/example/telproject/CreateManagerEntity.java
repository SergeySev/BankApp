package com.example.telproject;

import com.example.telproject.dto.ManagerCreateDto;
import com.example.telproject.dto.ManagerDTO;
import com.example.telproject.entity.Manager;
import com.example.telproject.entity.enums.ManagerStatus;

import java.sql.Timestamp;

public class CreateManagerEntity {
    String name = "John";

    public Manager createManager() {
        Manager manager = new Manager();
        manager.setId(1L);
        manager.setFirst_name("John");
        manager.setLast_name("Doe");
        manager.setStatus(ManagerStatus.ACTIVE);
        manager.setEmail("manager@gmail.com");
        manager.setBirth_date(Timestamp.valueOf("2023-04-03 13:01:36.968"));
        manager.setCreated_at(Timestamp.valueOf("2023-04-03 13:01:36.968"));
        manager.setUpdated_at(Timestamp.valueOf("2023-04-03 13:01:36.968"));
        manager.setPhone_number("123123123");
        return manager;
    }

    public ManagerDTO createManagerDto() {
        return new ManagerDTO(name,
                "Doe",
                ManagerStatus.ACTIVE.getValue(),
                "manager@gmail.com",
                "123123123",
                Timestamp.valueOf("2023-04-03 13:01:36.968").toLocalDateTime(),
                Timestamp.valueOf("2023-04-03 13:01:36.968").toLocalDateTime(),
                Timestamp.valueOf("2023-04-03 13:01:36.968").toLocalDateTime());
    }
    public ManagerCreateDto createManagerCreateDto() {
        return new ManagerCreateDto(name,
                "Doe",
                "ACTIVE",
                "manager@gmail.com",
                "123123123",
                Timestamp.valueOf("2023-04-03 13:01:36.968"));
    }
}
