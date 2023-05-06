package com.example.telproject.service;

import com.example.telproject.dto.AccountDTO;
import com.example.telproject.entity.Account;
import com.example.telproject.entity.Client;
import com.example.telproject.entity.enums.CurrencyType;
import com.example.telproject.mapper.AccountMapper;
import com.example.telproject.mapper.AccountMapperImpl;
import com.example.telproject.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AccountServiceTest {

    AccountRepository accountRepository = Mockito.mock(AccountRepository.class);
    AccountMapper accountMapper = new AccountMapperImpl();
    AccountService accountService = new AccountService(accountRepository, accountMapper);

    List<AccountDTO> createAccountsDTO() {
        List<AccountDTO> accountDTOList = new ArrayList<>(2);

        Client client = Mockito.mock(Client.class);

        Account account1 = new Account(client, CurrencyType.EUR);
        Account account2 = new Account(client, CurrencyType.UAH);

        account1.setCard_number("0000000000000000");
        account1.setCsv("000");

        account2.setCard_number("0000000000000000");
        account2.setCsv("000");

        accountDTOList.add(accountMapper.toDto(account1));
        accountDTOList.add(accountMapper.toDto(account2));
        return accountDTOList;
    }

    List<Account> createAccounts() {
        List<Account> accountList = new ArrayList<>(2);

        Client client = Mockito.mock(Client.class);

        Account account1 = new Account(client, CurrencyType.EUR);
        Account account2 = new Account(client, CurrencyType.UAH);

        account1.setCard_number("0000000000000000");
        account1.setCsv("000");

        account2.setCard_number("0000000000000000");
        account2.setCsv("000");

        accountList.add(account1);
        accountList.add(account2);
        return accountList;
    }

    @Test
    void getAccountByProductId() {
        List<AccountDTO> expected = createAccountsDTO();
        List<Account> accountList = createAccounts();

        Mockito.when(accountRepository.findByProductId(1L)).thenReturn(accountList);
        List<AccountDTO> result = accountService.getAccountByProductId(1L);

        Mockito.verify(accountRepository, Mockito.times(1)).findByProductId(1L);
        assertEquals(expected, result);
    }


//    @Test
//    void getAccountByProductId_whenAccountListIsEmpty() {
//        List<Account> accountList = new ArrayList<>();
//
//        RuntimeException runtimeException = Assertions.assertThrows(
//                RuntimeException.class,
//                () -> {accountService.getAccountByProductId(1L);});
//
//        Mockito.when(accountRepository.findByProductId(1L)).thenReturn(accountList);
//        assertEquals("Account with Product id: 1 doesn't exist in the DataBase", runtimeException.getMessage());
//
//        Mockito.verify(accountRepository, Mockito.times(1)).findByProductId(1L);
//    }
}