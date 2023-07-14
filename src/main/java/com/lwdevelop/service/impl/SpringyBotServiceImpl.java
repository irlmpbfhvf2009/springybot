package com.lwdevelop.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import com.lwdevelop.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import com.lwdevelop.bot.factory.BotFactory;
import com.lwdevelop.bot.model.CustomLongPollingBot;
import com.lwdevelop.bot.model.CustomWebhookBot;
import com.lwdevelop.dto.ConfigDTO;
import com.lwdevelop.dto.SpringyBotDTO;
import com.lwdevelop.repository.ConfigRepository;
import com.lwdevelop.repository.SpringyBotRepository;
import com.lwdevelop.service.SpringyBotService;
import com.lwdevelop.utils.CommUtils;
import com.lwdevelop.utils.RedisUtils;
import com.lwdevelop.utils.ResponseUtils;
import com.lwdevelop.utils.RetEnum;
import com.lwdevelop.utils.ResponseUtils.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;

@Slf4j
@Service
public class SpringyBotServiceImpl implements SpringyBotService {

    @Autowired
    private TelegramBotsApi telegramBotsApi;

    @Autowired
    private BotFactory botFactory;

    @Autowired
    private SpringyBotRepository springyBotRepository;

    @Autowired
    private ConfigRepository configRepository;

    @Autowired
    private RedisUtils redisUtils;

    // SpringyBot CRUD
    @Override
    public Optional<SpringyBot> findById(Long id) {
        return springyBotRepository.findById(id);
    }

    @Override
    public List<SpringyBot> findAllByPage(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return springyBotRepository.findAll(pageable).getContent();
    }

    @Override
    public List<SpringyBot> findAll() {
        return springyBotRepository.findAll();
    }

    @Override
    public SpringyBot findByUsername(String username) {
        return springyBotRepository.findByUsername(username);
    }

    @Override
    public SpringyBot findByToken(String token) {
        return springyBotRepository.findByToken(token);
    }

    @Override
    public void deleteById(Long id) {
        springyBotRepository.deleteById(id);
    }

    @Override
    public void save(SpringyBot springyBot) {
        springyBotRepository.save(springyBot);
    }

    @Override
    public List<RecordChannelUsers> findRecordChannelUsersBySpringyBotId(Long id) {
        return springyBotRepository.findRecordChannelUsersBySpringyBotId(id);
    }

    @Override
    public List<RecordGroupUsers> findRecordGroupUsersBySpringyBotId(Long id) {
        return springyBotRepository.findRecordGroupUsersBySpringyBotId(id);
    }

    @Override
    public List<InvitationThreshold> findInvitationThresholdBySpringyBotId(Long id) {
        return springyBotRepository.findInvitationThresholdBySpringyBotId(id);
    }

    @Override
    public List<RobotGroupManagement> findRobotGroupManagementBySpringyBotId(Long id) {
        return springyBotRepository.findRobotGroupManagementBySpringyBotId(id);
    }

    @Override
    public List<RobotChannelManagement> findRobotChannelManagementBySpringyBotId(Long id) {
        return springyBotRepository.findRobotChannelManagementBySpringyBotId(id);
    }

    @Override
    public List<WhiteList> findWhiteListBySpringyBotId(Long id) {
        return springyBotRepository.findWhiteListBySpringyBotId(id);
    }

    @Override
    public List<JobUser> findJobUserBySpringyBotId(Long id) {
        return springyBotRepository.findJobUserBySpringyBotId(id);
    }

    @Override
    public List<DemandUser> findDemandUserBySpringyBotId(Long id) {
        return springyBotRepository.findDemandUserBySpringyBotId(id);
    }

    @Override
    public List<InvitationBonusUser> findInvitationBonusUserBySpringyBotId(Long id) {
        return springyBotRepository.findInvitationBonusUserBySpringyBotId(id);
    }

    @Override
    public Optional<Config> findByConfigId(Long id) {
        return configRepository.findById(id);
    }

    @Override
    public void saveConfig(Config config) {
        configRepository.save(config);
    }

