package com.study.mybatisplusdemo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.mybatisplusdemo.entity.Product;
import com.study.mybatisplusdemo.mapper.ProductMapper;
import com.study.mybatisplusdemo.service.ProductService;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {
}
