package org.springybot.service;

import org.springframework.http.ResponseEntity;
import org.springybot.entity.RobotChannelManagement;
import org.springybot.entity.RobotGroupManagement;
import org.springybot.utils.ResponseUtils;

public interface RobotGroupAndChannelManagementService {

    // DB CRUD For RobotChannelManagement
    void deleteByIdWithRobotChannelManagement(Long Id);

    RobotChannelManagement findByBotIdAndChannelId(Long botId, Long channelId);

    // DB CRUD For RobotGroupManagement
    void deleteByIdWithRobotGroupManagement(Long Id);

    RobotGroupManagement findByBotIdAndGroupId(Long botId, Long groupId);



    ResponseEntity<ResponseUtils.ResponseData> getGroupAndChannelTreeData();


}
