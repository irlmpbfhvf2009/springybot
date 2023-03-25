package com.lwdevelop.service;

import com.lwdevelop.entity.RobotGroupManagement;

public interface RobotGroupManagementService {
    // DB CRUD For RobotGroupManagement
    void deleteById(Long Id);
    RobotGroupManagement findByBotIdAndGroupId(Long botId,Long groupId);
}
