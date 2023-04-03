package com.example.demo.service;

import com.example.demo.dto.ProductDTO;
import com.example.demo.entity.Image;
import com.example.demo.entity.Product;
import com.example.demo.exception.CustomException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.repository.ImageRepository;
import com.example.demo.repository.ProductRepository;
import com.google.cloud.storage.Bucket;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
//@RequiredArgsConstructor
public class ProductService {
    private ProductRepository productRepository;
    private ImageRepository imageRepository;
    private final Bucket bucket;

    public Page<Product> getProducts(Pageable pageable, String keyword){
        if (keyword == null){
            System.out.println("keyword is null");
            return productRepository.findAll(pageable);
        }

        return productRepository.findByTitleContains(pageable, keyword);
//        return productRepository.findAll(pageable);
    }

    public Page<Product> getMyProducts(Pageable pageable, Long user_seq){
        return productRepository.findAllByUserSeq(pageable, user_seq);
    }
    @Transactional
    public Product createProduct(Product product) {
        // 중복 검사
        // 중복 에러
        // 자료 가져오기
        // 자료 빌드
//        Product productPrepare = Product.builder()
//                .name(product.getName())
//                .title(product.getTitle())
//                .options(product.getOptions())
//                .description(product.getDescription())
//                .user_seq(product.getUser_seq())
//                .company(product.getCompany())
//                .location_x(product.getLocation_x())
//                .location_y(product.getLocation_y())
//                .build();
//        product.addImage(dto.getImage());
//        log.info(product.toString());
        return productRepository.save(product);
    }

    public Product getProduct(Long seq){
        Product product = productRepository.findById(seq).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.BAD_REQUEST, "상품이 존재하지 않습니다."));
//        ProductDTO productDTO = new ProductDTO(product);
        return product;
    }

//    @Transactional // 트랜젝셕
//    public ProductDTO modifyProduct(ProductDTO productDTO) {
//        if (productDTO.getSeq() == null || productDTO.getName().equals(""))
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "사용자 id가 비었습니다.");
//        return productDTO;
//    }
    @Transient
    public Product uploadImage(Product product,byte[] files) {
        // 받아온 품목을 저장하여 image seq 받아온다.
        Product returnProduct = productRepository.save(product);
        // File 저장 위치
        int imageIndex = 0;
        try {
            imageIndex = returnProduct.getImages().size()-1;
            log.info("imageIndex: "+imageIndex);
        } catch (CustomException e) {
            log.error(returnProduct.getImages().size() + " imageSize", e);
            throw new CustomException(ErrorCode.NOT_CORRECT_USER, "IMAGE_UPLOAD_FAILED");
        }

        try {

            //String blob = "products/"+product.getSeq()+"/images/"+product.getImages().get(imageIndex).getName();
            String blob = "products/"+returnProduct.getSeq()+"/images/"+returnProduct.getImages().get(imageIndex).getSeq();
            log.info("url"+blob);

            // 이미 존재하면 파일 삭제
            if(bucket.get(blob) != null) {
                log.info("존재");
                bucket.get(blob).delete();
            }
            // 파일을 Bucket에 저장
    //            bucket.create(blob,files,"image/jpg");
            bucket.create(blob,files,"multipart/form-data");
            log.info("저장");
            // DB에 유저 정보 업데이트 (Profile 이미지 위치 추가)
            returnProduct.getImages().get(imageIndex).setUrl("/"+blob);

            return productRepository.save(returnProduct);

        } catch (CustomException e) {
//            log.error(image.getUrl() + " profile upload faild", e);
            log.error(returnProduct.getImages().get(imageIndex).getUrl() + " profile upload faild", e);

            //throw new CustomException(ErrorCode.IMAGE_UPLOAD_FAILED);
            throw new CustomException(ErrorCode.NOT_CORRECT_USER, "IMAGE_UPLOAD_FAILED");
        }
    }
    @Transient
    public Product deleteImage(Long productSeq, Long imageSeq) {
        // product 가져오기
        Optional<Product> findOne = productRepository.findByProductSeq(productSeq);
        if(!findOne.isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "카테고리가 존재하지 않습니다.");
        }
        Product existingProduct = findOne.get();

        // image 가져오기
        Image image = existingProduct.getImages().stream()
                .filter(img -> img.getSeq().equals(imageSeq))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "카테고리가 존재하지 않습니다."));

        // image 파일 삭제
        String blob = "products/"+existingProduct.getSeq()+"/images/"+image.getSeq();
        log.info("url"+blob);

        try {
            // 이미 존재하면 파일 삭제
            if(bucket.get(blob) != null) {
                log.info("존재");
                bucket.get(blob).delete();
            }

            // image 삭제
            existingProduct.getImages().remove(image);

            // product 저장
            return productRepository.save(existingProduct);

        } catch (CustomException e) {
            log.error(blob + " image file delete faild", e);

            //throw new CustomException(ErrorCode.IMAGE_UPLOAD_FAILED);
            throw new CustomException(ErrorCode.NOT_CORRECT_USER, "IMAGE_UPLOAD_FAILED");
        }

    }
    public byte[] getProfile(String product_seq, String fileName) {
        return bucket.get("products/"+product_seq+"/images/"+fileName).getContent();
    }
    @Transient
//    private double distance;
    public Page<Map> findNear(Double location_x, Double location_y, Pageable pageable){
        return productRepository.findNear(location_x, location_y, pageable);
//        return entityManager.createNativeQuery(
//                "SELECT address, ( 3959 * acos( cos( radians(:location_y) ) * cos( radians( location_y ) ) * cos( radians( location_x ) - radians(:location_x) ) + sin( radians(:location_y) ) * sin( radians( location_y ) ) ) ) AS distance FROM markers HAVING distance < 25 ORDER BY distance LIMIT 0 , 5;",
//    Product.class)
//            .setParameter("location_y", location_y)
//                .setParameter("location_x", location_x)
//                .getResultList();
    }


}
