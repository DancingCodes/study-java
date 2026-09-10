package com.study;

import com.study.service.UserService;
import com.study.service.NotifyService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {
    public static void main(String[] args) {
        // 1. 创建 Spring 容器
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(AppConfig.class);

        // 2. 测试 UserService
        UserService userService = context.getBean(UserService.class);
        userService.register("张三");

        // 3. 测试 NotifyService（@Qualifier 指定注入 emailSender）
        NotifyService notifyService = context.getBean(NotifyService.class);
        notifyService.notify("你的订单已发货");

        // 4. 关闭容器
        context.close();
    }
}
