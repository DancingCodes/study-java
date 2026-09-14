package com.study.redisdemo.service.impl;

import com.study.redisdemo.entity.Product;
import com.study.redisdemo.mapper.ProductMapper;
import com.study.redisdemo.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String PRODUCT_KEY_PREFIX = "product:";

    @Override
    public Product getById(Long id) {
        String key = PRODUCT_KEY_PREFIX + id;

        // 1. 先查 Redis
        Product product = (Product) redisTemplate.opsForValue().get(key);
        if (product != null) {
            log.info("缓存命中，key：{}", key);
            return product;
        }

        // 2. 缓存没有，查数据库
        log.info("缓存未命中，查询数据库，id：{}", id);
        product = productMapper.selectById(id);
        if (product == null) {
            // 缓存空值，防止缓存穿透
            redisTemplate.opsForValue().set(key, null, 5, TimeUnit.MINUTES);
            return null;
        }

        // 3. 写入缓存（30 分钟过期）
        redisTemplate.opsForValue().set(key, product, 30, TimeUnit.MINUTES);
        log.info("写入缓存，key：{}", key);
        return product;
    }

    @Override
    public List<Product> listAll() {
        return productMapper.selectList(null);
    }

    @Override
    public void save(Product product) {
        productMapper.insert(product);
        log.info("新增商品，id：{}", product.getId());
    }

    @Override
    public void update(Product product) {
        productMapper.updateById(product);
        // 更新后删除缓存
        String key = PRODUCT_KEY_PREFIX + product.getId();
        redisTemplate.delete(key);
        log.info("更新商品并删除缓存，key：{}", key);
    }

    @Override
    public void deleteById(Long id) {
        productMapper.deleteById(id);
        // 删除后清除缓存
        String key = PRODUCT_KEY_PREFIX + id;
        redisTemplate.delete(key);
        log.info("删除商品并清除缓存，key：{}", key);
    }
}
