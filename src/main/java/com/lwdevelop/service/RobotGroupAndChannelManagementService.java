package com.lwdevelop.service;

import com.lwdevelop.entity.RobotChannelManagement;
import com.lwdevelop.entity.RobotGroupManagement;

public interface RobotGroupAndChannelManagementService {

    // DB CRUD For RobotChannelManagement
    void deleteByIdWithRobotChannelManagement(Long Id);

    RobotChannelManagement findByBotIdAndChannelId(Long botId, Long channelId);

    // DB CRUD For RobotGroupManagement
    void deleteByIdWithRobotGroupManagement(Long Id);

    RobotGroupManagement findByBotIdAndGroupId(Long botId, Long groupId);
}
