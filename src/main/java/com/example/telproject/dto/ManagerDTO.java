package com.example.telproject.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Value;

@Value
public class ManagerDTO {

    String firstName;
    String lastName;
    String status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    String createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    String updatedAt;

}
