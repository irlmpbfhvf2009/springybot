package com.glp.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.glp.entity.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import com.glp.dto.ConfigDTO;
import com.glp.dto.SpringyBotDTO;
import com.glp.utils.ResponseUtils;

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

        List<TgUser> findTgUserBySpringyBotId(Long id);

        TgUser findTgUserBySpringyBotIdAndUserId(Long id,String userId);

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

        ResponseEntity<ResponseUtils.ResponseData> getRunTime();

        ResponseEntity<ResponseUtils.ResponseData> updateConfig(ConfigDTO configDTO);

        String getBot(@RequestBody String token);
}
