package com.example.telproject.service;

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
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final TransactionMapper transactionMapper;
    private final String ACCOUNT_NOT_FOUND = "Account with %s doesn't exist in the database";

    /**
     * This method performs a cash-in transaction to the specified account.
     *
     * @param transaction   an object containing the transaction details.
     * @param to_account_id the ID of the account to which the cash-in is being made.
     * @return a TransactionDTO object representing the completed transaction.
     * @throws IllegalStateException if the account is not active or if the specified account is not found in the database.
     */
    @Transactional
    public TransactionDTO cashIn(Transaction transaction, Long to_account_id) {
        Account cashToAcc = accountRepository.
                findById(to_account_id).
                orElseThrow(() -> new IllegalStateException(String.format(ACCOUNT_NOT_FOUND, to_account_id)));

        if (!cashToAcc.getStatus().equals(AccountStatus.ACTIVE)) {
            throw new IllegalStateException("Account is not active");
        }

        // Scale the transaction amount to two decimal places
        transaction.setAmount(transaction.getAmount().setScale(2, RoundingMode.HALF_UP));

        // Update the account balance with the transaction amount
        BigDecimal balance = cashToAcc.getBalance();
        cashToAcc.setBalance(balance.add(transaction.getAmount()).setScale(2, RoundingMode.HALF_UP));

        transaction.setType(TransactionType.PENDING);

        // Save the updated account details in the database
        accountRepository.save(cashToAcc);

        // Set transaction details and save the transaction in the database
        transaction.setCreated_at(Timestamp.valueOf(LocalDateTime.now()));
        transaction.setType(TransactionType.APPROVED);
        transaction.setTo_account_id(cashToAcc);
        transaction.setFrom_account_id(cashToAcc);
        transactionRepository.save(transaction);
        return transactionMapper.toDto(transaction);
    }

    /**
     * Transfers a certain amount of money between two accounts and saves the transaction in the database.
     *
     * @param transaction     the transaction object containing the amount to be transferred and other transaction details
     * @param from_account_id the ID of the account to transfer money from
     * @param to_account_id   the ID of the account to transfer money to
     * @return the transaction details saved in the database as TransactionDTO
     * @throws IllegalStateException if either account is not active OR if the specified account is not found in the database, OR if there is not enough money in the "from" account to complete the transfer
     */
    @Transactional
    public TransactionDTO cashBetweenAccounts(Transaction transaction, Long from_account_id, Long to_account_id) {

        Account toAccount = accountRepository.
                findById(to_account_id).
                orElseThrow(() -> new IllegalStateException(String.
                        format(ACCOUNT_NOT_FOUND, to_account_id)));

        Account fromAccount = accountRepository.
                findById(from_account_id).
                orElseThrow(() -> new IllegalStateException(String.
                        format(ACCOUNT_NOT_FOUND, from_account_id)));

        if (!toAccount.getStatus().equals(AccountStatus.ACTIVE) || !fromAccount.getStatus().equals(AccountStatus.ACTIVE)) {
            throw new IllegalStateException("Account is not active");
        }

        transaction.setAmount(transaction.getAmount().setScale(2, RoundingMode.HALF_UP));

        BigDecimal balanceFromAcc = fromAccount.getBalance();
        BigDecimal balanceToAcc = toAccount.getBalance();

        //Check if there is enough money in the account to complete the transfer
        if (transaction.getAmount().compareTo(balanceFromAcc) > 0) {
            throw new IllegalStateException("Not enough many");
        }

        //Update balances of the accounts involved
        toAccount.setBalance(balanceToAcc.add(transaction.getAmount()).setScale(2, RoundingMode.HALF_UP));
        fromAccount.setBalance(balanceFromAcc.subtract(transaction.getAmount()).setScale(2, RoundingMode.HALF_UP));
        transaction.setType(TransactionType.PENDING);

        //Update accounts details in the database
        accountRepository.saveAll(List.of(toAccount, fromAccount));

        //Set transaction details
        transaction.setCreated_at(Timestamp.valueOf(LocalDateTime.now()));
        transaction.setType(TransactionType.APPROVED);
        transaction.setFrom_account_id(fromAccount);
        transaction.setTo_account_id(toAccount);

        transactionRepository.save(transaction);
        return transactionMapper.toDto(transaction);
    }
}
