package com.example.hello.service;

import com.example.hello.exception.UserNotFoundException;
import com.example.hello.model.User;
import com.example.hello.repository.UserMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {

    private final UserMapper userMapper;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public List<User> getAllUsers() {
        return userMapper.selectList(null);    // null 表示没有查询条件，查全部
    }

    public User getUserById(int id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new UserNotFoundException(id);
        }
        return user;
    }

    public User createUser(User user) {
        userMapper.insert(user);
        return user;
    }

    public User updateUser(int id, User user) {
        user.setId(id);
        userMapper.updateById(user);
        return user;
    }

    public void deleteUser(int id) {
        userMapper.deleteById(id);
    }
}