    @Override
    public ResponseEntity<ResponseData> start(SpringyBotDTO dto) {
        try {
            Long id = dto.getId();
            String botType = dto.getBotType();
            String botModel = dto.getBotModel();
            CustomLongPollingBot longPollingBot = null;
            CustomWebhookBot webhookBot = null;
            SpringyBot springyBot = findById(id).get();

            switch (botModel) {
                case "LongPolling":
                    switch (botType) {
                        case "talent":
                            longPollingBot = botFactory.createTalentLongPollingBot(dto);
                            break;
                        case "coolbao":
                            longPollingBot = botFactory.createCoolbaoLongPollingBot(dto);
                            break;
                        case "triSpeak":
                            longPollingBot = botFactory.createTriSpeakLongPollingBot(dto);
                            break;
                        case "demand":
                            longPollingBot = botFactory.createDemandLongPollingBot(dto);
                            break;
                        default:
                            break;
                    }
                    break;
                case "Webhook":
                    switch (botType) {
                        case "coolbao":
                            webhookBot = botFactory.createCoolbaoWebhookBot(dto);
                            break;
                    }
                    break;
            }

            if (webhookBot != null) {
                SetWebhook setWebhook = SetWebhook.builder()
                        .url("https://eb8c-61-218-87-189.ngrok-free.app/api")
                        .build();
                telegramBotsApi.registerBot(webhookBot, setWebhook);
            }

            if (longPollingBot != null) {
                Long botId = longPollingBot.getMe().getId();
                springyBot.setBotId(botId);
                springyBot.setState(true);
                save(springyBot);

                telegramBotsApi.registerBot(longPollingBot);

                // Redis
                Config config = findById(id).get().getConfig();
                List<RecordGroupUsers> recordGroupUsers = findRecordGroupUsersBySpringyBotId(id);
                List<RecordChannelUsers> recordChannelUsers = findRecordChannelUsersBySpringyBotId(id);
                List<InvitationThreshold> invitationThreshold = findInvitationThresholdBySpringyBotId(id);
                redisUtils.set("Config_" + id, config);
                redisUtils.set("RecordGroupUsers_" + id, recordGroupUsers);
                redisUtils.set("RecordChannelUsers_" + id, recordChannelUsers);
                redisUtils.set("InvitationThreshold_" + id, invitationThreshold);

                log.info("{} Telegram bot started.", dto.getUsername());
            }

            return ResponseUtils.response(RetEnum.RET_SUCCESS, "启动成功");

        } catch (TelegramApiException e) {
            log.error("Catch TelegramApiException : {}", e.toString());
            if (e.getMessage().equals("Bot token and username can't be empty")) {
                return ResponseUtils.response(RetEnum.RET_TOKEN_EMPTY);
            }
            return ResponseUtils.response(RetEnum.RET_START_FAIL);
        } catch (NoSuchElementException e) {
            log.error("Catch NoSuchElementException : {}", e.toString());
            return ResponseUtils.response(RetEnum.RET_START_NOT_EXIST);
        }

    }

    @Override
    public ResponseEntity<ResponseData> stop(SpringyBotDTO springyBotDTO) {
        try {
            Long id = springyBotDTO.getId();

            SpringyBot springyBot = findById(id).get();
            springyBot.setState(false);
            save(springyBot);

            log.info("{} Telegram bot stoped.", springyBotDTO.getUsername());

            return ResponseUtils.response(RetEnum.RET_SUCCESS, "已停止");
        } catch (Exception e) {
            log.error("Catch exception : {}", e.toString());
            return ResponseUtils.response(RetEnum.RET_STOP_FAIL);
        }
    }

    @Override
    public ResponseEntity<ResponseData> addBot(SpringyBotDTO springyBotDTO) {
        SpringyBot springyBot = new SpringyBot();
        springyBot.setToken(springyBotDTO.getToken());
        springyBot.setUsername(springyBotDTO.getUsername());
        springyBot.setBotModel(springyBotDTO.getBotModel());
        springyBot.setBotType(springyBotDTO.getBotType());
        springyBot.setState(springyBotDTO.getState());

        Config config = new Config();
        config.setContactPerson("");
        config.setDeleteSeconds(0);
        config.setFollowChannelSet(false);
        config.setFollowChannelSet_chatId(0L);
        config.setFollowChannelSet_chatTitle("");
        config.setInvitationBonusSet(false);
        config.setInviteEarnedOutstand(BigDecimal.valueOf(0));
        config.setMinimumPayout(BigDecimal.valueOf(0));
        config.setInviteFriendsAutoClearTime(0);
        config.setInviteFriendsQuantity(0);
        config.setInviteFriendsSet(false);
        config.setInviteMembers(0);
        config.setPassword("duv!3qz@XY");
        springyBot.setConfig(config);
        save(springyBot);

        log.info("SpringyBotServiceImpl ==> addBot ... [ {} ] 新增成功", springyBotDTO.getId());
        return ResponseUtils.response(RetEnum.RET_SUCCESS, "新增成功");
    }

    @Override
    public ResponseEntity<ResponseData> getAllBot(int page, int pageSize) {

        HashMap<Object, Object> data = new HashMap<>();
        List<SpringyBot> springyBotAllList = findAllByPage(page, pageSize);

        Object pager = CommUtils.Pager(page, pageSize, springyBotAllList.size());
        data.put("list", springyBotAllList);
        data.put("pager", pager);

        return ResponseUtils.response(RetEnum.RET_SUCCESS, data);
    }

