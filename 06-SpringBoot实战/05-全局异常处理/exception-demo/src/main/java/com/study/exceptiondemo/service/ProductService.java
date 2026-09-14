package com.study.exceptiondemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.study.exceptiondemo.entity.Product;

public interface ProductService extends IService<Product> {

    Product getProductById(Long id);
}
