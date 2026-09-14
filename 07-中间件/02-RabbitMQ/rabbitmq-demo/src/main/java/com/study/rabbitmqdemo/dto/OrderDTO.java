package com.study.rabbitmqdemo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO implements Serializable {

    private String orderId;
    private String productName;
    private BigDecimal amount;
}
