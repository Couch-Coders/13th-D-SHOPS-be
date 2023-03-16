package com.example.demo.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDTO {
    Long userEntryNo;
    String email;
    LocalDateTime registeredDate;
}
