package com.glp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.glp.entity.RobotGroupManagement;

@Repository
public interface RobotGroupManagementRepository extends JpaRepository<RobotGroupManagement, Long> {
    RobotGroupManagement findByBotIdAndGroupId(Long botId,Long groupId);

    List<RobotGroupManagement> findByBotId(Long botId);
    
}
