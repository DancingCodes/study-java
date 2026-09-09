package com.example.hello.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        // 每个测试方法执行前，创建一个 JwtUtil 实例
        // 密钥至少 32 字节，过期时间 1 小时
        jwtUtil = new JwtUtil(
                "test-secret-key-must-be-at-least-32-bytes-long",
                3600000
        );
    }

    @Test
    void 生成token后能解析出用户名() {
        String token = jwtUtil.generateToken("admin");

        String username = jwtUtil.getUsername(token);

        assertEquals("admin", username);
    }

    @Test
    void 正常token验证通过() {
        String token = jwtUtil.generateToken("admin");

        assertTrue(jwtUtil.isValid(token));
    }

    @Test
    void 瞎编的token验证失败() {
        assertFalse(jwtUtil.isValid("this.is.fake"));
    }

    @Test
    void 过期的token验证失败() {
        // 过期时间设为 0 毫秒，生成的 token 立即过期
        JwtUtil expiredJwtUtil = new JwtUtil(
                "test-secret-key-must-be-at-least-32-bytes-long",
                0
        );
        String token = expiredJwtUtil.generateToken("admin");

        assertFalse(expiredJwtUtil.isValid(token));
    }
}
