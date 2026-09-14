package com.study.webdemo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 练习 1：商品实体类
// 要求：
// 1. 使用 @Data、@NoArgsConstructor、@AllArgsConstructor 注解
// 2. 字段：id(Long)、name(String)、price(Double)、stock(Integer)

// TODO: 在这里写代码
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    Long id;
    String name;
    Double price;
    Integer stock;
}