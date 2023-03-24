package com.example.demo.repository;

import com.example.demo.dto.ProductDTO;
import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
//    List<Product> findByStoreIdOrderByCreatedDateDesc(Pageable pageable);
//    List<ProductDTO> findAll();
    @Query("SELECT u FROM ds_product u WHERE u.user_seq = :seq")
    Page<Product> findAllBySeq(Pageable pageable, @Param("seq") Long seq);
}
