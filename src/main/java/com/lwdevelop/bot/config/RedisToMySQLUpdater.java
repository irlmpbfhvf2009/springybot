package com.lwdevelop.bot.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.lwdevelop.entity.RobotChannelManagement;
import com.lwdevelop.entity.RobotGroupManagement;
import com.lwdevelop.entity.SpringyBot;
import com.lwdevelop.service.impl.SpringyBotServiceImpl;
import com.lwdevelop.utils.RedisUtils;

@Component
public class RedisToMySQLUpdater {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private SpringyBotServiceImpl springyBotService;

    @Scheduled(fixedRate = 300000) // 每5分鐘執行一次
    public void updateRedisDataToMySQL() {
        List<SpringyBot> springyBots = springyBotService.findAll();
        springyBots.stream().forEach(springyBot -> {
            // 獲取 Redis 中的數據
            List<RobotGroupManagement> robotGroupManagements = redisUtils.get(
                    "RobotGroupManagement_" + springyBot.getId(),
                    new TypeReference<List<RobotGroupManagement>>() {
                    });
            List<RobotChannelManagement> robotChannelManagements = redisUtils.get(
                    "RobotChannelManagement" + springyBot.getId(),
                    new TypeReference<List<RobotChannelManagement>>() {
                    });
            // 將數據更新到 MySQL
            if (robotGroupManagements != null) {
                List<RobotGroupManagement> robotGroupManagements_ = springyBotService.findRobotGroupManagementBySpringyBotId(springyBot.getId());
                robotGroupManagements_.clear();
                springyBot.setRobotGroupManagement(robotGroupManagements);
                springyBotService.save(springyBot);
            }
            if (robotChannelManagements != null) {
                List<RobotChannelManagement> robotChannelManagements_ = springyBotService.findRobotChannelManagementBySpringyBotId(springyBot.getId());
                robotChannelManagements_.clear();
                springyBot.setRobotChannelManagement(robotChannelManagements);
                springyBotService.save(springyBot);
            }
        });
    }
}
