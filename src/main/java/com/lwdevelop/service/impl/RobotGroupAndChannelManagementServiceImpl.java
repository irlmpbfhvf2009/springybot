package com.lwdevelop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lwdevelop.entity.RobotChannelManagement;
import com.lwdevelop.entity.RobotGroupManagement;
import com.lwdevelop.repository.RobotChannelManagementRepository;
import com.lwdevelop.repository.RobotGroupManagementRepository;
import com.lwdevelop.service.RobotGroupAndChannelManagementService;

@Service
public class RobotGroupAndChannelManagementServiceImpl implements RobotGroupAndChannelManagementService{

    @Autowired
    private RobotGroupManagementRepository robotGroupManagementRepository;

    @Autowired
    private RobotChannelManagementRepository robotChannelManagementRepository;
    
    @Override
    public void deleteByIdWithRobotChannelManagement(Long Id) {
        robotChannelManagementRepository.deleteById(Id);
    }

    @Override
    public RobotChannelManagement findByBotIdAndChannelId(Long botId, Long channelId) {
        return robotChannelManagementRepository.findByBotIdAndChannelId(botId, channelId);
    }

    @Override
    public void deleteByIdWithRobotGroupManagement(Long Id) {
        robotGroupManagementRepository.deleteById(Id);
    }

    @Override
    public RobotGroupManagement findByBotIdAndGroupId(Long botId, Long groupId) {
        return robotGroupManagementRepository.findByBotIdAndGroupId(botId, groupId);
    }

    
}
