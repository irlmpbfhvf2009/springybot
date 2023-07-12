package com.lwdevelop.service;

import org.springframework.http.ResponseEntity;
import com.lwdevelop.bot.bots.utils.Common;
import com.lwdevelop.entity.RobotChannelManagement;
import com.lwdevelop.entity.RobotGroupManagement;
import com.lwdevelop.utils.ResponseUtils;

public interface RobotGroupAndChannelManagementService {

    // DB CRUD For RobotChannelManagement
    void deleteByIdWithRobotChannelManagement(Long Id);

    RobotChannelManagement findByBotIdAndChannelId(Long botId, Long channelId);

    // DB CRUD For RobotGroupManagement
    void deleteByIdWithRobotGroupManagement(Long Id);

    RobotGroupManagement findByBotIdAndGroupId(Long botId, Long groupId);


    
    ResponseEntity<ResponseUtils.ResponseData> getGroupAndChannelTreeData();

    boolean ifSubscribeChannel(Common common);



}
