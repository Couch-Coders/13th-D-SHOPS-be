package com.example.demo.entity;

import com.example.demo.consts.UserActiveStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

//import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

@Entity(name = "users")
@ToString
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long userEntryNo;

    @Column
    String email;
    @Column
    String name;
    @Column
    String phone;

    @CreatedDate
    Date registeredDate;

    @Column
    UserActiveStatus userActiveStatus;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "company_seq", referencedColumnName = "seq")
//    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
//    @JoinColumn(name = "company_seq")
    private Company company;

    @Getter
    @Setter
    @Transient
    String uid;

    @Builder
    public User(Long userEntryNo, String email, String name, String phone, Date registeredDate, UserActiveStatus userActiveStatus, Company company) {
        this.userEntryNo = userEntryNo;
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.registeredDate = registeredDate;
        this.userActiveStatus = userActiveStatus;
        this.company = company;
    }

    public User() {
    }

    public boolean isActiveUser(){
        if (this.userActiveStatus == UserActiveStatus.ACTIVE)
            return true;
        return false;
    }

    public void activateUser() {
        this.userActiveStatus = UserActiveStatus.ACTIVE;
    }

    public void deactivateUser() {
        this.userActiveStatus = UserActiveStatus.NOT_ACTIVE;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}