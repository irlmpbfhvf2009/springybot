package com.lwdevelop.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lwdevelop.entity.InvitationBonusUser;
import com.lwdevelop.entity.InvitationThreshold;
import com.lwdevelop.entity.JobUser;
import com.lwdevelop.entity.RecordChannelUsers;
import com.lwdevelop.entity.RecordGroupUsers;
import com.lwdevelop.entity.RobotChannelManagement;
import com.lwdevelop.entity.RobotGroupManagement;
import com.lwdevelop.entity.SpringyBot;
import com.lwdevelop.entity.WhiteList;

@Repository
public interface SpringyBotRepository extends JpaRepository<SpringyBot, Long> {
    SpringyBot findById(Integer id);

    Page<SpringyBot> findAllByUsernameContaining(String username, Pageable pageable);

    SpringyBot findByUsername(String username);

    SpringyBot findByToken(String token);

    List<RecordGroupUsers> findRecordGroupUsersById(Long id);

    // 查询指定 SpringyBot ID 下的所有 RecordGroupUsers 实体
    @Query("SELECT rg FROM SpringyBot sb JOIN sb.recordGroupUsers rg WHERE sb.id = :id")
    List<RecordGroupUsers> findRecordGroupUsersBySpringyBotId(Long id);
    
    // 查询指定 SpringyBot ID 下的所有 RecordChannelUsers 实体
    @Query("SELECT rg FROM SpringyBot sb JOIN sb.recordChannelUsers rg WHERE sb.id = :id")
    List<RecordChannelUsers> findRecordChannelUsersBySpringyBotId(Long id);
    
    @Query("SELECT rg FROM SpringyBot sb JOIN sb.invitationThreshold rg WHERE sb.id = :id")
    List<InvitationThreshold> findInvitationThresholdBySpringyBotId(Long id);

    @Query("SELECT rg FROM SpringyBot sb JOIN sb.robotGroupManagement rg WHERE sb.id = :id")
    List<RobotGroupManagement> findRobotGroupManagementBySpringyBotId(Long id);

    @Query("SELECT rg FROM SpringyBot sb JOIN sb.robotChannelManagement rg WHERE sb.id = :id")
    List<RobotChannelManagement> findRobotChannelManagementBySpringyBotId(Long id);
    
    @Query("SELECT rg FROM SpringyBot sb JOIN sb.whiteList rg WHERE sb.id = :id")
    List<WhiteList> findWhiteListBySpringyBotId(Long id);

    @Query("SELECT rg FROM SpringyBot sb JOIN sb.jobUser rg WHERE sb.id = :id")
    List<JobUser> findJobUserBySpringyBotId(Long id);

    @Query("SELECT rg FROM SpringyBot sb JOIN sb.invitationBonusUser rg WHERE sb.id = :id")
    List<InvitationBonusUser> findInvitationBonusUserBySpringyBotId(Long id);



}
