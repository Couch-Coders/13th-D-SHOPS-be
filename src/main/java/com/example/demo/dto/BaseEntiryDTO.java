package com.example.demo.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BaseEntiryDTO {
    Long seq;
    LocalDateTime addDate;
    LocalDateTime modDate;
}
