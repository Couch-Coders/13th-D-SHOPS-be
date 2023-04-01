package com.example.demo.controller;

import com.example.demo.dto.ProductDTO;
import com.example.demo.entity.Company;
import com.example.demo.entity.Image;
import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
//@RequiredArgsConstructor
@AllArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    private ProductRepository productRepository;
    @GetMapping("")
    public Page<Product> getProducts(Pageable pageable, @AuthenticationPrincipal User user, @RequestParam(value="k", required=false) String k){
        return productService.getProducts(pageable, k);
    }
    @GetMapping("/search/{k}")
    public Page<Product> getProductsSearch(Pageable pageable, @AuthenticationPrincipal User user, @PathVariable String k){
        return productService.getProducts(pageable, k);
    }

    @GetMapping("/{seq}")
    public Product getProduct(@PathVariable Long seq){
        return productService.getProduct(seq);
    }

    //품목 등록
    @PostMapping("")
    public Product createProducts(@RequestBody Product product, @AuthenticationPrincipal User user){
        product.setUser_seq(user.getSeq());
//        Company company = new Company(user.getCompany());
        product.setCompany(user.getCompany());
        return productService.createProduct(product);
    }

    //품목 수정
    @PutMapping("")
    public Product modifyProducts(@RequestBody Product product, @AuthenticationPrincipal User user){
        if(product.getSeq() == null || product.equals(""))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비어있음");

        // Repository에서 데이터 가져오기
        Optional<Product> findOne = productRepository.findByProductSeq(product.getSeq());
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

    @PutMapping("{seq}")
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

    //품목 삭제
    @DeleteMapping("/{seq}")
    public  String deleteProduct(@PathVariable Long seq){
        productRepository.deleteById(seq);
        return "succsess";
    }

    // product > image ? 생각해보기
    @PostMapping("/{product_seq}/images")
    public Product uploadImage(@RequestParam MultipartFile files, @PathVariable Long product_seq, @AuthenticationPrincipal User user) throws IOException {
        Optional<Product> findOne = productRepository.findByProductSeq(product_seq);
//        Product product = productRepository.findById(product_seq);
        if(!findOne.isPresent()){ //데이터가 이미 존재하면 Exception을 발생시키고 종료
            // Repository에서 가져온 데이터가 존재하면  ResponseStatusException 를 리턴해주는데
            // 이는 Controller에서 HTTP 에러 응답을 하게 하는 Exception이다, HTTP code와 메세지를 적으면된다.
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "카테고리가 존재하지 않습니다.");
        }

        Product existingProduct = findOne.get();
//        Product product = productRepository.findBySeq(product_seq);
        Image image = new Image();
        image.setUser_seq(user.getSeq());
        log.info("================uploadImage================");
        log.info(files.getOriginalFilename());
        image.setName(files.getOriginalFilename());
        existingProduct.addImage(image);
//        return productRepository.save(existingProduct);
        return productService.uploadImage(existingProduct, files.getBytes());
//        return image;
    }
    @GetMapping("/{product_seq}/images/{fileName}")
    public byte[] downloadProfile(@PathVariable String product_seq, @PathVariable String fileName) {
        return productService.getProfile(product_seq, fileName);
    }

    @GetMapping("/near")
    public Page<Map> findNearestAddresses(Double location_x, Double location_y, Pageable pageable) {
        return productService.findNear(location_x, location_y, pageable);
//        return entityManager.createNativeQuery(
//                        "SELECT address, ( 3959 * acos( cos( radians(:latitude) ) * cos( radians( lat ) ) * cos( radians( lng ) - radians(:longitude) ) + sin( radians(:latitude) ) * sin( radians( lat ) ) ) ) AS distance FROM markers HAVING distance < 25 ORDER BY distance LIMIT 0 , 5;",
//                        Address.class)
//                .setParameter("latitude", latitude)
//                .setParameter("longitude", longitude)
//                .getResultList();
    }
}
