package com.lwdevelop.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.BotSession;
import com.lwdevelop.bot.Custom;
import com.lwdevelop.dto.SpringyBotDTO;
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

    @Autowired
    private SpringyBotRepository springyBotRepository;

    @Override
    public Optional<SpringyBot> findById(Long id) {
        return springyBotRepository.findById(id);
    }

    @Override
    public List<SpringyBot> findAll(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return springyBotRepository.findAll(pageable).getContent();
    }
    @Override
    public SpringyBot findByUsername(String username) {
        return springyBotRepository.findByUsername(username);
    }
    @Override
    public void deleteById(Long id) {
        springyBotRepository.deleteById(id);
    }
    @Override
    public void save(SpringyBot springyBot) {
        springyBotRepository.save(springyBot);
    }

    @Resource
    private TelegramBotsApi telegramBotsApi;

    // private BotSession botSession;

    private static Map<Long,BotSession> springyBotMap = new HashMap<>();

    @Override
    public synchronized ResponseEntity<ResponseData> start(SpringyBotDTO springyBotDTO) {
        try {
            Long id = springyBotDTO.getId();
            String token = springyBotDTO.getToken();
            String username = springyBotDTO.getUsername();
            SpringyBot springyBot = findById(id).get();
            BotSession botSession = telegramBotsApi.registerBot(new Custom(token, username, new DefaultBotOptions()));

            if(springyBotMap.containsKey(id)){
                return ResponseUtils.response(RetEnum.RET_START_EXIST);
            }
            springyBotMap.put(id, botSession);
            springyBot.setState(true);
            save(springyBot);
            log.info("Common Telegram bot started.");
            return ResponseUtils.response(RetEnum.RET_SUCCESS, "启动成功");

        } catch (TelegramApiException e) {
            log.error("Catch TelegramApiException", e);
            return ResponseUtils.response(RetEnum.RET_START_FAIL);
        }
    }

    @Override
    public synchronized ResponseEntity<ResponseData> stop(SpringyBotDTO springyBotDTO) {
        try {
            Long id = springyBotDTO.getId();
            SpringyBot springyBot = findById(springyBotDTO.getId()).get();
            
            if(springyBotMap.containsKey(id)){
                springyBotMap.get(id).stop();
                springyBotMap.remove(id);
            }
            springyBot.setState(false);
            save(springyBot);
            log.info("Common Telegram bot stoped.");
            return ResponseUtils.response(RetEnum.RET_SUCCESS, "已停止");
        } catch (Exception e) {
            log.error("Catch exception", e);
            return ResponseUtils.response(RetEnum.RET_STOP_FAIL);
        }
    }


    @Override
    public ResponseEntity<ResponseData> addBot(SpringyBotDTO springyBotDTO) {
        String token = springyBotDTO.getToken();
        String username = springyBotDTO.getUsername();
        SpringyBot springyBot = new SpringyBot();

        springyBot.setToken(token);
        springyBot.setUsername(username);
        springyBot.setState(false);
        save(springyBot);

        log.info("SpringyBotServiceImpl ==> addBot ... [ {} ] 新增成功", username);
        return ResponseUtils.response(RetEnum.RET_SUCCESS, "新增成功");
    }

    @Override
    public ResponseEntity<ResponseData> getAllBot(int page, int pageSize) {
        HashMap<String, Object> data = new HashMap<>();
        List<SpringyBot> springyBotList = findAll(page, pageSize);
        Object pager = CommUtils.Pager(page, pageSize, springyBotList.size());

        data.put("list", springyBotList);
        data.put("pager", pager);

        return ResponseUtils.response(RetEnum.RET_SUCCESS, data);
    }

    @Override
    public ResponseEntity<ResponseData> updateBot(SpringyBotDTO springyBotDTO) {
        SpringyBot springyBot = findById(springyBotDTO.getId()).get();

        springyBot.setUsername(springyBotDTO.getUsername());
        springyBot.setToken(springyBotDTO.getToken());
        save(springyBot);

        log.info("SpringyBotServiceImpl ==> updateBot ... [ {} ]", "done");
        return ResponseUtils.response(RetEnum.RET_SUCCESS, "编辑成功");
    }

    @Override
    public ResponseEntity<ResponseData> deleteBot(Map<String, String> requestData) {
        String[] ids = requestData.get(requestData.keySet().toArray()[0]).split(",");

        for (String id : ids) {
            deleteById(Long.parseLong(id));
        }
        
        return ResponseUtils.response(RetEnum.RET_SUCCESS, "删除成功");
    }


}
