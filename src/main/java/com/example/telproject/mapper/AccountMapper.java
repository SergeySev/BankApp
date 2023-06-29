package com.example.telproject.mapper;

import com.example.telproject.dto.CardDTO;
import com.example.telproject.entity.Card;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    CardDTO toDto(Card card);

    List<CardDTO> toDtoList(List<Card> cards);
}
