package com.example.telproject.mapper;

import com.example.telproject.dto.TransactionDTO;
import com.example.telproject.entity.Card;
import com.example.telproject.entity.Transaction;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    TransactionDTO toDto(Transaction transaction);
    List<TransactionDTO> listToDTO(List<TransactionDTO> transactionDTO);

    default String map(Card card) {
        return card.toString();
    }
}
