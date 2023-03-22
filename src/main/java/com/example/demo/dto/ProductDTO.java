package com.example.demo.dto;

import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductDTO extends BaseEntiryDTO {
    String name;
    String description;
    String options;
    Long user_seq;
}
