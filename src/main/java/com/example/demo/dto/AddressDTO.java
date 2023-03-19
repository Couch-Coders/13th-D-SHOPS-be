package com.example.demo.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AddressDTO {
    private Long seq;
    private String name;
}
