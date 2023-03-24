package com.example.demo.service;

import com.example.demo.consts.UserActiveStatus;
import com.example.demo.dto.ProductDTO;
import com.example.demo.entity.Image;
import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.exception.CustomException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.repository.ImageRepository;
import com.example.demo.repository.ProductRepository;
import com.google.cloud.storage.Bucket;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
//@RequiredArgsConstructor
public class ProductService {
    private ProductRepository productRepository;
    private ImageRepository imageRepository;
    private final Bucket bucket;

    public Page<Product> getProducts(Pageable pageable){
        return productRepository.findAll(pageable);
//        Product
    }

    public Page<Product> getMyProducts(Pageable pageable, Long user_seq){
        return productRepository.findAllBySeq(pageable, user_seq);
    }
    @Transactional
    public Product createProduct(ProductDTO dto) {
        // 중복 검사
        // 중복 에러
        // 자료 가져오기
        // 자료 빌드
        Product product = Product.builder()
                .name(dto.getName())
                .options(dto.getOptions())
                .description(dto.getDescription())
                .user_seq(dto.getUser_seq())
                .build();
//        product.addImage(dto.getImage());
        log.info(product.toString());
        return productRepository.save(product);
    }

    public Product getProduct(Long seq){
        return productRepository.findById(seq).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.BAD_REQUEST, "상품이 존재하지 않습니다."));
    }

//    @Transactional // 트랜젝셕
//    public ProductDTO modifyProduct(ProductDTO productDTO) {
//        if (productDTO.getSeq() == null || productDTO.getName().equals(""))
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "사용자 id가 비었습니다.");
//        return productDTO;
//    }
    public Image uploadImage(Image image,byte[] files) {
        // File 저장 위치 선업
        String blob = "products/"+image.getProduct_seq()+"/images/"+image.getName();
        image.setUrl(blob);
        log.info("url"+blob);

        try {
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
            image.setUrl("/"+blob);
            imageRepository.save(image);
            return image;

        } catch (CustomException e) {
            log.error(image.getUrl() + " profile upload faild", e);
            //throw new CustomException(ErrorCode.IMAGE_UPLOAD_FAILED);
            throw new CustomException(ErrorCode.NOT_CORRECT_USER, "IMAGE_UPLOAD_FAILED");
        }
    }

    public byte[] getProfile(String product_seq, String fileName) {
        return bucket.get("products/"+product_seq+"/images/"+fileName).getContent();
    }
}
