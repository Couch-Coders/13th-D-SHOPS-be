package com.example.demo.service;

import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class ProductService {
//    @Autowired
//    ProductRepository productRepository;
//
//    public List<Product> getProducts(Pageable pageable){
//        List<Product> products = productRepository.findByStoreIdOrderByCreatedDateDesc(pageable);
//        return products;
//    }
}
