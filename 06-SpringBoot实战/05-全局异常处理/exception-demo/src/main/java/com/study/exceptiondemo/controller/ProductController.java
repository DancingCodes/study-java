package com.study.exceptiondemo.controller;

import com.study.exceptiondemo.common.Result;
import com.study.exceptiondemo.entity.Product;
import com.study.exceptiondemo.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/{id}")
    public Result<Product> getById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return Result.success(product);
    }

    @GetMapping
    public Result<List<Product>> list() {
        List<Product> products = productService.list();
        return Result.success(products);
    }

    @PostMapping
    public Result<Product> create(@Valid @RequestBody Product product) {
        productService.save(product);
        return Result.success(product);
    }

    @PutMapping("/{id}")
    public Result<Product> update(@PathVariable Long id, @Valid @RequestBody Product product) {
        product.setId(id);
        productService.updateById(product);
        return Result.success(product);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        productService.removeById(id);
        return Result.success();
    }
}
