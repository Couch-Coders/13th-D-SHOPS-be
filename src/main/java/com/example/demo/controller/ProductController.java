package com.example.demo.controller;

import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    @Autowired
    ProductService productService;
    @Autowired
    ProductRepository productRepository;
    @GetMapping()
    public List<Product> getProducts(Pageable pageable){
        //return productService.getProducts(pageable);
        return productRepository.findAll();
    }
    @PostMapping()
    public Product addProducts(@RequestBody Product product, @AuthenticationPrincipal User user){
        product.setUser(user);
        return productRepository.save(product);
    }
}
