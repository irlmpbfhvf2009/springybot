package com.lwdevelop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lwdevelop.entity.RobotGroupManagement;

@Repository
public interface RobotGroupManagementRepository extends JpaRepository<RobotGroupManagement, Long> {
    void deleteById(Long id);
    RobotGroupManagement findByBotIdAndGroupId(Long botId,Long groupId);
    
}
