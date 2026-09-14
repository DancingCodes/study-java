package com.study.redisdemo.controller;

import com.study.redisdemo.common.Result;
import com.study.redisdemo.entity.Product;
import com.study.redisdemo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/{id}")
    public Result<Product> getById(@PathVariable Long id) {
        Product product = productService.getById(id);
        return Result.success(product);
    }

    @GetMapping
    public Result<List<Product>> listAll() {
        return Result.success(productService.listAll());
    }

    @PostMapping
    public Result<?> save(@RequestBody Product product) {
        productService.save(product);
        return Result.success("新增成功");
    }

    @PutMapping("/{id}")
    public Result<?> update(@PathVariable Long id, @RequestBody Product product) {
        product.setId(id);
        productService.update(product);
        return Result.success("更新成功");
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        productService.deleteById(id);
        return Result.success("删除成功");
    }
}
