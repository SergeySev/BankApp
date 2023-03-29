package com.example.telproject.service;

import com.example.telproject.dto.AccountDTO;
import com.example.telproject.mapper.AccountMapper;
import com.example.telproject.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final String ACCOUNT_NOT_FOUND = "Account with name: %s doesn't exist in the DataBase";

    public AccountDTO findByName(String name) {
        return accountMapper.
                toDto(accountRepository.
                        findAccountByName(name).
                        orElseThrow(() ->
                                new IllegalStateException(String.format(ACCOUNT_NOT_FOUND, name))));
    }
}
