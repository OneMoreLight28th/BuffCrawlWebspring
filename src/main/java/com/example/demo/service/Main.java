package com.example.demo.service;

import com.example.demo.dao.UserDao;
import com.example.demo.entity.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * mybatis测试类
 */

public class Main {
    public static void main(String[] args) throws IOException {
        String resource = "configuration.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession session = sqlSessionFactory.openSession();

        try {
            UserDao userDao = session.getMapper(UserDao.class);

            // 查询用户信息
            User user = userDao.getUserById("12345");
            System.out.println(user);

            // 插入新用户信息
            User newUser = new User("67890", "newPassword");
            userDao.insertUser(newUser);
            session.commit();

        } finally {
            session.close();
        }
    }
}
