package com.lwdevelop.bot.bots.triSpeak.messageHandling;

import com.lwdevelop.bot.bots.utils.Common;
import com.lwdevelop.bot.chatMessageHandlers.BasePrivateMessage;
import com.lwdevelop.entity.Config;
import com.lwdevelop.entity.SpringyBot;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PrivateMessage_ extends BasePrivateMessage {

    public PrivateMessage_(Common common) {
        super(common);
    }

    @Override
    public void handler() {
        switch (this.text) {
            case "/reset_config":
                SpringyBot springyBot = springyBotServiceImpl.findById(springyBotId).get();
                Config config = springyBot.getConfig();
                redisUtils.set("Config_" + springyBotId, config);
                log.info("Config reset for SpringyBot with ID: {}", this.common.getSpringyBotId());
                break;

            default:
                break;
        }

    }

}
