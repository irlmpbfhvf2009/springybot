package org.springybot.botModel.messageHandler;

import org.springybot.botModel.Common;
import org.telegram.telegrambots.meta.api.objects.Message;

public abstract class BasePrivateMessage_ {

    protected Common common;
    protected Message message;
    protected String text;
    protected Long chatId;
    protected String chatId_str;
    protected Long springyBotId;

    public BasePrivateMessage_(Common common){
        this.common = common;
        this.text = common.getUpdate().getMessage().getText();
        this.message = common.getUpdate().getMessage();
        this.chatId = common.getUpdate().getMessage().getChatId();
        this.chatId_str = common.getUpdate().getMessage().getChatId().toString();
        this.springyBotId = this.common.getSpringyBotId();
    }

    public abstract void handler();
}
