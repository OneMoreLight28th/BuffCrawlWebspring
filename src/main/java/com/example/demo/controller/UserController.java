package com.example.demo.controller;

import com.example.demo.Utils.JwtUtil;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


/**
 * 账户登录
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        return userService.registerUser(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        return userService.loginUser(user);
    }

    @GetMapping("/check-auth")
    public ResponseEntity<String> checkAuthentication(HttpServletRequest request) {
        JwtUtil.TokenValidationResult validationResult = userService.checkAuthentication(request);

        if (validationResult.isValid()) {

            return ResponseEntity.ok("登录成功"); // 将用户名返回给前端
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("用户未登录或登录失效");
        }
    }
}
