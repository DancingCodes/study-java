package com.study.dao;

import org.springframework.stereotype.Repository;

@Repository
public class UserDao {
    public void save(String name) {
        System.out.println("保存用户：" + name);
    }
}
