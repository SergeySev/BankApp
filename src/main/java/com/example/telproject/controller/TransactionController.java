package com.example.telproject.controller;

import com.example.telproject.dto.TransactionDTO;
import com.example.telproject.entity.Transaction;
import com.example.telproject.service.TransactionService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/transaktion")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    /**
     * API endpoint to create a transaction for cash-in operation.
     *
     * @param transaction   Transaction object contains the transaction details.
     * @param to_account_id Long value represents the ID of the account to which the amount is credited.
     * @return TransactionDTO object which contains the details of the transaction after the cash-in operation.
     * @throws IllegalStateException If the specified account is not found in the database or not active, or if the amount to be credited is greater than the balance of the specified account.
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful transaction"),
            @ApiResponse(responseCode = "400", description = "The account is not active or if the specified account is not found in the database"),
    })
    @PostMapping(path = "/cash-in")
    public TransactionDTO cashIn(@RequestBody Transaction transaction,
                                 @RequestParam Long to_account_id) {
        return transactionService.cashIn(transaction, to_account_id);
    }

    /**

     Handles HTTP POST requests to transfer money between two accounts.
     @param transaction the Transaction object containing transaction details
     @param from_account_id the ID of the account to transfer money from
     @param to_account_id the ID of the account to transfer money to
     @return the TransactionDTO object representing the completed transaction
     @throws IllegalStateException if the specified account is not found in the database or is not active, or if there is not enough money
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful transaction"),
            @ApiResponse(responseCode = "400", description = "The account is not active or if the specified account is not found in the database"),
            @ApiResponse(responseCode = "400", description = "Not enough money"),
    })
    @PostMapping(path = "/betweenAcc")
    public TransactionDTO betweenAcc(@RequestBody Transaction transaction,
                                     @RequestParam Long from_account_id,
                                     @RequestParam Long to_account_id) {
        return transactionService.cashBetweenAccounts(transaction, from_account_id, to_account_id);
    }
}
