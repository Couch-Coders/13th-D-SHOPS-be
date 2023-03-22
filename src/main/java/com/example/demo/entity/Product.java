package com.example.demo.entity;

import com.google.api.client.json.Json;
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
    @Column
    private String description;
    @Column
    private String options;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_seq", referencedColumnName = "seq")
    private Image image;
    @Column
    private Long user_seq;
//    private String description;
//    private String options;
//    @ManyToOne
//    @JoinColumn(name = "user_seq")
//    private User user;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_seq", referencedColumnName = "seq")
//    private User user;
}
