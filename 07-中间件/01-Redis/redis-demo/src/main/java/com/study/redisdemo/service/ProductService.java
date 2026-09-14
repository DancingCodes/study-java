package com.study.redisdemo.service;

import com.study.redisdemo.entity.Product;

import java.util.List;

public interface ProductService {

    Product getById(Long id);

    List<Product> listAll();

    void save(Product product);

    void update(Product product);

    void deleteById(Long id);
}
