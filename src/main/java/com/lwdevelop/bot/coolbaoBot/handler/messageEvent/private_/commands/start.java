package com.lwdevelop.bot.coolbaoBot.handler.messageEvent.private_.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import com.lwdevelop.bot.coolbaoBot.utils.Common;

public class start {
    public void cmd(Common common){
        Message message = common.getUpdate().getMessage();
        String chatId = String.valueOf(message.getChatId());
        SendMessage response = new SendMessage(chatId, "hi");
        common.sendResponseAsync(response);
    }
}
