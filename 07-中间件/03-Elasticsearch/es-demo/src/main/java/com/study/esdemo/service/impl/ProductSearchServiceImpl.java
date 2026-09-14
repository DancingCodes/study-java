package com.study.esdemo.service.impl;

import com.study.esdemo.entity.ProductDoc;
import com.study.esdemo.repository.ProductDocRepository;
import com.study.esdemo.service.ProductSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class ProductSearchServiceImpl implements ProductSearchService {

    @Autowired
    private ProductDocRepository productDocRepository;

    @Override
    public void save(ProductDoc productDoc) {
        productDocRepository.save(productDoc);
        log.info("文档写入 ES，id：{}", productDoc.getId());
    }

    @Override
    public void deleteById(Long id) {
        productDocRepository.deleteById(id);
        log.info("文档从 ES 删除，id：{}", id);
    }

    // 练习 1：实现商品搜索
    // 要求：
    //   1. keyword 不为空时，搜索 name 字段（用 findByName）
    //   2. category 不为空时，按分类过滤（用 findByCategory）
    //   3. minPrice 和 maxPrice 都不为空时，按价格范围过滤（用 findByPriceBetween）
    //   4. 如果什么条件都没有，返回全部（用 productDocRepository.findAll()）
    // 提示：可以先实现简单版本，每个条件单独处理
    @Override
    public List<ProductDoc> search(String keyword, String category, Double minPrice, Double maxPrice, int page, int size) throws IOException {
        // TODO: 在这里写代码
        return List.of();
    }
}
