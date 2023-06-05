package com.lwdevelop.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.BotSession;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.telegram.telegrambots.updatesreceivers.DefaultWebhook;
import com.lwdevelop.bot.coolbao.coolbao_bot;
import com.lwdevelop.bot.coolbao.utils.SpringyBotEnum;
import com.lwdevelop.bot.talent.talent_bot;
import com.lwdevelop.bot.test.TelegrambotWebhook;
import com.lwdevelop.bot.triSpeak.triSpeak_bot;
import com.lwdevelop.dto.SpringyBotDTO;
import com.lwdevelop.entity.Config;
import com.lwdevelop.entity.SpringyBot;
import com.lwdevelop.repository.SpringyBotRepository;
import com.lwdevelop.service.SpringyBotService;
import com.lwdevelop.utils.CommUtils;
import com.lwdevelop.utils.ResponseUtils;
import com.lwdevelop.utils.RetEnum;
import com.lwdevelop.utils.ResponseUtils.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;

@Slf4j
@Service
public class SpringyBotServiceImpl implements SpringyBotService {

    @Resource
    private TelegramBotsApi telegramBotsApi;

    @Value("${telegram.webhook-host}")
    private String webhookHost;

    @Value("${telegram.internal.url}")
    private String internalUrl;

    @Autowired
    private SpringyBotRepository springyBotRepository;

    private static Map<Long, BotSession> springyBotMap = new HashMap<>();

    private static Map<String, Date> run_time = new HashMap<>();

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
    public ResponseEntity<ResponseData> start(SpringyBotDTO springyBotDTO) {
        try {
            Long id = springyBotDTO.getId();

            if (springyBotMap.containsKey(id)) {
                return ResponseUtils.response(RetEnum.RET_START_EXIST);
            }

            SpringyBot springyBot = findById(springyBotDTO.getId()).get();
            BotSession botSession = null;
            Long botId = null;
            String botType = springyBot.getBotType();
            List<String> allowedUpdates = Arrays.asList("update_id", "message", "edited_message",
                    "channel_post", "edited_channel_post", "inline_query", "chosen_inline_result",
                    "callback_query", "shipping_query", "pre_checkout_query", "poll", "poll_answer",
                    "my_chat_member", "chat_member");

            switch (botType) {
                case "talent":
                    talent_bot talent_bot = new talent_bot(springyBotDTO);
                    talent_bot.getOptions().setAllowedUpdates(allowedUpdates);
                    botId = talent_bot.getMe().getId();
                    botSession = telegramBotsApi.registerBot(talent_bot);
                    break;
                case "coolbao":
                    coolbao_bot coolbao_bot = new coolbao_bot(springyBotDTO);
                    coolbao_bot.getOptions().setAllowedUpdates(allowedUpdates);
                    botId = coolbao_bot.getMe().getId();
                    botSession = telegramBotsApi.registerBot(coolbao_bot);
                    break;
                case "triSpeak":
                    triSpeak_bot triSpeak_bot = new triSpeak_bot(springyBotDTO);
                    triSpeak_bot.getOptions().setAllowedUpdates(allowedUpdates);
                    botId = triSpeak_bot.getMe().getId();
                    botSession = telegramBotsApi.registerBot(triSpeak_bot);
                    break;
                case "telegrambot":
                    DefaultWebhook defaultWebhook = new DefaultWebhook();
                    defaultWebhook.setInternalUrl(internalUrl);
                    TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class, defaultWebhook);
                    SetWebhook setWebhook = SetWebhook.builder().url(webhookHost).build();
                    telegramBotsApi.registerBot(new TelegrambotWebhook(springyBotDTO), setWebhook);
                    break;
                default:
                    break;
            }

            if (botSession != null) {
                springyBotMap.put(id, botSession);
            }
            if(botId!=null){
                springyBot.setBotId(botId);
            }
            springyBot.setState(true);
            save(springyBot);


            log.info("{} Telegram bot started.", springyBotDTO.getUsername());
            run_time.put(springyBot.getUsername(), new Date());

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
            if (springyBotMap.containsKey(id)) {
                springyBotMap.get(id).stop();
                springyBotMap.remove(id);
            }

            SpringyBot springyBot = findById(springyBotDTO.getId()).get();
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
        springyBot.setBotType(springyBotDTO.getBotType());
        springyBot.setState(springyBotDTO.getState());

        String botType = springyBotDTO.getBotType();

        Config config = new Config();
        config.setContactPerson("");
        config.setDeleteSeconds(0);
        config.setFollowChannelSet(false);
        config.setFollowChannelSet_chatId(0L);
        config.setFollowChannelSet_chatTitle("");
        config.setInvitationBonusSet(false);
        config.setInviteEarnedOutstand(0);
        config.setInviteFriendsAutoClearTime(0);
        config.setInviteFriendsQuantity(0);
        config.setInviteFriendsSet(false);
        config.setInviteMembers(0);
        config.setPassword("");

        switch (botType) {
            case "coolbao":
                config.setPassword(SpringyBotEnum.PASSWORD.getText());
                break;
            default:
                break;
        }
        springyBot.setConfig(config);
        save(springyBot);
        log.info("SpringyBotServiceImpl ==> addBot ... [ {} ] 新增成功", springyBotDTO.getUsername());
        return ResponseUtils.response(RetEnum.RET_SUCCESS, "新增成功");
    }

