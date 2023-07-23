package com.example.demo.dao;

import com.example.demo.entity.User;

public interface UserDao {
    User getUserById(String userId);

    void insertUser(User user);
}
