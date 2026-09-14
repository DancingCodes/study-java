package com.study.rabbitmqdemo.consumer;

import com.study.rabbitmqdemo.dto.OrderDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderConsumer {

    // 练习 1：监听 order.queue，接收 OrderDTO 对象并打印日志
    // 提示：用 @RabbitListener(queues = "order.queue") 注解
    // TODO: 在这里写代码
}
