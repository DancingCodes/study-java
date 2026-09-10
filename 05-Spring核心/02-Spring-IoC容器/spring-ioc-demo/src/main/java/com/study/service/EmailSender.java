package com.study.service;

import org.springframework.stereotype.Component;

@Component("emailSender")
public class EmailSender implements MessageSender {
    public void send(String msg) {
        System.out.println("邮件发送：" + msg);
    }
}
