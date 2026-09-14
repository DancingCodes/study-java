package com.study.testdemo.service.impl;

import com.study.testdemo.entity.Product;
import com.study.testdemo.exception.BusinessException;
import com.study.testdemo.mapper.ProductMapper;
import com.study.testdemo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Override
    public Product getProductById(Long id) {
        Product product = productMapper.selectById(id);
        if (product == null) {
            throw new BusinessException(404, "商品不存在");
        }
        return product;
    }

    @Override
    public Product createProduct(Product product) {
        productMapper.insert(product);
        return product;
    }
}
