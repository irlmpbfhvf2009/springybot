package com.lwdevelop.service;

public interface RobotGroupManagementService {
    // DB CRUD For RobotGroupManagement
    void deleteByGroupIdAndBotId(Long groupId,Long botId);
}
