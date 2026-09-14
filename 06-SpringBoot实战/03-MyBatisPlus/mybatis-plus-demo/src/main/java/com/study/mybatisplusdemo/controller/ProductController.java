package com.study.mybatisplusdemo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.study.mybatisplusdemo.common.Result;
import com.study.mybatisplusdemo.entity.Product;
import com.study.mybatisplusdemo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/{id}")
    public Result<Product> getById(@PathVariable Long id) {
        Product product = productService.getById(id);
        return Result.success(product);
    }

    @GetMapping
    public Result<List<Product>> list() {
        List<Product> products = productService.list();
        return Result.success(products);
    }

    @PostMapping
    public Result<Product> create(@RequestBody Product product) {
        productService.save(product);
        return Result.success(product);
    }

    @PutMapping("/{id}")
    public Result<Product> update(@PathVariable Long id, @RequestBody Product product) {
        product.setId(id);
        productService.updateById(product);
        return Result.success(product);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        productService.removeById(id);
        return Result.success();
    }

    @GetMapping("/search")
    public Result<List<Product>> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice
    ) {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(name != null, Product::getName, name)
               .ge(minPrice != null, Product::getPrice, minPrice)
               .le(maxPrice != null, Product::getPrice, maxPrice)
               .orderByAsc(Product::getPrice);

        List<Product> products = productService.list(wrapper);
        return Result.success(products);
    }

    @GetMapping("/page")
    public Result<IPage<Product>> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String name
    ) {
        Page<Product> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(name != null, Product::getName, name);

        IPage<Product> result = productService.page(page, wrapper);
        return Result.success(result);
    }
}
