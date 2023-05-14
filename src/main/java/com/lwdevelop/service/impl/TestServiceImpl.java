package com.lwdevelop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.lwdevelop.entity.Config;
import com.lwdevelop.entity.SpringyBot;
import com.lwdevelop.repository.SpringyBotRepository;
import com.lwdevelop.service.TestService;
import com.lwdevelop.utils.ResponseUtils;
import com.lwdevelop.utils.RetEnum;
import com.lwdevelop.utils.ResponseUtils.ResponseData;

@Service
public class TestServiceImpl implements TestService{
    
    @Autowired
    private SpringyBotRepository springyBotRepository;

    @Override
    public ResponseEntity<ResponseData> addConfig(Long id) {
        SpringyBot springyBot = springyBotRepository.findById(id).get();
        if(springyBot!=null){
            Config config = new Config();
            config.setFollowChannelSet(true);
            if(springyBot.getConfig()==null){
                springyBot.setConfig(config);
            }
            springyBotRepository.save(springyBot);
        }
        return ResponseUtils.response(RetEnum.RET_SUCCESS, "新增参数成功");
    }
    
}
