package com.example.demo.entity;

import com.example.demo.dto.ProductDTO;
import com.google.api.client.json.Json;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "ds_product")
@Getter
@Setter
@ToString
@DynamicInsert //@ColumnDefault("'N'") 할려고
public class Product extends BaseEntiry{
    @Column
    private String name;
    @Column
    private String description;
    @Column
    private String options; //List<String>

//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "image_seq")
//    Image image;
//    @OneToMany//(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
//    @JoinColumn(name = "image_seq", referencedColumnName = "seq")
//    private List<Image> image;
//    private Image image;
//    private List<Image> image = new ArrayList<>();
//    private List<Image> images = new ArrayList<>();
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

//    @Builder
//    public Product (Product product) {
//        this.seq = product.getSeq();
//        this.name = product.getName();
//        this.description = product.getDescription();
//        this.options = product.getOptions();
//        this.image = product.getImage();
//        this.user_seq = product.getUser_seq();
//    }
    @Builder
    public Product (Long seq, String name, String description, String options, Long user_seq) {
        this.seq = seq;
        this.name = name;
        this.description = description;
        this.options = options;
        this.user_seq = user_seq;
    }
    public Product() {

    }
//    public void addImage(Image image){
//        this.image.add(image);
//    }
}
