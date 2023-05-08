package com.lwdevelop.bot.coolbaoBot.handler.messageEvent.private_.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import com.lwdevelop.bot.coolbaoBot.utils.Common;
import com.lwdevelop.entity.SpringyBot;
import com.lwdevelop.service.impl.SpringyBotServiceImpl;
import com.lwdevelop.utils.SpringUtils;

public class login {

    @Autowired
    private SpringyBotServiceImpl springyBotServiceImpl = SpringUtils.getApplicationContext()
            .getBean(SpringyBotServiceImpl.class);

    public void cmd(Common common) {
        Message message = common.getUpdate().getMessage();
        String chatId = String.valueOf(message.getChatId());
        SpringyBot springyBot = springyBotServiceImpl.findById(common.getSpringyBotId()).get();

        if (springyBot.getWhiteList().stream().anyMatch(wl -> wl.getUserId().equals(message.getChatId()))) {
            SendMessage response = new SendMessage(chatId, "安安");
            common.sendResponseAsync(response);
            common.getUserState().put(message.getChatId(), "");
        } else {
            SendMessage response = new SendMessage(chatId, "Please enter password");
            common.sendResponseAsync(response);
            common.getUserState().put(message.getChatId(), "enter_password");
        }
    }
}
