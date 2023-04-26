package com.example.telproject.controller;

import com.example.telproject.dto.AccountDTO;
import com.example.telproject.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping(path = "/accounts_by_product")
    public List<AccountDTO> getAccountByProductId(@RequestParam Long product_id) {
        System.out.println("\n\n\nProduct id in  controller: " + product_id + "\n");
        return accountService.getAccountByProductId(product_id);
    }
}
