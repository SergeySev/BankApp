package com.example.telproject.mapper;

import com.example.telproject.dto.AccountDTO;
import com.example.telproject.entity.Account;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountDTO toDto(Account account);

    List<AccountDTO> toDtoList(List<Account> accounts);
}
