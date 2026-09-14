package com.study.logdemo.controller;

import com.study.logdemo.common.Result;
import com.study.logdemo.entity.Product;
import com.study.logdemo.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public Result<List<Product>> list() {
        log.info("收到请求：查询全部商品");
        List<Product> products = productService.findAll();
        log.info("查询完成，返回 {} 条商品", products.size());
        return Result.success(products);
    }

    @GetMapping("/{id}")
    public Result<Product> getById(@PathVariable Long id) {
        log.info("收到请求：查询商品，ID：{}", id);
        Product product = productService.findById(id);
        if (product == null) {
            log.warn("商品不存在，ID：{}", id);
            return Result.error(404, "商品不存在");
        }
        return Result.success(product);
    }

    @PostMapping
    public Result<Product> create(@RequestBody Product product) {
        log.info("收到请求：新增商品，名称：{}，价格：{}", product.getName(), product.getPrice());
        Product created = productService.create(product);
        return Result.success(created);
    }

    @PutMapping("/{id}")
    public Result<Product> update(@PathVariable Long id, @RequestBody Product product) {
        log.info("收到请求：更新商品，ID：{}", id);
        Product updated = productService.update(id, product);
        if (updated == null) {
            log.warn("更新失败，商品不存在，ID：{}", id);
            return Result.error(404, "商品不存在");
        }
        return Result.success(updated);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        log.info("收到请求：删除商品，ID：{}", id);
        boolean deleted = productService.delete(id);
        if (!deleted) {
            log.warn("删除失败，商品不存在，ID：{}", id);
            return Result.error(404, "商品不存在");
        }
        return Result.success();
    }
}
