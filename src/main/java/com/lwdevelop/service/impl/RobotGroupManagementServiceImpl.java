package com.lwdevelop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lwdevelop.repository.RobotGroupManagementRepository;
import com.lwdevelop.service.RobotGroupManagementService;

@Service
public class RobotGroupManagementServiceImpl implements RobotGroupManagementService{

    @Autowired
    private RobotGroupManagementRepository robotGroupManagementRepository;
    
    @Override
    public void deleteByGroupIdAndBotId(Long groupId,Long botId) {
        robotGroupManagementRepository.deleteByGroupIdAndBotId(groupId,botId);
    }
    
}
