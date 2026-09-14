package com.study.exceptiondemo.entity;

import com.baomidou.mybatisplus.annotation.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("product")
public class Product {

    @TableId(type = IdType.AUTO)
    private Long id;

    @NotBlank(message = "商品名称不能为空")
    @Size(min = 2, max = 50, message = "商品名称长度必须在2-50之间")
    private String name;

    @NotNull(message = "价格不能为空")
    @DecimalMin(value = "0.01", message = "价格必须大于0")
    private BigDecimal price;

    @NotNull(message = "库存不能为空")
    @Min(value = 0, message = "库存不能为负数")
    private Integer stock;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}
