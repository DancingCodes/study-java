package com.study.testdemo.mapper;

import com.study.testdemo.entity.Product;

public interface ProductMapper {

    Product selectById(Long id);

    int insert(Product product);
}
