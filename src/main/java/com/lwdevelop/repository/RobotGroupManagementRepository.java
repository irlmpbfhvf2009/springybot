package com.lwdevelop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lwdevelop.entity.RobotGroupManagement;

public interface RobotGroupManagementRepository extends JpaRepository<RobotGroupManagement, Long> {
    void deleteById(Long id);
    RobotGroupManagement findByBotIdAndGroupId(Long botId,Long groupId);
    
}
