package com.example.demo.service;

import com.example.demo.dto.ProductDTO;
import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
//@AllArgsConstructor
@RequiredArgsConstructor
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    public List<Product> getProducts(){
        return productRepository.findAll();
    }
    public Product getProduct(Long seq){
        return productRepository.findById(seq).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.BAD_REQUEST, "상품이 존재하지 않습니다."));
    }

    @Transactional // 트랜젝셕
    public ProductDTO modifyProduct(ProductDTO productDTO) {
        if (productDTO.getSeq() == null || productDTO.getName().equals(""))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "사용자 id가 비었습니다.");
        return productDTO;
    }
}
