package com.study.esdemo.service;

import com.study.esdemo.entity.ProductDoc;

import java.io.IOException;
import java.util.List;

public interface ProductSearchService {

    void save(ProductDoc productDoc);

    void deleteById(Long id);

    List<ProductDoc> search(String keyword, String category, Double minPrice, Double maxPrice, int page, int size) throws IOException;
}
