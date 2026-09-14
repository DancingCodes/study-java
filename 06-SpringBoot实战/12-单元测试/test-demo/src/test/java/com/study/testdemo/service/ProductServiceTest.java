package com.study.testdemo.service;

import com.study.testdemo.entity.Product;
import com.study.testdemo.exception.BusinessException;
import com.study.testdemo.mapper.ProductMapper;
import com.study.testdemo.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void testGetProductByIdSuccess() {
        Product mockProduct = new Product(1L, "手机", 2999.0, 100);
        when(productMapper.selectById(1L)).thenReturn(mockProduct);

        Product result = productService.getProductById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("手机", result.getName());
        assertEquals(2999.0, result.getPrice());
        assertEquals(100, result.getStock());
        verify(productMapper, times(1)).selectById(1L);
    }

    @Test
    void testGetProductByIdNotFound() {
        when(productMapper.selectById(999L)).thenReturn(null);

        BusinessException ex = assertThrows(BusinessException.class, () -> {
            productService.getProductById(999L);
        });

        assertEquals(404, ex.getCode());
        assertEquals("商品不存在", ex.getMessage());
        verify(productMapper, times(1)).selectById(999L);
    }

    @Test
    void testCreateProduct() {
        Product newProduct = new Product(null, "笔记本电脑", 5999.0, 50);
        when(productMapper.insert(newProduct)).thenReturn(1);

        Product result = productService.createProduct(newProduct);

        assertNotNull(result);
        assertEquals("笔记本电脑", result.getName());
        assertEquals(5999.0, result.getPrice());
        verify(productMapper, times(1)).insert(newProduct);
    }
}
