package com.study.scheduledemo.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class ProductTask {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Scheduled(fixedRate = 5000)
    public void printTime() {
        log.info("[fixedRate] 当前时间：{}", LocalDateTime.now().format(FMT));
    }

    @Scheduled(cron = "0 0/1 * * * ?")
    public void syncData() {
        log.info("[cron] 开始模拟数据同步...");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        log.info("[cron] 数据同步完成");
    }

    public String runManually() {
        String time = LocalDateTime.now().format(FMT);
        log.info("[手动触发] 任务执行，时间：{}", time);
        return "任务已手动执行，时间：" + time;
    }
}
