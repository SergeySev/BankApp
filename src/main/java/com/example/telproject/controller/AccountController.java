package com.example.telproject.controller;

import com.example.telproject.dto.AccountDTO;
import com.example.telproject.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping(path = "{name}")
    public AccountDTO findByName(@PathVariable("name") String name) {
        return accountService.findByName(name);
    }

}
