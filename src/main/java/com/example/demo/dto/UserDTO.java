package com.example.demo.dto;

import com.example.demo.entity.BaseEntiry;
import com.example.demo.entity.Company;
import com.example.demo.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDTO extends BaseEntiryDTO {
    String email;
    String name;
    String phone;
    Company company;

    @Builder
    public UserDTO(User user){
        this.seq = user.getSeq();
        this.email = user.getEmail();
        this.name = user.getName();
        this.phone = user.getPhone();
        this.company = user.getCompany();
    }
}
