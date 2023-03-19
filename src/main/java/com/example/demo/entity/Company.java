package com.example.demo.entity;

import com.example.demo.consts.UserActiveStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private String name;
    private String email;
    private String phone;

    //@OneToOne(mappedBy = "company")
//    @JoinColumn(name = "user_seq")
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "user_seq", referencedColumnName = "userEntryNo")
//    private User user;

    // getters and setters
//    @Builder
//    public Company(Long seq, String name) {
//        this.seq = seq;
//        this.name = name;
//    }
//
//    public Company(String name) {
//        this.name = name;
//    }
//    public Company() {
//    }
}