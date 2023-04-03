package com.example.demo.entity;

import com.example.demo.dto.ProductDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.google.api.client.json.Json;
import jakarta.persistence.*;
import lombok.*;
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
    private String title;
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
    //20230401 jay product 생성시 회사, 주소 연결
//    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @JoinColumn(name = "company_seq", referencedColumnName = "seq")
    @ManyToOne
    @JoinColumn(name = "company_seq", referencedColumnName = "seq")//, insertable = false, updatable = false
//    @JsonIgnore
    private Company company;
//    @ManyToOne
//    @JoinColumn(name = "image_seq", referencedColumnName = "seq")
////    private List<Image> image = new ArrayList<>();
//    private Image image;
//    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)//
////    @JoinColumn(name = "image_seq", referencedColumnName = "seq")
//    private List<Image> images = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    public void addImage(final Image image) {
        images.add(image);
    }
    public void deleteImage(Long index) {
        images.remove(index);
    }
    @Column
    Double location_x; // Longitude
    @Column
    Double location_y; // Latitude
    @Transient
    private double distance;
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
    public Product (Product product) {
        this.seq = product.seq;
        this.name = product.name;
        this.title = product.title;
        this.description = product.description;
        this.options = product.options;
        this.user_seq = product.user_seq;
        this.company = product.company;
        this.location_x = product.location_x;
        this.location_y = product.location_y;
    }
    @Builder
    public Product (Long seq, String name, String title, String description, String options, Long user_seq, Company company, Double location_x, Double location_y) {
        this.seq = seq;
        this.name = name;
        this.title = title;
        this.description = description;
        this.options = options;
        this.user_seq = user_seq;
        this.company = company;
        this.location_x = location_x;
        this.location_y = location_y;
    }
    public Product() {

    }
//    public void addImages(List<Image> images){
//        this.images.addAll(images);
//    }
//    public void putImage(List<Image> images){
//        this.images = images;
//    }
}
