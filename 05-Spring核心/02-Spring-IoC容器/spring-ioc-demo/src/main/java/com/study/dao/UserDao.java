package com.study.dao;

import org.springframework.stereotype.Repository;

// 练习 1：数据层
// 已加好 @Repository，有 save 方法
@Repository
public class UserDao {
    public void save(String name) {
        System.out.println("保存用户：" + name);
    }
}
