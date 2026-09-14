package com.study.dockerdemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HealthController {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping("/")
    public Map<String, Object> index() {
        Map<String, Object> result = new HashMap<>();
        result.put("message", "Docker Demo 运行成功！");
        result.put("timestamp", System.currentTimeMillis());
        return result;
    }

    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> result = new HashMap<>();

        // 检查 MySQL
        try (Connection conn = dataSource.getConnection()) {
            result.put("mysql", "连接成功");
        } catch (Exception e) {
            result.put("mysql", "连接失败：" + e.getMessage());
        }

        // 检查 Redis
        try {
            redisTemplate.opsForValue().set("health-check", "ok");
            result.put("redis", "连接成功");
        } catch (Exception e) {
            result.put("redis", "连接失败：" + e.getMessage());
        }

        return result;
    }
}
