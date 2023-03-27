package com.lwdevelop.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisUtils {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public String setRedisValue(String key, String text) {
        try {
            redisTemplate.opsForValue().set(key, text);
            return "success";
        } catch (Exception e) {
            return e.toString();
        }
    }

    public String getRedisValue(String key) {
        try {
            return (String) redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            return e.toString();
        }
    }

    public void deleteRedisValue(String key) {
        redisTemplate.delete(key);
    }

    // 修改指定 key 的数据
    // public void updateRedisValue(String key, Object newValue) {
    // redisTemplate.opsForValue().set(key, newValue);
    // }

}
