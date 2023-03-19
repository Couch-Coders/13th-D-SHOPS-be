package com.example.demo.dto;

import com.example.demo.entity.Company;
import com.example.demo.entity.User;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CompanyDTO extends BaseEntiryDTO{
    private String name;
    private String email;
    private String phone;
}

