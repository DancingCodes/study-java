package com.study.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class NotifyService {
    @Autowired
    @Qualifier("emailSender")
    private MessageSender sender;

    public void notify(String msg) {
        sender.send(msg);
    }
}
