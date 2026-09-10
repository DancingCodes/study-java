package com.study.service;

import com.study.annotation.Log;
import com.study.dao.UserDao;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Log("用户注册")
    public void register(String name) {
        userDao.save(name);
    }

    public void delete(String name) {
        System.out.println("删除用户：" + name);
    }
}
