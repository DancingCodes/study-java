package com.study.rabbitmqdemo.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    // ========== JSON 消息转换器 ==========

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // ========== 练习 1：Direct 模式 ==========

    // TODO: 创建一个名为 "order.queue" 的持久化队列

    // TODO: 创建一个名为 "order.exchange" 的 Direct 交换机

    // TODO: 将 order.queue 绑定到 order.exchange，routing key 为 "order"

    // ========== 练习 2：Fanout 广播模式 ==========

    // TODO: 创建一个名为 "notify.exchange" 的 Fanout 交换机

    // TODO: 创建一个名为 "notify.sms.queue" 的持久化队列（短信队列）

    // TODO: 创建一个名为 "notify.email.queue" 的持久化队列（邮件队列）

    // TODO: 将 notify.sms.queue 绑定到 notify.exchange

    // TODO: 将 notify.email.queue 绑定到 notify.exchange
}
