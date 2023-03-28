package com.example.telproject.service;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class RequestClient {
    private final Long manager_id;
    private final String first_name;
    private final String last_name;
    private final String email;
    private final Timestamp birth_date;
    private final String password;

}
