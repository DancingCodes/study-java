package com.study.rabbitmqdemo.controller;

import com.study.rabbitmqdemo.common.Result;
import com.study.rabbitmqdemo.dto.OrderDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // 练习 1：发送 Direct 消息
    // POST /order — 接收 OrderDTO，发送到 order.exchange，routing key 为 "order"
    // TODO: 在这里写代码

    // 练习 2：发送 Fanout 广播消息
    // POST /order/notify — 接收一个字符串消息，发送到 notify.exchange
    // 提示：Fanout 交换机不看 routing key，第二个参数传空字符串 ""
    // TODO: 在这里写代码
}
