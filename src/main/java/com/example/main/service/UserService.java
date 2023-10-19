package com.example.main.service;

import com.example.main.Utils.JwtUtil;
import com.example.main.mapper.UserMapper;
import com.example.main.entity.User;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class UserService {

    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserMapper userMapper, BCryptPasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    // 用户注册逻辑，包括密码加密和保存到数据库
    public ResponseEntity<?> registerUser(User user) {
        // 进行数据验证，确保用户名和密码的合法性
        if (user.getUserId() == null || user.getUserId().isEmpty() || user.getUserPwd() == null || user.getUserPwd().isEmpty()) {
            return ResponseEntity.badRequest().body("用户名和密码不能为空。");
        }

        // 检查用户名是否已存在
        User existingUser = userMapper.findByUserId(user.getUserId());
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
        userMapper.save(user);

        return ResponseEntity.ok("用户注册成功!");
    }

    public ResponseEntity<?> loginUser(User user) {
        // 根据用户名查找用户
        User existingUser = userMapper.findByUserId(user.getUserId());

        if (existingUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("账号不存在，请先注册。");
        }

        // 检查输入的密码是否与存储的密码匹配
        if (passwordEncoder.matches(user.getUserPwd(), existingUser.getUserPwd())) {
            // 校验成功，生成JWT，并将用户名添加到JWT声明中
            String jwt = JwtUtil.createJWT(existingUser.getUserId(), JwtUtil.JWT_TTL);
            System.out.println(jwt);
            // 将JWT放入响应头
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + jwt);
            headers.add("Access-Control-Expose-Headers", "Authorization");

            return ResponseEntity.ok().headers(headers).body("登录成功!");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("用户名或密码错误!");
        }

    }

    public JwtUtil.TokenValidationResult checkAuthentication(HttpServletRequest request) {
        // 获取 Authorization 字段，JWT token
        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // 去掉前缀 "Bearer "
            System.out.println(token);
            try {
                // 验证 token
                return JwtUtil.parseJWT(token);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 验证失败或没有提供 token，返回未授权状态
        return new JwtUtil.TokenValidationResult(false);
    }
}

