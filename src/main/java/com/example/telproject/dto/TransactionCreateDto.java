package com.example.telproject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class TransactionCreateDto {
    BigDecimal amount;
    Long to_card_id;
    Long from_card_id;
    String type;
    String description;
}
