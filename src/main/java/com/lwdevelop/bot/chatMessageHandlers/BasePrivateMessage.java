package com.lwdevelop.bot.chatMessageHandlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Message;

import com.lwdevelop.bot.bots.utils.Common;
import com.lwdevelop.service.impl.SpringyBotServiceImpl;
import com.lwdevelop.utils.RedisUtils;
import com.lwdevelop.utils.SpringUtils;

public abstract class BasePrivateMessage {

    @Autowired
    protected SpringyBotServiceImpl springyBotServiceImpl = SpringUtils.getApplicationContext()
            .getBean(SpringyBotServiceImpl.class);

    @Autowired
    protected RedisUtils redisUtils = SpringUtils.getApplicationContext()
            .getBean(RedisUtils.class);

    protected Common common;
    protected Message message;
    protected String text;
    protected Long chatId;
    protected Long springyBotId;

    public BasePrivateMessage(Common common){
        this.common = common;
        this.text = common.getUpdate().getMessage().getText();
        this.message = common.getUpdate().getMessage();
        this.chatId = common.getUpdate().getMessage().getChatId();
        this.springyBotId = this.common.getSpringyBotId();
    }

    public abstract void handler();

}
