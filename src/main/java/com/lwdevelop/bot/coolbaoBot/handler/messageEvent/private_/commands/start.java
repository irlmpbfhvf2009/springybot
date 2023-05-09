package com.lwdevelop.bot.coolbaoBot.handler.messageEvent.private_.commands;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import com.lwdevelop.bot.coolbaoBot.utils.Common;
import com.lwdevelop.bot.coolbaoBot.utils.SpringyBotEnum;
import com.lwdevelop.entity.SpringyBot;
import com.lwdevelop.entity.WhiteList;
import com.lwdevelop.service.impl.SpringyBotServiceImpl;
import com.lwdevelop.utils.SpringUtils;

public class start {

    @Autowired
    private SpringyBotServiceImpl springyBotServiceImpl = SpringUtils.getApplicationContext()
            .getBean(SpringyBotServiceImpl.class);
            
    public void cmd(Common common){

        SpringyBot springyBot = springyBotServiceImpl.findById(common.getSpringyBotId()).get();
        Set<WhiteList> whiteList = springyBot.getWhiteList();
        
        Message message = common.getUpdate().getMessage();
        String chatId = String.valueOf(message.getChatId());

        if (whiteList.stream().anyMatch(wl -> wl.getUserId().equals(message.getChatId()))) {
            SendMessage response = new SendMessage(chatId, "歡迎使用 @" + common.getUsername() + "\n\n" + SpringyBotEnum.COMMANDS_HELP.getText());
            common.sendResponseAsync(response);
        }else{
            SendMessage response = new SendMessage(chatId, "hi");
            common.sendResponseAsync(response);
        }
    }
}
