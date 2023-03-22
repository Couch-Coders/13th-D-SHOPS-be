package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

@Entity(name = "ds_image")
@Getter
@Setter
@DynamicInsert //@ColumnDefault("'N'") 할려고
public class Image  extends BaseEntiry {
    @Column
    private String name;
    @Column
    private String url;
}
