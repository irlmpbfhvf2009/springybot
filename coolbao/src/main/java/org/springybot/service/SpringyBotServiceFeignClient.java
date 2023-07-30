package org.springybot.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springybot.entity.SpringyBot;

@FeignClient(name="springybot-service")
public interface SpringyBotServiceFeignClient {
    
    @PostMapping("/springybot/v1/cacheSpringyBotDataToRedis")
    SpringyBot cacheSpringyBotDataToRedis(String token);
    
}
