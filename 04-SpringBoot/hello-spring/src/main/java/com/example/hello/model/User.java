package com.example.hello.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@TableName("users")
public class User {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @NotBlank(message = "姓名不能为空")           // 不能为 null、""、"  "
    private String name;

    @NotNull(message = "年龄不能为空")             // 不能为 null
    @Min(value = 0, message = "年龄不能小于0")     // 最小值
    @Max(value = 150, message = "年龄不能大于150") // 最大值
    private Integer age;

    public User() {}

    public User(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
}
