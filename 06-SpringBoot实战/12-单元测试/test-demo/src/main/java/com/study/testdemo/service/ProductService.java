package com.study.testdemo.service;

import com.study.testdemo.entity.Product;

public interface ProductService {

    Product getProductById(Long id);

    Product createProduct(Product product);
}
