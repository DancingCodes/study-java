package com.study.service;

import org.springframework.stereotype.Component;

@Component("smsSender")
public class SmsSender implements MessageSender {
    public void send(String msg) {
        System.out.println("短信发送：" + msg);
    }
}
