package com.study.redisdemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.study.redisdemo.entity.Product;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {
}
