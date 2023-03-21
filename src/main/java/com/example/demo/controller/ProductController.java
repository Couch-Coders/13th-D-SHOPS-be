package com.example.demo.controller;

import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    @Autowired
    ProductService productService;
    @Autowired
    ProductRepository productRepository;
    @GetMapping("")
    public List<Product> getProducts(@AuthenticationPrincipal User user){
        return productService.getProducts();
    }

    @GetMapping("/{seq}")
    public Product getProduct(@PathVariable Long seq){
        return productService.getProduct(seq);
    }

    //품목 등록
    @PostMapping("")
    public Product addProducts(@RequestBody Product product, @AuthenticationPrincipal User user){
        product.setUser_seq(user.getSeq());
        return productRepository.save(product);
    }

    //품목 수정
    @PutMapping("")
    public Product modifyProducts(@RequestBody Product product, @AuthenticationPrincipal User user){
        if(product.getName() == null || product.equals(""))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비어있음");

        // Repository에서 데이터 가져오기
        Optional<Product> findOne = productRepository.findById(product.getSeq());
        if(!findOne.isPresent()){ //데이터가 이미 존재하면 Exception을 발생시키고 종료
            // Repository에서 가져온 데이터가 존재하면  ResponseStatusException 를 리턴해주는데
            // 이는 Controller에서 HTTP 에러 응답을 하게 하는 Exception이다, HTTP code와 메세지를 적으면된다.
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "카테고리가 존재하지 않습니다.");
        }

        Product existingProduct = findOne.get();
        //existingProduct.setUser_seq(user.getSeq());
        if(product.getName() != null)
            existingProduct.setName(product.getName());
        if(product.getOptions() != null)
            existingProduct.setOptions(product.getOptions());
        if(product.getDescription() != null)
            existingProduct.setDescription(product.getDescription());

        return productRepository.save(existingProduct);
    }

    @DeleteMapping("/{seq}")
    public  String deleteProduct(@PathVariable Long seq){
        productRepository.deleteById(seq);
        return "succsess";
    }
}
