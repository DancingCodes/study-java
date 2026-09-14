package com.study.redisdemo.controller;

import com.study.redisdemo.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/redis")
public class RedisController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // ========== String 操作 ==========

    @PostMapping("/string")
    public Result<?> setString(@RequestParam String key, @RequestParam String value) {
        redisTemplate.opsForValue().set(key, value);
        return Result.success("设置成功");
    }

    @GetMapping("/string")
    public Result<?> getString(@RequestParam String key) {
        Object value = redisTemplate.opsForValue().get(key);
        return Result.success(value);
    }

    @DeleteMapping("/string")
    public Result<?> deleteString(@RequestParam String key) {
        redisTemplate.delete(key);
        return Result.success("删除成功");
    }

    // ========== String 带过期时间 ==========

    @PostMapping("/string/expire")
    public Result<?> setStringWithExpire(@RequestParam String key,
                                         @RequestParam String value,
                                         @RequestParam long seconds) {
        redisTemplate.opsForValue().set(key, value, seconds, TimeUnit.SECONDS);
        return Result.success("设置成功，" + seconds + "秒后过期");
    }

    // ========== Hash 操作 ==========

    @PostMapping("/hash")
    public Result<?> setHash(@RequestParam String key,
                              @RequestParam String field,
                              @RequestParam String value) {
        redisTemplate.opsForHash().put(key, field, value);
        return Result.success("设置成功");
    }

    @GetMapping("/hash")
    public Result<?> getHash(@RequestParam String key, @RequestParam String field) {
        Object value = redisTemplate.opsForHash().get(key, field);
        return Result.success(value);
    }

    @GetMapping("/hash/all")
    public Result<?> getHashAll(@RequestParam String key) {
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);
        return Result.success(entries);
    }

    @DeleteMapping("/hash")
    public Result<?> deleteHash(@RequestParam String key, @RequestParam String field) {
        redisTemplate.opsForHash().delete(key, field);
        return Result.success("删除成功");
    }

    // ========== List 操作 ==========

    @PostMapping("/list/lpush")
    public Result<?> listLeftPush(@RequestParam String key, @RequestParam String value) {
        redisTemplate.opsForList().leftPush(key, value);
        return Result.success("左侧插入成功");
    }

    @PostMapping("/list/rpush")
    public Result<?> listRightPush(@RequestParam String key, @RequestParam String value) {
        redisTemplate.opsForList().rightPush(key, value);
        return Result.success("右侧插入成功");
    }

    @GetMapping("/list")
    public Result<?> listRange(@RequestParam String key,
                                @RequestParam(defaultValue = "0") long start,
                                @RequestParam(defaultValue = "-1") long end) {
        return Result.success(redisTemplate.opsForList().range(key, start, end));
    }

    // ========== Set 操作 ==========

    @PostMapping("/set")
    public Result<?> setAdd(@RequestParam String key, @RequestParam String value) {
        redisTemplate.opsForSet().add(key, value);
        return Result.success("添加成功");
    }

    @GetMapping("/set")
    public Result<?> setMembers(@RequestParam String key) {
        return Result.success(redisTemplate.opsForSet().members(key));
    }

    // ========== Sorted Set 操作 ==========

    @PostMapping("/zset")
    public Result<?> zsetAdd(@RequestParam String key,
                              @RequestParam String value,
                              @RequestParam double score) {
        redisTemplate.opsForZSet().add(key, value, score);
        return Result.success("添加成功");
    }

    @GetMapping("/zset")
    public Result<?> zsetRange(@RequestParam String key,
                                @RequestParam(defaultValue = "0") long start,
                                @RequestParam(defaultValue = "-1") long end) {
        return Result.success(redisTemplate.opsForZSet().reverseRangeWithScores(key, start, end));
    }

    // ========== 通用操作 ==========

    @GetMapping("/exists")
    public Result<?> exists(@RequestParam String key) {
        Boolean hasKey = redisTemplate.hasKey(key);
        return Result.success(hasKey);
    }

    @GetMapping("/ttl")
    public Result<?> ttl(@RequestParam String key) {
        Long expire = redisTemplate.getExpire(key, TimeUnit.SECONDS);
        return Result.success(expire + "秒");
    }
}
