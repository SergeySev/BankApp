package com.example.telproject.controller;

import com.example.telproject.dto.ManagerDTO;
import com.example.telproject.entity.Manager;
import com.example.telproject.service.ManagerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(path = "api/v1/managers")
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerService managerService;

    @GetMapping(path = "{name}")
    public List<ManagerDTO> getManager(@PathVariable("name") String name) {
        return managerService.findManagerByName(name);
    }

    @GetMapping(path = "/findById/{id}")
    public ManagerDTO findManagerById(@PathVariable("id") Long id) {
        return managerService.findById(id);
    }

    @PostMapping(path = "/registerManager")
    public ManagerDTO addNewManager(@RequestBody Manager manager) {
        return managerService.addNewManager(manager);
    }

    @PutMapping(path = "/update")
    @Transactional
    public ManagerDTO update(@RequestBody Manager manager) {
        return managerService.updateManager(manager);
    }

}
