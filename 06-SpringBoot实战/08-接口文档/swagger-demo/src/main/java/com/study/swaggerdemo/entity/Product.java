package com.study.swaggerdemo.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "商品实体")
public class Product {

    @Schema(description = "商品ID", example = "1")
    private Long id;

    @Schema(description = "商品名称", example = "iPhone 15")
    private String name;

    @Schema(description = "价格", example = "5999.00")
    private Double price;

    @Schema(description = "库存数量", example = "100")
    private Integer stock;
}
