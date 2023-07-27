package org.springybot.repository;

import java.util.List;
import org.springybot.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

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
    @Query("SELECT rc FROM SpringyBot sb JOIN sb.recordChannelUsers rc WHERE sb.id = :id")
    List<RecordChannelUsers> findRecordChannelUsersBySpringyBotId(Long id);
    
    @Query("SELECT it FROM SpringyBot sb JOIN sb.invitationThreshold it WHERE sb.id = :id")
    List<InvitationThreshold> findInvitationThresholdBySpringyBotId(Long id);

    @Query("SELECT rgm FROM SpringyBot sb JOIN sb.robotGroupManagement rgm WHERE sb.id = :id")
    List<RobotGroupManagement> findRobotGroupManagementBySpringyBotId(Long id);

    @Query("SELECT rcm FROM SpringyBot sb JOIN sb.robotChannelManagement rcm WHERE sb.id = :id")
    List<RobotChannelManagement> findRobotChannelManagementBySpringyBotId(Long id);
    
    @Query("SELECT wl FROM SpringyBot sb JOIN sb.whiteList wl WHERE sb.id = :id")
    List<WhiteList> findWhiteListBySpringyBotId(Long id);

    @Query("SELECT ju FROM SpringyBot sb JOIN sb.tgUser ju WHERE sb.id = :id")
    List<TgUser> findTgUserBySpringyBotId(Long id);

    @Query("SELECT ju FROM SpringyBot sb JOIN sb.tgUser ju WHERE sb.id = :id and ju.userId = :userId")
    TgUser findTgUserBySpringyBotIdAndUserId(Long id,String userId);

    @Query("SELECT ib FROM SpringyBot sb JOIN sb.invitationBonusUser ib WHERE sb.id = :id")
    List<InvitationBonusUser> findInvitationBonusUserBySpringyBotId(Long id);

}
