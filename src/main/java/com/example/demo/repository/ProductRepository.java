package com.example.demo.repository;

import com.example.demo.dto.ProductDTO;
import com.example.demo.entity.Address;
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

    @Query("SELECT seq, ( 3959 * acos( cos( radians(:location_y) ) * cos( radians( location_y ) ) * cos( radians( location_x ) - radians(:location_x) ) + sin( radians(:location_y) ) * sin( radians( location_y ) ) ) ) AS distance FROM ds_products") //  HAVING distance < 25 ORDER BY distance LIMIT 0 , 5;
    Page<Product> findNear(Pageable pageable, @Param("location_x") Double location_x, @Param("location_y") Double location_y);
//    Pageable pageable,    return entityManager.createNativeQuery(
//                "SELECT address, ( 3959 * acos( cos( radians(:latitude) ) * cos( radians( lat ) ) * cos( radians( lng ) - radians(:longitude) ) + sin( radians(:latitude) ) * sin( radians( lat ) ) ) ) AS distance FROM markers HAVING distance < 25 ORDER BY distance LIMIT 0 , 5;",
//    Address .class)
//            .setParameter("latitude", latitude)
//                .setParameter("longitude", longitude)
//                .getResultList();
//}

//    Page<Product> findByUser_seqContains(Pageable pageable, Long seq);

    Page<Product> findByTitleContains(Pageable pageable, String name);
}
