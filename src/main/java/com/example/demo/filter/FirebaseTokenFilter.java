// https://www.couchcoding.kr/blogs/couchcoding/Firebase%EB%A1%9C%20Google%20%EB%A1%9C%EA%B7%B8%EC%9D%B8%20%EA%B5%AC%ED%98%84%ED%95%98%EA%B8%B0%202%20(Spring%20%ED%8C%8C%ED%8A%B8)
// 3. Filter에서 인증토큰 검증하기
//이제 백엔드에서는 firebase IDToken을 인증하는 부분을 작성하려고 합니다. Filter는 사용자 요청의 전후 처리를 할 수 있는 구성요소입니다. 사용자 요청이 들어오면 Controller에 접근하기 전에 먼저 Request를 인터셉트 해서 전처리 역할 및 후처리 역할을 할 수 있습니다.
//
//
//
//또한 Spring Security 설정과 결합하면 특정 Request와 결합할때만 사용자 요청을 처리할 수 있습니다.
//
//토큰을 검증하는 Filter를 만들고 Security에 요청에따라 검증하도록 처리해봅시다.
//
//(본 예제는 Client 단에서 Header에 Authorization: Bearer {FirebaseIdToken} 형태로 메세지가 온다고 가정합니다.)
//doFilterInternal를 오버라이드 하였는데 Request가 들어오면 해당 로직을 타게된다.
//
//전체로직은
//
//Authorization Header에서 Token을 가져온다.
//FirebaseAuth를 이용하여 Token을 검증한다.
//UserDetailsService에서 사용자 정보를 가져와 SecuriyContext에 추가해준다.
//현재 예제에서는 id를 firebase에서 제공하는 uid를 사용하였다 Firebase에서 제공하는 사용자별 유니크 id다. →userDetailsService.loadUserByUsername(uid)
//UserDetails와 UserDetailsService는 Interface를 구현해 사용해주자.
//Context에 추가한 User정보는 Controller에 Principal principal 를 추가해 받아올 수 있다.
//https://www.baeldung.com/get-user-in-spring-security 참고!
//인증 실패시 HttpStatus 403과 json으로 code를 response하게 하였다.

package com.example.demo.filter;

import com.example.demo.consts.AuthConsts;
import com.example.demo.dto.FirebaseTokenDTO;
import com.example.demo.entity.User;
import com.example.demo.service.auth.AuthService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.Cookie;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class FirebaseTokenFilter extends OncePerRequestFilter {

    AuthService authService;
    UserDetailsService userDetailsService;

    public FirebaseTokenFilter(AuthService authService, UserDetailsService userDetailsService) {
        this.authService = authService;
        this.userDetailsService = userDetailsService;
    }

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        String token = findCookie(cookies, AuthConsts.accessTokenKey);

        try {
            FirebaseTokenDTO tokenDTO = authService.verifyIdToken(token);
            String email = authService.verifyIdToken(token).getEmail();
            User user = (User) userDetailsService.loadUserByUsername(email);
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            user.setUid(tokenDTO.getUid());
        } catch (Exception e) {
            setUnauthorizedResponse(response, "INVALID_TOKEN");
            return;
        }

        filterChain.doFilter(request, response);
    }

    protected String findCookie(Cookie[] cookies, String cookieName) {
//        for (Cookie c : cookies) {
//            if (c.getName().equals(cookieName)) {
//                log.info(cookieName + " : " + c.getValue());
//                return c.getValue();
//            }
//        }
        return null;
    }

    protected void setUnauthorizedResponse(HttpServletResponse response, String code) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"code\" : \""+code+"\"}");
    }

}