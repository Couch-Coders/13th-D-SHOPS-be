// 20230331 jay application-local.properties 로 설정시 Profile 설정되어 만들어 줬다
package com.example.demo.service.auth;

import com.example.demo.dto.FirebaseTokenDTO;
import com.example.demo.exception.CustomException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.service.UserService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Profile("local")
public class LocalAuthService extends AuthService {
  FirebaseAuth firebaseAuth;
  public LocalAuthService(FirebaseAuth firebaseAuth, UserService userService) {
    super(userService);
    this.firebaseAuth = firebaseAuth;
    log.info("====================LocalAuthService====================");
  }

  @Override
  public FirebaseTokenDTO verifyIdToken(String bearerToken) {
    log.info("====================FirebaseTokenDTO====================");
    try {
      FirebaseToken token = firebaseAuth.verifyIdToken(bearerToken);
      FirebaseTokenDTO tokenDTO = new FirebaseTokenDTO(token);
      return tokenDTO;
    } catch (FirebaseAuthException e) {
      log.error("access token is not usable : {}", e.getMessage());
      throw new CustomException(ErrorCode.AUTHENTICATION_FAILURE, "엑세스 토큰이 유효하지 않습니다.");
//            throw new CustomException(ErrorCode.NOT_FOUND_TOKEN);
      //return null;
    }
  }

  @Override
  public void revokeRefreshTokens(String uid) {
    log.info("====================revokeRefreshTokens====================");
    try {
      firebaseAuth.revokeRefreshTokens(uid);
    } catch (FirebaseAuthException e) {
      log.error("revoke token error : {}", e.getMessage());
//            throw new CustomException(ErrorCode.NOT_FOUND_USER, "리프레쉬 토큰을 삭제할 수 없습니다.");
    }
  }
}


