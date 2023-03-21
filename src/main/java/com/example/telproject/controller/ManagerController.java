package com.example.telproject.controller;

import com.example.telproject.entity.Manager;
import com.example.telproject.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/managers")
public class ManagerController {

    private final ManagerService managerService;

    @Autowired
    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }

    @GetMapping
    public List<Manager> showManagers() {
        return managerService.getManagers();
    }

    @GetMapping(path = "{findManager}")
    public Optional<Manager> showManagers(@PathVariable("findManager") String name) {
        return managerService.findManagerByName(name);
    }

    @PostMapping(path = "/registerManager")
    public String addNewManager(@RequestBody Manager manager) {
        return managerService.addNewManager(manager);
    }

    @PutMapping(path = "/update")
    @Transactional
    public String update(@RequestParam("firstName") String firstName,
                         @RequestParam("lastName") String lastName) {
        return managerService.updateManager(firstName, lastName);
    }

}
