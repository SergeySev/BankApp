package com.example.telproject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
@Getter
@Setter
@AllArgsConstructor
public class ManagerCreateDto {
    String first_name;
    String last_name;
    String status;
    String email;
    String phone_number;
    Timestamp birth_date;
}
