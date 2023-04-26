package com.example.telproject.dto;

import lombok.Value;

@Value
public class AccountDTO {
    String card_number;
    String csv;
    String type;
    String status;
    String balance;
    String currency_code;

}
