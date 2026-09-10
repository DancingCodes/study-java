package com.study.aspect;

import com.study.annotation.Log;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect {

    @Around("@annotation(log)")
    public Object around(ProceedingJoinPoint joinPoint, Log log) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String desc = log.value().isEmpty() ? methodName : log.value();

        long start = System.currentTimeMillis();
        System.out.println("[日志] " + desc + " 开始");

        Object result = joinPoint.proceed();

        long end = System.currentTimeMillis();
        System.out.println("[日志] " + desc + " 结束，耗时：" + (end - start) + "ms");

        return result;
    }
}
