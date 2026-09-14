package com.study.validationdemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.study.validationdemo.entity.Product;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {
}
