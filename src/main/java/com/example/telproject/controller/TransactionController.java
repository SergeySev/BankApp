package com.example.telproject.controller;

import com.example.telproject.dto.TransactionCreateDto;
import com.example.telproject.dto.TransactionDTO;
import com.example.telproject.service.TransactionService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/transaction")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    /**
     * API endpoint to create a transaction for cash-in operation.
     *
     * @param transaction   Transaction object contains the transaction details.
     * @return TransactionDTO object which contains the details of the transaction after the cash-in operation.
     * @throws IllegalStateException If the specified account is not found in the database or not active, or if the amount to be credited is greater than the balance of the specified account.
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = """
                    Successful transaction. Use this json: {
                        "to_account_id": 1,
                        "amount": 100,
                        "type": "NEW",
                        "description": "100"
                    }"""),
            @ApiResponse(responseCode = "400", description = "The account is not active or if the specified account is not found in the database"),
    })
    @PostMapping(path = "/cash-in")
    public TransactionDTO cashIn(@RequestBody TransactionCreateDto transaction) {
        return transactionService.cashIn(transaction);
    }

    /**

     Handles HTTP POST requests to transfer money between two accounts.
     @param transaction the Transaction object containing transaction details
     @return the TransactionDTO object representing the completed transaction
     @throws IllegalStateException if the specified account is not found in the database or is not active, or if there is not enough money
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = """
                    Successful transaction. Use this json: {
                        "from_account_id": 1,
                        "to_account_id": 2,
                        "amount": 100,
                        "type": "NEW",
                        "description": "100"
                    }"""),
            @ApiResponse(responseCode = "400", description = "The account is not active or if the specified account is not found in the database"),
            @ApiResponse(responseCode = "400", description = "Not enough money"),
    })
    @PostMapping(path = "/betweenAcc")
    public TransactionDTO betweenAcc(@RequestBody TransactionCreateDto transaction) {
        return transactionService.cashBetweenAccounts(transaction);
    }
}
