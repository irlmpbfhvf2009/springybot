package com.lwdevelop.bot.triSpeak.messageHandling;

import org.springframework.beans.factory.annotation.Autowired;

import com.lwdevelop.bot.utils.Common;
import com.lwdevelop.entity.Config;
import com.lwdevelop.entity.SpringyBot;
import com.lwdevelop.service.impl.SpringyBotServiceImpl;
import com.lwdevelop.utils.RedisUtils;
import com.lwdevelop.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PrivateMessage_ {

    @Autowired
    private SpringyBotServiceImpl springyBotServiceImpl = SpringUtils.getApplicationContext()
            .getBean(SpringyBotServiceImpl.class);

    @Autowired
    private RedisUtils redisUtils = SpringUtils.getApplicationContext()
            .getBean(RedisUtils.class);

    private Common common;
    private String text;

    public PrivateMessage_(Common common) {
        this.common = common;
        this.text = common.getUpdate().getMessage().getText();
    }

    public void handler() {

        switch (this.text) {
            case "/reset_config":
                SpringyBot springyBot = springyBotServiceImpl.findById(this.common.getSpringyBotId()).get();
                Config config = springyBot.getConfig();
                redisUtils.set("Config_" + this.common.getSpringyBotId(), config);
                log.info("Config reset for SpringyBot with ID: {}", this.common.getSpringyBotId());
                break;

            default:
                break;
        }

    }

}
