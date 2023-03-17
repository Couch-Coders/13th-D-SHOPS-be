package com.example.demo.dto;

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
    Long userEntryNo;
    String email;
    String name;
    String phone;
    LocalDateTime registeredDate;

    public UserDTO(User user){
        this.userEntryNo = user.getUserEntryNo();
        this.email = user.getEmail();
        this.name = user.getName();
        this.phone = user.getPhone();
    }

//    public User toEntiry(User user) {
//        return User.builder()
//                .user(user)
//                .email(user.getEmail())
//    }


    //}
}
