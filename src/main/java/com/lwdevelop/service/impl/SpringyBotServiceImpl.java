package com.lwdevelop.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.BotSession;
import com.lwdevelop.bot.coolbao.coolbao_bot;
import com.lwdevelop.bot.coolbao.utils.SpringyBotEnum;
import com.lwdevelop.bot.talent.talent_bot;
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

    @Autowired
    private SpringyBotRepository springyBotRepository;

    private static Map<Long, BotSession> springyBotMap = new HashMap<>();

    private static Map<String,Date> run_time = new HashMap<>();

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
            springyBot.setState(true);
            save(springyBot);

            BotSession botSession = null;
            ;
            String botType = springyBot.getBotType();

            switch (botType) {
                case "talent":
                    botSession = telegramBotsApi.registerBot(new talent_bot(springyBotDTO));
                    break;
                case "coolbao":
                    botSession = telegramBotsApi.registerBot(new coolbao_bot(springyBotDTO));
                    break;
                case "triSpeak":
                    botSession = telegramBotsApi.registerBot(new triSpeak_bot(springyBotDTO));
                    break;
                default:
                    break;
            }
            if (botSession != null) {
                springyBotMap.put(id, botSession);
            }

            log.info("Common Telegram bot started.");
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

            log.info("Common Telegram bot stoped.");


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
        Config config= new Config();

        switch (botType) {
            case "coolbao":
                config.setPassword(SpringyBotEnum.PASSWORD.getText());
                springyBot.setConfig(config);
                break;
            case "triSpeak":
            config.setFollowChannelSet(false);
            default:
                break;
        }
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
            ;
        }
        ;
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
