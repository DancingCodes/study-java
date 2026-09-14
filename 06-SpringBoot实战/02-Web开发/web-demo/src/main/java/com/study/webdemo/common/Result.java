package com.study.webdemo.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 练习 1：统一响应类
// 要求：
// 1. 使用泛型 <T> 让 data 字段支持任意类型
// 2. 用 @Data、@NoArgsConstructor、@AllArgsConstructor 注解
// 3. 字段：code(Integer) — 状态码，message(String) — 提示信息，data(T) — 返回数据
// 4. 提供三个静态方法：
//    - success(T data) — 成功，code=200，message="操作成功"，携带数据
//    - success()       — 成功，code=200，message="操作成功"，data 为 null
//    - error(Integer code, String message) — 失败，自定义 code 和 message，data 为 null

// TODO: 在这里写代码
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    private Integer code;
    private String message;
    private T data;

    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功", data);
    }

    public static <T> Result<T> success() {
        return new Result<>(200, "操作成功", null);
    }

    public static <T> Result<T> error(Integer code, String message) {
        return new Result<>(code, message, null);
    }
}