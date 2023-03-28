package com.example.telproject.dto;

import lombok.Value;

@Value
public class AccountDTO {
    String name;
    String type;
    String status;
    String balance;
    String currency_code;

}
