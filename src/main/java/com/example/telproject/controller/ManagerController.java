package com.example.telproject.controller;

import com.example.telproject.dto.ManagerDTO;
import com.example.telproject.entity.Manager;
import com.example.telproject.service.ManagerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping(path = "api/v1/managers")
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerService managerService;

    @GetMapping(path = "{name}")
    public ManagerDTO getManager(@PathVariable("name") String name) {
        return managerService.findManagerByName(name);
    }

    @GetMapping(path = "/findById/{id}")
    public ManagerDTO findAllManagers(@PathVariable("id") Long id) {
        return managerService.findById(id);
    }

    @PostMapping(path = "/registerManager")
    public String addNewManager(@RequestBody Manager manager) {
        return managerService.addNewManager(manager);
    }

    @PutMapping(path = "/update")
    @Transactional
    public String update(@RequestBody Manager manager) {
        return managerService.updateManager(manager.getFirst_name(), manager.getLast_name(), manager.getBirth_date());
    }

}
