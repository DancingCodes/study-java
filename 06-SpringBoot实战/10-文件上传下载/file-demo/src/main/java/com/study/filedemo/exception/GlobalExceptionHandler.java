package com.study.filedemo.exception;

import com.study.filedemo.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public Result<Void> handleMaxUploadSize(MaxUploadSizeExceededException e) {
        log.warn("文件大小超出限制：{}", e.getMessage());
        return Result.error(400, "文件大小超出限制，单个文件最大10MB，请求总大小最大50MB");
    }

    @ExceptionHandler(RuntimeException.class)
    public Result<Void> handleRuntime(RuntimeException e) {
        log.error("运行时异常：{}", e.getMessage(), e);
        return Result.error(500, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        log.error("系统异常：{}", e.getMessage(), e);
        return Result.error(500, "系统内部错误");
    }
}
