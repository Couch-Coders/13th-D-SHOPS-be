// https://github.com/Couch-Coders/12th-Fun-Pyeon-log-be/tree/main/src/main/java/com/example/demo/service/auth
//
// 구조
// (C)AuthService <- (C)InterAuthService
//         ^- (C)ProdAuthService
//
// 로컬 테스트시에는 OAuth 로그인이 불가능 하기 때문에 로컬 테스트가 가능하게 하기 위한 무엇인가가 필요하다.
//
// InterAuthService에서는 실제 Firebase Login이 아닌 가상의 로그인을 수행해서 API만 테스트 가능하게 만든다.
//
// ProdAuthService에서는 실제 Firebase Login을 수행한다.
//
// AuthService는 Abstract Class로 공통의 인터페이스와 실행을 제공한다.
package com.example.demo.service.auth;

import com.example.demo.dto.FirebaseTokenDTO;
import com.example.demo.entity.User;
//import com.example.demo.exception.CustomException;
import com.example.demo.exception.CustomException;
import com.example.demo.service.UserService;
import com.google.firebase.auth.FirebaseAuthException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public abstract class AuthService {
    UserService userService;

    public abstract FirebaseTokenDTO verifyIdToken(String bearerToken);

    public abstract void revokeRefreshTokens(String uid) throws FirebaseAuthException;

    public User loginOrEntry(FirebaseTokenDTO tokenDTO) {
        log.info("====================loginOrEntry====================");
        User user = null;
        try {
            log.error(tokenDTO.getEmail());
            user = userService.getUser(tokenDTO.getEmail());
            if (!user.isActiveUser()) {
                log.error("User \"" + user.getEmail() + "\" is not active user. activating user \"" + user.getEmail() + "\"");
                userService.activateUser(user);
            }
        } catch (CustomException e) {
            log.error("User with email {} was not found in the database, creating user", tokenDTO.getEmail());
            user = userService.addUser(tokenDTO.getEmail());
        }
        return user;
    }
}