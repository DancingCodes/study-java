package com.example.hello.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppInfoController {

    @Value("${app.name}")        // 从 yml 读取 app.name 的值
    private String appName;

    @Value("${app.version}")     // 从 yml 读取 app.version 的值
    private String appVersion;

    @GetMapping("/info")
    public String info() {
        return appName + " v" + appVersion;
    }
}
