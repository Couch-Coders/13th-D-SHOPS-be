package com.example.demo.dto;

import com.example.demo.entity.Image;
import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
public class ProductDTO{// extends BaseEntiryDTO
    String name;
    String description;
    String options;
    Image image;
    Long user_seq;
}
