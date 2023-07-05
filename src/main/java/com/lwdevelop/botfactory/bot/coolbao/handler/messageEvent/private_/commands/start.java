package com.lwdevelop.botfactory.bot.coolbao.handler.messageEvent.private_.commands;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import com.lwdevelop.botfactory.Common;
import com.lwdevelop.botfactory.bot.coolbao.utils.SpringyBotEnum;
import com.lwdevelop.entity.WhiteList;
import com.lwdevelop.service.impl.SpringyBotServiceImpl;
import com.lwdevelop.utils.SpringUtils;

public class start {

    @Autowired
    private SpringyBotServiceImpl springyBotServiceImpl = SpringUtils.getApplicationContext()
            .getBean(SpringyBotServiceImpl.class);
            
    public void cmd(Common common){

        List<WhiteList> whiteList = springyBotServiceImpl.findWhiteListBySpringyBotId(common.getSpringyBotId());
        
        Message message = common.getUpdate().getMessage();
        String chatId = String.valueOf(message.getChatId());

        if (whiteList.stream().anyMatch(wl -> wl.getUserId().equals(message.getChatId()))) {
            SendMessage response = new SendMessage(chatId, "歡迎使用 @" + common.getUsername() + "\n\n" + SpringyBotEnum.COMMANDS_START.getText() +"\n\n/help - 幫助");
            common.executeAsync(response);
        }else{
            SendMessage response = new SendMessage(chatId, "歡迎使用 @" + common.getUsername() + "\n\n" + SpringyBotEnum.COMMANDS_START.getText() +"\n\n");
            common.executeAsync(response);
        }
    }
}
