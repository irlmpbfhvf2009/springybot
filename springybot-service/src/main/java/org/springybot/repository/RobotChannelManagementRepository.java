package org.springybot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springybot.entity.RobotChannelManagement;

@Repository
public interface RobotChannelManagementRepository extends JpaRepository<RobotChannelManagement, Long> {

    RobotChannelManagement findByBotIdAndChannelId(Long botId,Long channelId);

    List<RobotChannelManagement> findByBotId(Long botId);

}
