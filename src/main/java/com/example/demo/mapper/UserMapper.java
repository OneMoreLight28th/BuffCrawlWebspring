package com.example.demo.mapper;


import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 账户接口
 */

@Repository
public interface UserMapper extends JpaRepository<User, Long> {
    User findByUserId(String username);
}