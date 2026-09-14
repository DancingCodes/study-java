package com.study.esdemo.repository;

import com.study.esdemo.entity.ProductDoc;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ProductDocRepository extends ElasticsearchRepository<ProductDoc, Long> {

    // 练习 1：根据 name 搜索（Spring Data ES 自动实现）
    List<ProductDoc> findByName(String name);

    // 练习 1：根据价格范围查询
    List<ProductDoc> findByPriceBetween(Double min, Double max);

    // 练习 1：根据分类精确查询
    List<ProductDoc> findByCategory(String category);
}
