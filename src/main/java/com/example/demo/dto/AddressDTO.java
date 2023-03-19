package com.example.demo.dto;

import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AddressDTO extends BaseEntiryDTO{
    private String name;
    private String post_code;
    private String address;
    private String extra;
    private String detail;

    private String location_x;
    private String location_y;
}
