package com.example.hello;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController          // 标记这是一个控制器，处理 HTTP 请求
public class HelloController {

    @GetMapping("/hello") // 浏览器访问 /hello 时执行这个方法
    public String hello() {
        return "Hello, Spring Boot!";
    }
}