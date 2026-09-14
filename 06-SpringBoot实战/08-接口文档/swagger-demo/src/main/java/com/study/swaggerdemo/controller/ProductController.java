package com.study.swaggerdemo.controller;

import com.study.swaggerdemo.common.Result;
import com.study.swaggerdemo.entity.Product;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "商品管理", description = "商品的增删改查接口")
@RestController
@RequestMapping("/products")
public class ProductController {

    @Operation(summary = "根据ID查询商品", description = "传入商品ID，返回商品详情")
    @GetMapping("/{id}")
    public Result<Product> getById(
            @Parameter(description = "商品ID") @PathVariable Long id) {
        Product product = new Product(id, "iPhone 15", 5999.0, 100);
        return Result.success(product);
    }

    @Operation(summary = "查询全部商品")
    @GetMapping
    public Result<List<Product>> list() {
        List<Product> products = List.of(
                new Product(1L, "iPhone 15", 5999.0, 100),
                new Product(2L, "小米14", 3999.0, 200),
                new Product(3L, "华为Mate60", 6999.0, 50)
        );
        return Result.success(products);
    }

    @Operation(summary = "搜索商品", description = "按名称模糊搜索，支持价格范围筛选")
    @GetMapping("/search")
    public Result<List<Product>> search(
            @Parameter(description = "商品名称（模糊搜索）") @RequestParam(required = false) String name,
            @Parameter(description = "最低价格") @RequestParam(required = false) Double minPrice,
            @Parameter(description = "最高价格") @RequestParam(required = false) Double maxPrice) {
        List<Product> products = List.of(
                new Product(1L, "iPhone 15", 5999.0, 100),
                new Product(3L, "华为Mate60", 6999.0, 50)
        );
        return Result.success(products);
    }

    @Operation(summary = "创建商品")
    @PostMapping
    public Result<Product> create(@RequestBody Product product) {
        product.setId(1L);
        return Result.success(product);
    }

    @Operation(summary = "更新商品")
    @PutMapping("/{id}")
    public Result<Product> update(
            @Parameter(description = "商品ID") @PathVariable Long id,
            @RequestBody Product product) {
        product.setId(id);
        return Result.success(product);
    }

    @Operation(summary = "删除商品")
    @DeleteMapping("/{id}")
    public Result<Void> delete(
            @Parameter(description = "商品ID") @PathVariable Long id) {
        return Result.success();
    }
}
