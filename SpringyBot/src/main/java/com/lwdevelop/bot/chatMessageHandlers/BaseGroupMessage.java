package com.lwdevelop.bot.chatMessageHandlers;

import org.springframework.beans.factory.annotation.Autowired;

import com.lwdevelop.bot.bots.utils.Common;
import com.lwdevelop.utils.RedisUtils;
import com.lwdevelop.utils.SpringUtils;

public abstract class BaseGroupMessage {

    @Autowired
    protected RedisUtils redisUtils = SpringUtils.getApplicationContext()
            .getBean(RedisUtils.class);

    protected Common common;
    protected Long chatId;
    protected String chatId_str;
    protected Long userId;
    protected String username;
    protected String firstname;
    protected String lastname;
    protected Integer messageId;
    protected Long springyBotId;

    public BaseGroupMessage(Common common) {
        this.common = common;
        this.springyBotId = common.getSpringyBotId();
        this.chatId = common.getUpdate().getMessage().getChatId();
        this.chatId_str = common.getUpdate().getMessage().getChatId().toString();
        this.userId = common.getUpdate().getMessage().getFrom().getId();
        this.username = common.getUpdate().getMessage().getFrom().getUserName() == null ? ""
                : common.getUpdate().getMessage().getFrom().getUserName();
        this.firstname = common.getUpdate().getMessage().getFrom().getFirstName() == null ? ""
                : common.getUpdate().getMessage().getFrom().getFirstName();
        this.lastname = common.getUpdate().getMessage().getFrom().getLastName() == null ? ""
                : common.getUpdate().getMessage().getFrom().getLastName();
        this.messageId = common.getUpdate().getMessage().getMessageId();

    }

    public abstract void handler();
}
