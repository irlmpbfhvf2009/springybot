package com.lwdevelop.bot.chatMessageHandlers;

import com.lwdevelop.bot.bots.utils.Common;

public abstract class BaseChannelMessage {

    protected Common common;
    protected String text;
    protected String chatId_str;
    protected Integer messageId;

    public BaseChannelMessage(Common common) {
        this.common = common;
        this.text = common.getUpdate().getChannelPost().getText();
        this.chatId_str = common.getUpdate().getChannelPost().getChatId().toString();
        this.messageId = common.getUpdate().getChannelPost().getMessageId();

    }

    public abstract void handler();

}
