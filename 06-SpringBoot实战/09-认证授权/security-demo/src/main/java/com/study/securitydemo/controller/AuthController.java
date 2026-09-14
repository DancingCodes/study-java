package com.study.securitydemo.controller;

import com.study.securitydemo.common.Result;
import com.study.securitydemo.dto.LoginRequest;
import com.study.securitydemo.util.JwtUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil;

    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public Result<Map<String, String>> login(@RequestBody LoginRequest request) {
        if ("admin".equals(request.getUsername()) && "123456".equals(request.getPassword())) {
            String token = jwtUtil.generateToken(request.getUsername());
            return Result.success(Map.of("token", token));
        }
        return Result.error(401, "用户名或密码错误");
    }
}
