package com.study.exceptiondemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.study.exceptiondemo.entity.Product;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {
}
