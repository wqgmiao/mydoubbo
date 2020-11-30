package com.example.redis.utils;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class RedisUtils {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 写入数据
     *
     * @param k 键
     * @param v 值
     */
    public void set(String k, String v) {
        stringRedisTemplate.opsForValue().set(k, v);
    }

    /**
     * 获取数据
     *
     * @param k 键
     * @return 值
     */
    public String get(String k) {
        return stringRedisTemplate.opsForValue().get(k);
    }
}