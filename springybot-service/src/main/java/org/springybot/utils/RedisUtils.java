package org.springybot.utils;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

// @Slf4j
@Component
public class RedisUtils {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 檢查鍵是否存在
     */
    public boolean exists(final String key) {
        if (StringUtils.isBlank(key)) {
            return false;
        }
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 根據key讀取數據
     */
    public Object get(final String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        try {
            return redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根據key讀取數據
     */
    public <T> T get(final String key, TypeReference<T> typeReference) {
        if (key == null || StringUtils.isBlank(key)) {
            return null;
        }
        try {
            Object value = redisTemplate.opsForValue().get(key);
            if (value != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.convertValue(value, typeReference);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 寫入數據
     */
    public boolean set(final String key, Object value) {
        if (StringUtils.isBlank(key)) {
            return false;
        }
        try {
            redisTemplate.opsForValue().set(key, value);
            // log.info("Successfully wrote data to Redis.，key：{}", key);
            return true;
        } catch (Exception e) {
            // log.error("Failed to write data to Redis.，key：{}", key);
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 刪除數據
     */
    public void delete(final String key) {
        Boolean isDeleted = redisTemplate.delete(key);
        if (isDeleted) {
            // log.info("RecordChannelUsers key deleted successfully");
        } else {
            // log.info("RecordChannelUsers key not found");
        }
    }
    /**
     * 清空數據
     */
    public void clearRedisData(final String key) {
        redisTemplate.opsForHash().delete(key); // 清空指定键的数据
    }

    public void clearAllData() {
        try {
            Set<String> keys = redisTemplate.keys("*");
            if (keys != null && !keys.isEmpty()) {
                redisTemplate.delete(keys);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}