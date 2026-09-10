package com.study;

import com.study.service.UserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(AppConfig.class);

        UserService userService = context.getBean(UserService.class);

        // register 有 @Log → 会打印日志
        userService.register("张三");

        System.out.println("---");

        // delete 没有 @Log → 不会打印日志
        userService.delete("李四");

        context.close();
    }
}
