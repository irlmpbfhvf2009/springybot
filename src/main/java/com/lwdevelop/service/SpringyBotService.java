package com.lwdevelop.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;

import com.lwdevelop.dto.ConfigDTO;
import com.lwdevelop.dto.SpringyBotDTO;
import com.lwdevelop.entity.Config;
import com.lwdevelop.entity.InvitationBonusUser;
import com.lwdevelop.entity.InvitationThreshold;
import com.lwdevelop.entity.JobUser;
import com.lwdevelop.entity.RecordChannelUsers;
import com.lwdevelop.entity.RecordGroupUsers;
import com.lwdevelop.entity.RobotChannelManagement;
import com.lwdevelop.entity.RobotGroupManagement;
import com.lwdevelop.entity.SpringyBot;
import com.lwdevelop.entity.WhiteList;
import com.lwdevelop.utils.ResponseUtils;

public interface SpringyBotService {
        // DB CRUD For SpringyBot
        SpringyBot findByUsername(String username);

        SpringyBot findByToken(String token);

        Optional<SpringyBot> findById(Long id);

        void save(SpringyBot springyBot);

        List<SpringyBot> findAllByPage(int page, int pageSize);

        List<SpringyBot> findAll();

        void deleteById(Long id);

        List<RecordChannelUsers> findRecordChannelUsersBySpringyBotId(Long id);

        List<RecordGroupUsers> findRecordGroupUsersBySpringyBotId(Long id);

        List<InvitationThreshold> findInvitationThresholdBySpringyBotId(Long id);

        List<RobotGroupManagement> findRobotGroupManagementBySpringyBotId(Long id);

        List<RobotChannelManagement> findRobotChannelManagementBySpringyBotId(Long id);

        List<WhiteList> findWhiteListBySpringyBotId(Long id);

        List<JobUser> findJobUserBySpringyBotId(Long id);

        List<InvitationBonusUser> findInvitationBonusUserBySpringyBotId(Long id);

        // DB CRUD For Config
        Optional<Config> findByConfigId(Long id);

        void saveConfig(Config config);

        ResponseEntity<ResponseUtils.ResponseData> fetchManagedChat(ConfigDTO configDTO);

        // custom
        ResponseEntity<ResponseUtils.ResponseData> addBot(SpringyBotDTO springyBotDTO);

        ResponseEntity<ResponseUtils.ResponseData> getAllBot(int page, int pageSize);

        ResponseEntity<ResponseUtils.ResponseData> updateBot(SpringyBotDTO springyBotDTO);

        ResponseEntity<ResponseUtils.ResponseData> deleteBot(Map<String, String> requestData);

        ResponseEntity<ResponseUtils.ResponseData> start(SpringyBotDTO springyBotDTO);

        ResponseEntity<ResponseUtils.ResponseData> stop(SpringyBotDTO springyBotDTO);

        ResponseEntity<ResponseUtils.ResponseData> getRunTime();

        ResponseEntity<ResponseUtils.ResponseData> updateConfig(ConfigDTO configDTO);

}
