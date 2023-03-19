package com.example.demo.controller;

import com.example.demo.consts.AuthConsts;
import com.example.demo.dto.UserDTO;
import com.example.demo.entity.User;
import com.example.demo.repository.CompanyRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.auth.AuthService;
import com.example.demo.service.UserService;
import com.google.firebase.auth.FirebaseAuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    AuthService authService;

    //private final UserService userService;

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    // 내정보
    @GetMapping("/me")
    public User getUser(@AuthenticationPrincipal User user) {
        log.info("getUser " + user);
        return userService.getUser(user.getSeq());
    }

    // 회원 탈퇴
    @DeleteMapping("/me")
    public ResponseEntity<String> deleteUser(@AuthenticationPrincipal User user) throws FirebaseAuthException {
        userService.deleteUser(user.getEmail());
        authService.revokeRefreshTokens(user.getUid());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, removeCookie(AuthConsts.accessTokenKey).toString())
                .build();
    }

    // 회원 정보 수정
    @PutMapping("/me")
    public User modifyUser(@RequestBody UserDTO userDTO, @AuthenticationPrincipal User user) {
        userDTO.setSeq(user.getSeq());
        return userService.modifyUser(userDTO);
        //return userService.save(userDTO);
    }

    public ResponseCookie removeCookie(String key){
        return ResponseCookie.from(key, "")
                .maxAge(0)
                .sameSite("Lax")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .build();
    }

    //sql 테스트
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/not")
    public List<User> getNotDeletedUser(){
        return userRepository.findAllNotDeleted();
    }
}