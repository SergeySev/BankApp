package com.example.telproject.service;

import com.example.telproject.dto.AccountDTO;
import com.example.telproject.entity.Account;
import com.example.telproject.mapper.AccountMapper;
import com.example.telproject.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final String ACCOUNT_NOT_FOUND = "Account with %s: %s doesn't exist in the DataBase";


    public List<AccountDTO> getAccountByProductId(Long productId) {
        System.out.println("\n\nProduct id in  service " + productId);
        List<Account>  accounts = accountRepository.findByProductId(productId);
        if (accounts.isEmpty()) {
            throw new RuntimeException(String.format(ACCOUNT_NOT_FOUND, "Product id", productId));
        }
        return accountMapper.toDtoList(accounts);
    }
}
