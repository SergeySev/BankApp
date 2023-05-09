package com.example.telproject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
@Getter
@Setter
@AllArgsConstructor
public class ClientCreateDto {
    Long manager_id;
    String first_name;
    String last_name;
    String email;
    String password;
    Timestamp birth_date;
}
