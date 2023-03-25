package com.example.demo.controller;

import com.example.demo.consts.AuthConsts;
import com.example.demo.dto.UserDTO;
import com.example.demo.dto.ProductDTO;
import com.example.demo.entity.Image;
import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.repository.CompanyRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.ProductService;
import com.example.demo.service.auth.AuthService;
import com.example.demo.service.UserService;
import com.google.firebase.auth.FirebaseAuthException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
//@RequiredArgsConstructor
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserService userService;

    private final ProductService productService;

    private ProductRepository productRepository;

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

    // 지운 사람 가져오기 테스트
    @GetMapping("/not")
    public List<User> getNotDeletedUser(){
        return userRepository.findAllNotDeleted();
    }

    @GetMapping("/me/products")
    public Page<Product> getMyProducts(Pageable pageable, @AuthenticationPrincipal User user){
        return productService.getMyProducts(pageable, user.getSeq());
    }

    @PostMapping("/me/products")
    public Product createMyProducts(@RequestBody ProductDTO productDTO, @AuthenticationPrincipal User user){
        productDTO.setUser_seq(user.getSeq());
        return productService.createProduct(productDTO);
    }

    @PutMapping("/me/products/{seq}")
    public Product modifyProducts(@PathVariable Long seq, @RequestBody Product product, @AuthenticationPrincipal User user){
        if(seq == null || product.equals(""))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비어있음");

        // Repository에서 데이터 가져오기
        Optional<Product> findOne = productRepository.findByProductSeq(seq);
        if(!findOne.isPresent()){ //데이터가 이미 존재하면 Exception을 발생시키고 종료
            // Repository에서 가져온 데이터가 존재하면  ResponseStatusException 를 리턴해주는데
            // 이는 Controller에서 HTTP 에러 응답을 하게 하는 Exception이다, HTTP code와 메세지를 적으면된다.
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "카테고리가 존재하지 않습니다.");
        }

        Product existingProduct = findOne.get();
        //existingProduct.setUser_seq(user.getSeq());
        if(product.getName() != null)
            existingProduct.setName(product.getName());
        if(product.getTitle() != null)
            existingProduct.setTitle(product.getTitle());
        if(product.getOptions() != null)
            existingProduct.setOptions(product.getOptions());
        if(product.getDescription() != null)
            existingProduct.setDescription(product.getDescription());

        return productRepository.save(existingProduct);
    }

    @DeleteMapping("/me/products/{seq}")
    public  String deleteProduct(@PathVariable Long seq){
        productRepository.deleteById(seq);
        return "succsess";
    }
}
