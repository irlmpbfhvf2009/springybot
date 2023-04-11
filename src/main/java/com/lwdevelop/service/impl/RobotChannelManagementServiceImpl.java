package com.lwdevelop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lwdevelop.entity.RobotChannelManagement;
import com.lwdevelop.repository.RobotChannelManagementRepository;
import com.lwdevelop.service.RobotChannelManagementService;

@Service
public class RobotChannelManagementServiceImpl implements RobotChannelManagementService{

    @Autowired
    private RobotChannelManagementRepository robotChannelManagementRepository;
    
    @Override
    public void deleteById(Long Id) {
        robotChannelManagementRepository.deleteById(Id);
    }

    @Override
    public RobotChannelManagement findByBotIdAndGroupId(Long botId, Long groupId) {
        return robotChannelManagementRepository.findByBotIdAndGroupId(botId, groupId);
    }
    
}
