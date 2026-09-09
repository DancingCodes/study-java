package com.example.hello.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(int id) {
        super("用户不存在，id=" + id);
    }
}
