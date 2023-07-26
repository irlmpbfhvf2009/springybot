package com.glp.service;

import org.springframework.http.ResponseEntity;
import com.glp.entity.RobotChannelManagement;
import com.glp.entity.RobotGroupManagement;
import com.glp.utils.ResponseUtils;

public interface RobotGroupAndChannelManagementService {

    // DB CRUD For RobotChannelManagement
    void deleteByIdWithRobotChannelManagement(Long Id);

    RobotChannelManagement findByBotIdAndChannelId(Long botId, Long channelId);

    // DB CRUD For RobotGroupManagement
    void deleteByIdWithRobotGroupManagement(Long Id);

    RobotGroupManagement findByBotIdAndGroupId(Long botId, Long groupId);



    ResponseEntity<ResponseUtils.ResponseData> getGroupAndChannelTreeData();


}
