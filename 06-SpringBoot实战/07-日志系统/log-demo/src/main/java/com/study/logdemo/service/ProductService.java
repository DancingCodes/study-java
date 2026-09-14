package com.study.logdemo.service;

import com.study.logdemo.entity.Product;

import java.util.List;

public interface ProductService {

    List<Product> findAll();

    Product findById(Long id);

    Product create(Product product);

    void deleteById(Long id);
}
