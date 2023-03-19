package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

@Entity(name = "ds_product")
@Getter
@Setter
@DynamicInsert //@ColumnDefault("'N'") 할려고
public class Product extends BaseEntiry{
    @Column
    private String name;
//    private String description;
//    private String options;
//    @ManyToOne
//    @JoinColumn(name = "user_seq")
//    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq")
    private User user;
}
