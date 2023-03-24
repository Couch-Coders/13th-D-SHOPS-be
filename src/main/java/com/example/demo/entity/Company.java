package com.example.demo.entity;

import com.example.demo.consts.UserActiveStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Entity(name = "ds_company")
@Getter
@Setter
@DynamicInsert //@ColumnDefault("'N'") 할려고
public class Company extends BaseEntiry {
    @Column
    private String name;
    @Column
    private String email;
    @Column
    private String phone;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_seq", referencedColumnName = "seq")
    private Address address;

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
    public Company() {
    }
}