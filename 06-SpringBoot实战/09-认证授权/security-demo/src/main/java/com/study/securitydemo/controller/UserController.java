package com.study.securitydemo.controller;

import com.study.securitydemo.common.Result;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping("/me")
    public Result<Map<String, String>> me(@AuthenticationPrincipal UserDetails userDetails) {
        return Result.success(Map.of("username", userDetails.getUsername()));
    }
}
