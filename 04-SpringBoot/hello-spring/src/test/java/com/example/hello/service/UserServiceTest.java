package com.example.hello.service;

import com.example.hello.exception.UserNotFoundException;
import com.example.hello.model.User;
import com.example.hello.repository.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)  // 启用 Mockito
class UserServiceTest {

    @Mock                     // 模拟 UserMapper（不真的查数据库）
    private UserMapper userMapper;

    @InjectMocks              // 把上面的 Mock 注入到 UserService 里
    private UserService userService;

    @Test
    void 查所有用户_返回列表() {
        // Given：模拟数据库返回两个用户
        List<User> fakeUsers = Arrays.asList(
                new User("张三", 25),
                new User("李四", 30)
        );
        when(userMapper.selectList(null)).thenReturn(fakeUsers);

        // When：调用 Service 方法
        List<User> result = userService.getAllUsers();

        // Then：验证结果
        assertEquals(2, result.size());
        assertEquals("张三", result.get(0).getName());
    }

    @Test
    void 按id查用户_存在_返回用户() {
        // Given
        User fakeUser = new User("张三", 25);
        when(userMapper.selectById(1)).thenReturn(fakeUser);

        // When
        User result = userService.getUserById(1);

        // Then
        assertEquals("张三", result.getName());
    }

    @Test
    void 按id查用户_不存在_抛异常() {
        // Given：模拟数据库返回 null
        when(userMapper.selectById(999)).thenReturn(null);

        // When & Then：应该抛 UserNotFoundException
        assertThrows(UserNotFoundException.class, () -> {
            userService.getUserById(999);
        });
    }

    @Test
    void 创建用户_调用了mapper的insert() {
        // Given
        User newUser = new User("王五", 20);

        // When
        userService.createUser(newUser);

        // Then：验证 mapper.insert() 被调用了 1 次
        verify(userMapper, times(1)).insert(newUser);
    }

    @Test
    void 删除用户_调用了mapper的deleteById() {
        // When
        userService.deleteUser(1);

        // Then
        verify(userMapper, times(1)).deleteById(1);
    }
}
