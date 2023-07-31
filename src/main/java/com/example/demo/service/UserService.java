package com.example.demo.service;

import com.example.demo.dao.UserRepository;
import com.example.demo.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 用户注册逻辑，包括密码加密和保存到数据库
    public ResponseEntity<?> registerUser(User user) {
        // 进行数据验证，确保用户名和密码的合法性
        if (user.getUserId() == null || user.getUserId().isEmpty() || user.getUserPwd() == null || user.getUserPwd().isEmpty()) {
            return ResponseEntity.badRequest().body("用户名和密码不能为空。");
        }

        // 检查用户名是否已存在
        User existingUser = userRepository.findByUserId(user.getUserId());
        if (existingUser != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("用户名已存在。");
        }

        // 检查用户名长度
        if (user.getUserId().length() < 3 || user.getUserId().length() > 10) {
            return ResponseEntity.badRequest().body("用户名长度必须在3到10个字符之间。");
        }

        // 检查用户名是否包含特殊字符
        if (!user.getUserId().matches("^[a-zA-Z0-9_]+$")) {
            return ResponseEntity.badRequest().body("用户名只能包含字母、数字和下划线。");
        }

        // 对密码进行加密后保存到数据库
        String encodedPassword = passwordEncoder.encode(user.getUserPwd());
        user.setUserPwd(encodedPassword);
        userRepository.save(user);

        return ResponseEntity.ok("用户注册成功!");
    }


    // 用户登录逻辑，包括密码验证
    public ResponseEntity<?> loginUser(User user) {
        // 根据用户名查找用户
        User existingUser = userRepository.findByUserId(user.getUserId());

        if (existingUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("账号不存在，请先注册。");
        }

        // 检查输入的密码是否与存储的密码匹配
        if (passwordEncoder.matches(user.getUserPwd(), existingUser.getUserPwd())) {
            return ResponseEntity.ok("登陆成功!");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("用户名或密码错误!");
        }
    }
}
