package com.example.telproject.controller;

import com.example.telproject.dto.AccountDTO;
import com.example.telproject.service.AccountService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    /**

     Returns a list of AccountDTO objects associated with the provided product_id.
     The product_id is retrieved from the request parameters.
     @param product_id the ID of the product for which accounts are to be retrieved.
     @return a List of AccountDTO objects associated with the provided product_id.
     @throws IllegalStateException if no accounts are found for the provided product_id.
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval"),
            @ApiResponse(responseCode = "400", description = "Account doesn't exist"),
    })
    @GetMapping(path = "/accounts_by_product")
    public List<AccountDTO> getAccountByProductId(@RequestParam Long product_id) {
        return accountService.getAccountByProductId(product_id);
    }
}
