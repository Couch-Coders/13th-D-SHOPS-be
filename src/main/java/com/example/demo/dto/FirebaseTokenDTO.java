//로그인 성공시 유저 정보를 리턴해주는 DTO
package com.example.demo.dto;

import com.google.firebase.auth.FirebaseToken;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FirebaseTokenDTO {
    String uid;
    String name;
    String email;
    String pictureUrl;

    public FirebaseTokenDTO(FirebaseToken firebaseToken) {
        this.uid = firebaseToken.getUid();
        this.name = firebaseToken.getName();
        this.email = firebaseToken.getEmail();
        this.pictureUrl = firebaseToken.getPicture();
    }
}
