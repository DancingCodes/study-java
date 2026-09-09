package com.example.hello.controller;

import com.example.hello.model.LoginRequest;
import com.example.hello.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
public class AuthController {

    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    // 模拟的用户数据（实际项目会从数据库查）
    // 密码是 "123456" 的 BCrypt 加密结果
    private final String fakeUsername = "admin";
    private final String fakePassword = "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy";

    public AuthController(JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public Map<String, String> login(@Valid @RequestBody LoginRequest request) {

        // 1. 验证用户名
        if (!fakeUsername.equals(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "用户名或密码错误");
        }

        // 2. 验证密码（用 BCrypt 比对）
        if (!passwordEncoder.matches(request.getPassword(), fakePassword)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "用户名或密码错误");
        }

        // 3. 生成 token 返回
        String token = jwtUtil.generateToken(request.getUsername());
        return Map.of("token", token);
    }
}
