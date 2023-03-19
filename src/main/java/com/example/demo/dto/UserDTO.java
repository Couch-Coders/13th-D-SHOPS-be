package com.example.demo.dto;

import com.example.demo.entity.Company;
import com.example.demo.entity.User;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDTO {
    Long seq;
    String email;
    String name;
    String phone;
    LocalDateTime registeredDate;
    Company company;

    public UserDTO(User user){
        this.seq = user.getSeq();
        this.email = user.getEmail();
        this.name = user.getName();
        this.phone = user.getPhone();
        this.company = user.getCompany();
    }
}
