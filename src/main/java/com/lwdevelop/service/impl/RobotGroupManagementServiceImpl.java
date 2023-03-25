package com.lwdevelop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lwdevelop.entity.RobotGroupManagement;
import com.lwdevelop.repository.RobotGroupManagementRepository;
import com.lwdevelop.service.RobotGroupManagementService;

@Service
public class RobotGroupManagementServiceImpl implements RobotGroupManagementService{

    @Autowired
    private RobotGroupManagementRepository robotGroupManagementRepository;
    
    @Override
    public void deleteById(Long Id) {
        robotGroupManagementRepository.deleteById(Id);
    }

    @Override
    public RobotGroupManagement findByBotIdAndGroupId(Long botId, Long groupId) {
        return robotGroupManagementRepository.findByBotIdAndGroupId(botId, groupId);
    }
    
}
