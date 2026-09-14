package com.study.webdemo.controller;

import com.study.webdemo.common.Result;
import com.study.webdemo.entity.Product;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 练习 1：商品管理接口
// 要求：
// 1. 使用 @RestController 标记类
// 2. 使用 @RequestMapping("/products") 设置公共路径前缀
// 3. 实现以下 5 个接口（现在没有数据库，用假数据模拟返回）

// TODO: 在这里加上类注解
@RestController
@RequestMapping("/products")
public class ProductController {

    // 练习 1-1：根据 ID 查询商品
    // 接口：GET /products/{id}
    // 参数：路径参数 id (Long)
    // 返回：Result<Product>，返回一个模拟的商品对象
    // 提示：用 @GetMapping("/{id}") + @PathVariable

    // TODO: 在这里写代码
    @GetMapping("/{id}")
    public Result<Product> getById(@PathVariable Long id) {
        Product product = new Product(id, "手机", 2999.0, 100);
        return Result.success(product);
    }

    // 练习 1-2：搜索商品
    // 接口：GET /products?name=xxx&minPrice=0&maxPrice=100
    // 参数：三个查询参数都是可选的 — name(String), minPrice(Double), maxPrice(Double)
    // 返回：Result<List<Product>>，返回一个模拟的商品列表（2-3个商品）
    // 提示：用 @GetMapping + @RequestParam(required = false)

    // TODO: 在这里写代码
    @GetMapping
    public Result<List<Product>> list(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice
    ) {
        List<Product> products = List.of(
                new Product(1L, "手机", 2999.0, 100),
                new Product(2L, "笔记本电脑", 5999.0, 50)
        );
        return Result.success(products);
    }

    // 练习 1-3：创建商品
    // 接口：POST /products
    // 参数：请求体 JSON，用 Product 对象接收
    // 返回：Result<Product>，给商品设置一个模拟的 id（比如 1L）后返回
    // 提示：用 @PostMapping + @RequestBody

    // TODO: 在这里写代码
    @PostMapping
    public Result<Product> create(@RequestBody Product product) {
        product.setId(1L);
        return Result.success(product);
    }

    // 练习 1-4：更新商品
    // 接口：PUT /products/{id}
    // 参数：路径参数 id (Long) + 请求体 JSON (Product 对象)
    // 返回：Result<Product>，把 id 设置到 product 对象里然后返回
    // 提示：用 @PutMapping("/{id}") + @PathVariable + @RequestBody

    // TODO: 在这里写代码
    @PutMapping("/{id}")
    public Result<Product> update(@PathVariable Long id,@RequestBody Product product) {
        product.setId(id);
        return Result.success(product);
    }


    // 练习 1-5：删除商品
    // 接口：DELETE /products/{id}
    // 参数：路径参数 id (Long)
    // 返回：Result<Void>，无数据返回
    // 提示：用 @DeleteMapping("/{id}") + @PathVariable

    // TODO: 在这里写代码
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        return Result.success();
    }
}
