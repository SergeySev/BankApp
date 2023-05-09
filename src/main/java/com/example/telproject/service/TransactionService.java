package com.example.telproject.service;

import com.example.telproject.dto.TransactionCreateDto;
import com.example.telproject.dto.TransactionDTO;
import com.example.telproject.entity.Account;
import com.example.telproject.entity.Transaction;
import com.example.telproject.entity.enums.AccountStatus;
import com.example.telproject.entity.enums.TransactionType;
import com.example.telproject.mapper.TransactionMapper;
import com.example.telproject.repository.AccountRepository;
import com.example.telproject.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final TransactionMapper transactionMapper;
    private final String ACCOUNT_NOT_FOUND = "Account with %s doesn't exist in the database";

    /**
     * This method performs a cash-in request to the specified account.
     *
     * @param request   an object containing the request details.
     * @return a TransactionDTO object representing the completed request.
     * @throws IllegalStateException if the account is not active or if the specified account is not found in the database.
     */
    @Transactional
    public TransactionDTO cashIn(TransactionCreateDto request) {
        if (request.getAmount().compareTo(BigDecimal.valueOf(0)) <= 0) {
            throw new IllegalStateException("The amount must be a positive integer");
        }
        Account cashToAcc = accountRepository.
                findById(request.getTo_account_id()).
                orElseThrow(() -> new IllegalStateException(String.format(ACCOUNT_NOT_FOUND, request.getTo_account_id())));

        if (!cashToAcc.getStatus().equals(AccountStatus.ACTIVE)) {
            throw new IllegalStateException("Account is not active");
        }

        Transaction transaction = new Transaction(cashToAcc,
                cashToAcc,
                TransactionType.PENDING,
                request.getAmount(),
                request.getDescription());

        // Update the account balance with the request amount
        BigDecimal balance = cashToAcc.getBalance();
        cashToAcc.setBalance(balance.add(request.getAmount()).setScale(2, RoundingMode.HALF_UP));



        // Save the updated account details in the database
        accountRepository.save(cashToAcc);
        transaction.setType(TransactionType.APPROVED);
        transactionRepository.save(transaction);
        return transactionMapper.toDto(transaction);
    }

    /**
     * Transfers a certain amount of money between two accounts and saves the transaction in the database.
     *
     * @param request     the transaction object containing the amount to be transferred and other transaction details
     * @return the transaction details saved in the database as TransactionDTO
     * @throws IllegalStateException if either account is not active OR if the specified account is not found in the database, OR if there is not enough money in the "from" account to complete the transfer
     */
    @Transactional
    public TransactionDTO cashBetweenAccounts(TransactionCreateDto request) {

        Account toAccount = accountRepository.
                findById(request.getTo_account_id()).
                orElseThrow(() -> new IllegalStateException(String.
                        format(ACCOUNT_NOT_FOUND, request.getTo_account_id())));

        Account fromAccount = accountRepository.
                findById(request.getFrom_account_id()).
                orElseThrow(() -> new IllegalStateException(String.
                        format(ACCOUNT_NOT_FOUND, request.getFrom_account_id())));

        if (!toAccount.getStatus().equals(AccountStatus.ACTIVE) || !fromAccount.getStatus().equals(AccountStatus.ACTIVE)) {
            throw new IllegalStateException("Account is not active");
        }

        request.setAmount(request.getAmount().setScale(2, RoundingMode.HALF_UP));

        BigDecimal balanceFromAcc = fromAccount.getBalance();
        BigDecimal balanceToAcc = toAccount.getBalance();

        //Check if there is enough money in the account to complete the transfer
        if (request.getAmount().compareTo(balanceFromAcc) > 0) {
            throw new IllegalStateException("Not enough many");
        }

        //Update balances of the accounts involved
        toAccount.setBalance(balanceToAcc.add(request.getAmount()).setScale(2, RoundingMode.HALF_UP));
        fromAccount.setBalance(balanceFromAcc.subtract(request.getAmount()).setScale(2, RoundingMode.HALF_UP));

        //Update accounts details in the database
        accountRepository.saveAll(List.of(toAccount, fromAccount));

        //Set transaction details
        Transaction transaction = new Transaction(fromAccount, toAccount, TransactionType.APPROVED, request.getAmount(), request.getDescription());


        transactionRepository.save(transaction);
        return transactionMapper.toDto(transaction);
    }
}