    @Override
    public ResponseEntity<ResponseData> getAllBot(int page, int pageSize) {
        HashMap<Object, Object> data = new HashMap<>();
        List<SpringyBot> springyBotList = findAllByPage(page, pageSize);
        for (SpringyBot springyBot : springyBotList) {
            if (!springyBotMap.containsKey(springyBot.getId())) {
                springyBot.setState(false);
                save(springyBot);
            }
        }
        
        Object pager = CommUtils.Pager(page, pageSize, springyBotList.size());
        data.put("list", springyBotList);
        data.put("pager", pager);
        return ResponseUtils.response(RetEnum.RET_SUCCESS, data);
    }

    @Override
    public ResponseEntity<ResponseData> updateBot(SpringyBotDTO springyBotDTO) {
        Long id = springyBotDTO.getId();
        SpringyBot springyBot = findById(id).get();
        if (springyBotMap.containsKey(id)) {
            springyBotMap.get(id).stop();
            springyBotMap.remove(id);
            springyBot.setState(false);
        }
        springyBot.setUsername(springyBotDTO.getUsername());
        springyBot.setToken(springyBotDTO.getToken());
        springyBot.setBotType(springyBotDTO.getBotType());
        springyBot.getConfig().setContactPerson(springyBotDTO.getConfig().getContactPerson());
        springyBot.getConfig().setDeleteSeconds(springyBotDTO.getConfig().getDeleteSeconds());
        springyBot.getConfig().setFollowChannelSet(springyBotDTO.getConfig().getFollowChannelSet());
        springyBot.getConfig().setFollowChannelSet_chatId(springyBotDTO.getConfig().getFollowChannelSet_chatId());
        springyBot.getConfig().setFollowChannelSet_chatTitle(springyBotDTO.getConfig().getFollowChannelSet_chatTitle());
        springyBot.getConfig().setInvitationBonusSet(springyBotDTO.getConfig().getInvitationBonusSet());
        springyBot.getConfig().setInviteEarnedOutstand(springyBotDTO.getConfig().getInviteEarnedOutstand());
        springyBot.getConfig().setInviteFriendsAutoClearTime(springyBotDTO.getConfig().getInviteFriendsAutoClearTime());
        springyBot.getConfig().setInviteFriendsQuantity(springyBotDTO.getConfig().getInviteFriendsQuantity());
        springyBot.getConfig().setInviteFriendsSet(springyBotDTO.getConfig().getInviteFriendsSet());
        springyBot.getConfig().setInviteMembers(springyBotDTO.getConfig().getInviteMembers());
        springyBot.getConfig().setPassword(springyBotDTO.getConfig().getPassword());
        save(springyBot);
        log.info("SpringyBotServiceImpl ==> updateBot ... [ {} ] 修改成功", springyBotDTO.getUsername());
        return ResponseUtils.response(RetEnum.RET_SUCCESS, "修改成功");
    }

    @Override
    public ResponseEntity<ResponseData> deleteBot(Map<String, String> requestData) {
        String[] ids = requestData.get(requestData.keySet().toArray()[0]).split(",");

        for (String id : ids) {
            Long parseId = Long.parseLong(id);
            if (springyBotMap.containsKey(parseId)) {
                springyBotMap.get(parseId).stop();
                springyBotMap.remove(parseId);
            }

            // 刪除關聯資料表
            Optional<SpringyBot> optSpringyBot = Optional.of(findById(parseId).get());
            SpringyBot springyBot = optSpringyBot.get();
            springyBot.getRobotGroupManagement().remove((Object) springyBot.getId());
            springyBot.getRobotChannelManagement().remove((Object) springyBot.getId());
            springyBot.getJobUser().remove((Object) springyBot.getId());

            deleteById(parseId);
            log.info("SpringyBotServiceImpl ==> deleteBot ... [ {} ] 刪除成功", id);
        }
        return ResponseUtils.response(RetEnum.RET_SUCCESS, "删除成功");
    }

    @Override
    public ResponseEntity<ResponseData> getRunTime() {
        return ResponseUtils.response(RetEnum.RET_SUCCESS, "");
    }

}
