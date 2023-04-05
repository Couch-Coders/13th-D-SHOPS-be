package com.example.demo.repository;

import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
//    List<Product> findByStoreIdOrderByCreatedDateDesc(Pageable pageable);
//    List<ProductDTO> findAll();
    @Query("SELECT u FROM ds_product u WHERE u.user_seq = :seq")
    Page<Product> findAllByUserSeq(Pageable pageable, @Param("seq") Long seq);
    @Query("SELECT u FROM ds_product u WHERE u.seq = :seq")
    Optional<Product> findByProductSeq(@Param("seq") Long seq);

//    Optional<Product> findBySeq(Long seq);

//    @Query("SELECT pd.seq AS seq,( 3959 * acos( cos( radians(:location_y) ) * cos( radians( location_y ) ) * cos( radians( location_x ) - radians(:location_x) ) + sin( radians(:location_y) ) * sin( radians( location_y ) ) ) ) distance FROM ds_product pd") //  HAVING distance < 25 ORDER BY distance LIMIT 0 , 5;
//@Query("SELECT u.seq AS seq,( 3959 * acos( cos( radians(:location_y) ) * cos( radians( location_y ) ) * cos( radians( location_x ) - radians(:location_x) ) + sin( radians(:location_y) ) * sin( radians( location_y ) ) ) ) AS distance FROM ds_product u") //  HAVING distance < 25 ORDER BY distance LIMIT 0 , 5;
//    @Query("SELECT u.seq as seq, a.distance as dis FROM ds_product u join(SELECT (1) AS distance) AS a ON 1=1") //  HAVING distance < 25 ORDER BY distance LIMIT 0 , 5;
//@Query("SELECT u.seq as seq, a.distance as dis FROM ds_product u join(SELECT (1) AS distance) AS a ON 1=1") //  HAVING distance < 25 ORDER BY distance LIMIT 0 , 5;
// left outer join(SELECT (1) AS distance) AS a ON 1=1 //  HAVING distance < 25 ORDER BY distance LIMIT 0 , 5;
//    @Query("SELECT u.seq as seq, (1+1) as b FROM ds_product u")
//    @Query(value = "SELECT u, (1+1) as distance FROM ds_product u", nativeQuery = true)
//    @Query(value = "SELECT *,( 3959 * acos( cos( radians(:location_y) ) * cos( radians( location_y ) ) * cos( radians( location_x ) - radians(:location_x) ) + sin( radians(:location_y) ) * sin( radians( location_y ) ) ) ) as distance FROM ds_product ORDER BY distance", countQuery = "SELECT COUNT(*) FROM product" , nativeQuery = true)
//    @Query(value = "SELECT u.seq as seq, (1+1) as distance FROM ds_product u ORDER BY distance", countQuery = "SELECT COUNT(*) FROM ds_product" , nativeQuery = true)
//    @Query(value = "SELECT u.seq, u.title, u.description,u.options,u.location_x,u.location_y, u.user_seq, ( 3959 * acos( cos( radians(:location_y) ) * cos( radians( location_y ) ) * cos( radians( location_x ) - radians(:location_x) ) + sin( radians(:location_y) ) * sin( radians( location_y ) ) ) ) as distance FROM ds_product u ORDER BY distance", countQuery = "SELECT COUNT(*) FROM ds_product" , nativeQuery = true)
    //@Query(value = "SELECT u.seq, u.title, u.description, u.options, u.company.address.location_x as location_x, u.company.address.location_y as location_y, u.user_seq, ( 3959 * acos( cos( radians(:location_y) ) * cos( radians( location_y ) ) * cos( radians( location_x ) - radians(:location_x) ) + sin( radians(:location_y) ) * sin( radians( location_y ) ) ) ) as distance FROM ds_product u ORDER BY distance", countQuery = "SELECT COUNT(*) FROM ds_product" , nativeQuery = true)
// 20230405 jay 위치를 address 에 받아오게 수정
//    @Query(value = "SELECT u.seq, u.title, u.description, u.options, a.location_x, a.location_y, u.user_seq, " +
//            "( 3959 * acos( cos( radians(:location_y) ) * cos( radians( a.location_y ) ) * cos( radians( a.location_x ) - radians(:location_x) ) + sin( radians(:location_y) ) * sin( radians( a.location_y ) ) ) ) as distance " +
//            "FROM ds_product u " +
//            "INNER JOIN ds_company c ON u.company_seq = c.seq " +
//            "INNER JOIN ds_address a ON c.address_seq = a.seq " +
//            "ORDER BY distance",
//            countQuery = "SELECT COUNT(*) FROM ds_product",
//            nativeQuery = true)
    // 20230406 jay url 추가
    @Query(value = "SELECT u.seq, u.title, u.description, u.options, a.location_x, a.location_y, u.user_seq, i.url, i.name, " +
            "( 3959 * acos( cos( radians(:location_y) ) * cos( radians( a.location_y ) ) * cos( radians( a.location_x ) - radians(:location_x) ) + sin( radians(:location_y) ) * sin( radians( a.location_y ) ) ) ) as distance " +
            "FROM ds_product u " +
            "INNER JOIN ds_company c ON u.company_seq = c.seq " +
            "INNER JOIN ds_address a ON c.address_seq = a.seq " +
            "LEFT JOIN ds_product_images pi ON u.seq = pi.ds_product_seq " +
            "LEFT JOIN ds_image i ON pi.images_seq = i.seq " +
            "ORDER BY distance",
            countQuery = "SELECT COUNT(*) FROM ds_product",
            nativeQuery = true)
    Page<Map> findNear(@Param("location_x") Double location_x, @Param("location_y") Double location_y, Pageable pageable);
    //u.seq as seq, u.addDate as addDate, u.mod_date as mod_date, u.del_Flag_YN as del_Flag_YN, u.name as name, u.title as title, u.description as description, u.options as options, u.user_seq as user_seq, u.location_x as location_x, u.location_y = location_y ,
//    Page<Product> findNear(Pageable pageable, @Param("location_x") Double location_x, @Param("location_y") Double location_y);
//    return entityManager.createNativeQuery(
//                "SELECT address, ( 3959 * acos( cos( radians(:location_y) ) * cos( radians( location_y ) ) * cos( radians( location_x ) - radians(:location_x) ) + sin( radians(:location_y) ) * sin( radians( location_y ) ) ) ) AS distance FROM markers HAVING distance < 25 ORDER BY distance LIMIT 0 , 5;",
//    Product.class)
//            .setParameter("location_y", location_y)
//                .setParameter("location_x", location_x)
//                .getResultList();
//}

//    Page<Product> findByUser_seqContains(Pageable pageable, Long seq);

    Page<Product> findByTitleContains(Pageable pageable, String name);
}
