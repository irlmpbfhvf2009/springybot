package com.lwdevelop.service;

import com.lwdevelop.entity.RobotChannelManagement;

public interface RobotChannelManagementService {

    // DB CRUD For RobotChannelManagement
    void deleteById(Long Id);
    RobotChannelManagement findByBotIdAndChannelId(Long botId,Long channelId);
}
