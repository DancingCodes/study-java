package com.study.springbootdemo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// TODO: 练习 2 — 写一个 Hello 接口
// 要求：
// 1. 用 @RestController 标记类
// 2. 写一个 hello() 方法，映射到 GET /hello
// 3. 返回字符串 "Hello, Spring Boot!"
// 4. 启动项目后浏览器访问 http://localhost:8080/hello 验证

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        // TODO: 在这里写代码，返回 "Hello, Spring Boot!"
        return "Hello, Spring Boot!";
    }
}
