package com.example.demo.dto;

import com.example.demo.entity.Company;
import com.example.demo.entity.Image;
import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import jakarta.persistence.Column;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductDTO extends BaseEntiryDTO{// extends BaseEntiryDTO
    String name;
    String title;
    String description;
    String options;
    Image image;
    Long user_seq;
    //20230401 jay product 생성시 회사, 주소 연결
    Company company;
    Double location_x;
    Double location_y;

    @Builder
    public ProductDTO(Product product){
        this.seq = product.getSeq();
        this.name = product.getName();
        this.title = product.getTitle();
        this.description = product.getDescription();
        this.options = product.getOptions();
        this.user_seq = product.getUser_seq();
        this.company = product.getCompany();
    }
}
