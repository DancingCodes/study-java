package com.example.hello.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.hello.model.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper   // 告诉 MyBatis：这是一个数据访问接口
public interface UserMapper extends BaseMapper<User> {
    // 继承 BaseMapper 后，增删改查方法自动就有了：
    // selectList()  — 查所有
    // selectById()  — 按 id 查
    // insert()      — 新增
    // updateById()  — 按 id 更新
    // deleteById()  — 按 id 删除
}
