package com.study.logdemo.service.impl;

import com.study.logdemo.entity.Product;
import com.study.logdemo.exception.BusinessException;
import com.study.logdemo.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    private final List<Product> products = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public ProductServiceImpl() {
        products.add(new Product(idGenerator.getAndIncrement(), "Java编程思想", new BigDecimal("108.00"), 50));
        products.add(new Product(idGenerator.getAndIncrement(), "Spring Boot实战", new BigDecimal("89.00"), 30));
        products.add(new Product(idGenerator.getAndIncrement(), "深入理解JVM", new BigDecimal("129.00"), 20));
    }

    @Override
    public List<Product> findAll() {
        log.debug("查询所有商品，当前共 {} 条", products.size());
        return products;
    }

    @Override
    public Product findById(Long id) {
        log.debug("根据 ID 查询商品，ID：{}", id);
        return products.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> {
                    log.warn("商品不存在，ID：{}", id);
                    return new BusinessException(404, "商品不存在");
                });
    }

    @Override
    public Product create(Product product) {
        product.setId(idGenerator.getAndIncrement());
        products.add(product);
        log.debug("创建商品成功：{}", product);
        return product;
    }

    @Override
    public void deleteById(Long id) {
        log.debug("删除商品，ID：{}", id);
        boolean removed = products.removeIf(p -> p.getId().equals(id));
        if (!removed) {
            log.warn("删除失败，商品不存在，ID：{}", id);
            throw new BusinessException(404, "商品不存在");
        }
        log.info("商品已删除，ID：{}", id);
    }
}
