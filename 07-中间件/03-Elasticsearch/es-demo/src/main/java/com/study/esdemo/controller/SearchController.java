package com.study.esdemo.controller;

import com.study.esdemo.common.Result;
import com.study.esdemo.entity.ProductDoc;
import com.study.esdemo.service.ProductSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private ProductSearchService productSearchService;

    // 练习 2：新增商品到 ES（模拟数据同步）
    // POST /search/product — 接收 ProductDoc，保存到 ES
    // TODO: 在这里写代码

    // 练习 2：从 ES 删除商品
    // DELETE /search/product/{id} — 根据 id 删除
    // TODO: 在这里写代码

    // 练习 1：搜索商品
    // GET /search/product — 接收 keyword、category、minPrice、maxPrice、page、size 参数
    // TODO: 在这里写代码
}
