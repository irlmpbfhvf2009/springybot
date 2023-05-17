package com.lwdevelop.bot.triSpeak.utils;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class GroupMessageHandler {

    @Async
    public void processSendMessage(String chatId, String warningText, int deleteSeconds, Common common) {

        SendMessage response = new SendMessage(chatId, warningText);
        Integer msgId = common.executeAsync(response);
        common.deleteMessageTask(chatId, msgId, deleteSeconds);
    }
}