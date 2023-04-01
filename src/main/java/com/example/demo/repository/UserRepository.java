package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    void deleteByEmail(String email);
    //sql 테스트
    @Query("SELECT u FROM ds_user u WHERE u.del_Flag_YN = 'N'")
    List<User> findAllNotDeleted();
}