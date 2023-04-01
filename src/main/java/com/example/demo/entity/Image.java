package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
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
//    @Column
//    private Long product_seq;
    @Column
    private Long user_seq;
//    @ManyToOne
//    @JoinColumn(name = "product_seq", referencedColumnName = "seq")
//    private Product product;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "product_seq", referencedColumnName = "seq")
//    private Product product;
//    @ManyToOne
//    @JoinColumn(name = "product_seq", referencedColumnName = "seq")
//    @JsonBackReference
//    private  Product product;
    public Image() {

    }
}

