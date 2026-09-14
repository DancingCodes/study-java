package com.study.configdemo.controller;

import com.study.configdemo.common.Result;
import com.study.configdemo.config.AppProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app")
public class AppController {

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Autowired
    private AppProperties appProperties;

    @GetMapping("/info")
    public Result<AppProperties> info() {
        return Result.success(appProperties);
    }

    @GetMapping("/profile")
    public Result<String> profile() {
        return Result.success("当前环境：" + activeProfile);
    }
}
