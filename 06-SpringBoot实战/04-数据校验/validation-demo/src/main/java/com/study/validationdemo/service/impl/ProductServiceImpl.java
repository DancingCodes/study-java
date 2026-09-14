package com.study.validationdemo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.validationdemo.entity.Product;
import com.study.validationdemo.mapper.ProductMapper;
import com.study.validationdemo.service.ProductService;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {
}