    @Override
    public ResponseEntity<ResponseData> updateBot(SpringyBotDTO springyBotDTO) {
        Long id = springyBotDTO.getId();
        SpringyBot springyBot = findById(id).get();

        springyBot.setUsername(springyBotDTO.getUsername());
        springyBot.setToken(springyBotDTO.getToken());
        springyBot.setBotModel(springyBotDTO.getBotModel());
        springyBot.setBotType(springyBotDTO.getBotType());

        if (springyBot.getConfig() == null) {
            Config config = new Config();
            config.setContactPerson("");
            config.setDeleteSeconds(10);
            config.setFollowChannelSet(false);
            config.setFollowChannelSet_chatId(0L);
            config.setFollowChannelSet_chatTitle("");
            config.setInvitationBonusSet(false);
            config.setInviteEarnedOutstand(BigDecimal.valueOf(0));
            config.setMinimumPayout(BigDecimal.valueOf(0));
            config.setInviteFriendsAutoClearTime(0);
            config.setInviteFriendsQuantity(0);
            config.setInviteFriendsSet(false);
            config.setInviteMembers(0);
            config.setPassword("");
            springyBot.setConfig(config);
        }

        save(springyBot);
        log.info("SpringyBotServiceImpl ==> updateBot ... [ {} ] 修改成功", springyBotDTO.getId());
        return ResponseUtils.response(RetEnum.RET_SUCCESS, "修改成功");
    }

    @Override
    public ResponseEntity<ResponseData> updateConfig(ConfigDTO configDTO) {
        Long id = configDTO.getId();
        Config config = findByConfigId(id).get();
        config.setContactPerson(configDTO.getContactPerson());
        config.setDeleteSeconds(configDTO.getDeleteSeconds());
        config.setFollowChannelSet(configDTO.getFollowChannelSet());
        config.setFollowChannelSet_chatId(configDTO.getFollowChannelSet_chatId());
        config.setFollowChannelSet_chatTitle(configDTO.getFollowChannelSet_chatTitle());
        config.setInvitationBonusSet(configDTO.getInvitationBonusSet());
        config.setInviteEarnedOutstand(configDTO.getInviteEarnedOutstand());
        config.setMinimumPayout(configDTO.getMinimumPayout());
        config.setInviteFriendsAutoClearTime(configDTO.getInviteFriendsAutoClearTime());
        config.setInviteFriendsQuantity(configDTO.getInviteFriendsQuantity());
        config.setInviteFriendsSet(configDTO.getInviteFriendsSet());
        config.setInviteMembers(configDTO.getInviteMembers());
        config.setPassword(configDTO.getPassword());
        saveConfig(config);

        List<SpringyBot> springyBots = findAll();

        springyBots.stream().filter(s -> s.getConfig().getId().equals(id)).findAny().ifPresent(s -> {
            redisUtils.set("Config_" + s.getId(), config);
        });

        log.info("SpringyBotServiceImpl ==> updateConfig ... [ {} ] 修改成功", configDTO.getId());
        return ResponseUtils.response(RetEnum.RET_SUCCESS, "修改成功");
    }

    @Override
    public ResponseEntity<ResponseData> deleteBot(Map<String, String> requestData) {
        String[] ids = requestData.get(requestData.keySet().toArray()[0]).split(",");

        for (String id : ids) {
            Long parseId = Long.parseLong(id);
            deleteById(parseId);
            log.info("SpringyBotServiceImpl ==> deleteBot ... [ {} ] 刪除成功", id);
        }
        return ResponseUtils.response(RetEnum.RET_SUCCESS, "删除成功");
    }

    @Override
    public ResponseEntity<ResponseData> getRunTime() {
        return ResponseUtils.response(RetEnum.RET_SUCCESS, "");
    }

    @Override
    public ResponseEntity<ResponseData> fetchManagedChat(ConfigDTO configDTO) {
        Long configId = configDTO.getId();
        List<SpringyBot> springyBots = findAll();
        List<String> chatTitles = new ArrayList<>();
        HashMap<Object, Object> data = new HashMap<>();
        springyBots.stream().filter(s -> s.getConfig().getId().equals(configId)).findAny()
                .ifPresent(springybot -> {
                    List<RobotChannelManagement> robotChannelManagements = findRobotChannelManagementBySpringyBotId(
                            springybot.getId());
                    robotChannelManagements.stream().forEach(rcm -> {
                        chatTitles.add(rcm.getChannelTitle());
                    });
                });
        data.put("list", chatTitles);
        return ResponseUtils.response(RetEnum.RET_SUCCESS, data);
    }

}
