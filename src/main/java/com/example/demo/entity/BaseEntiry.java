package com.example.demo.entity;

import com.google.firebase.database.annotations.NotNull;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@ToString
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntiry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(updatable = false, nullable = false, columnDefinition = "INT(11)")
//    @Column(updatable = false, nullable = false, columnDefinition = "INT(11)")
    Long seq;
    @CreationTimestamp
    @Column(updatable = false)
    LocalDateTime add_date;

//    @LastModifiedDate
    @UpdateTimestamp
    LocalDateTime mod_date;

    @Column(columnDefinition = "CHAR(1) DEFAULT 'N'")
//    @Column(columnDefinition = "char(1)")
//    @ColumnDefault("'N'")
    String del_Flag_YN;
}
