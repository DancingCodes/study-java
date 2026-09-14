package com.study.exceptiondemo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.exceptiondemo.entity.Product;
import com.study.exceptiondemo.exception.BusinessException;
import com.study.exceptiondemo.mapper.ProductMapper;
import com.study.exceptiondemo.service.ProductService;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Override
    public Product getProductById(Long id) {
        Product product = this.getById(id);
        if (product == null) {
            throw new BusinessException(404, "商品不存在");
        }
        return product;
    }
}
