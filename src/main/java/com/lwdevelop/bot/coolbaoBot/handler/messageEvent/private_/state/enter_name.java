package com.lwdevelop.bot.coolbaoBot.handler.messageEvent.private_.state;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import com.lwdevelop.bot.coolbaoBot.utils.Common;
import com.lwdevelop.bot.coolbaoBot.utils.KeyboardButton;
import com.lwdevelop.bot.coolbaoBot.utils.SpringyBotEnum;
import com.lwdevelop.entity.SpringyBot;
import com.lwdevelop.service.impl.SpringyBotServiceImpl;
import com.lwdevelop.utils.SpringUtils;

public class enter_name {
    
    @Autowired
    private SpringyBotServiceImpl springyBotServiceImpl = SpringUtils.getApplicationContext()
            .getBean(SpringyBotServiceImpl.class);

    public void en(Common common) {
        String text = common.getUpdate().getMessage().getText();
        String chatId = String.valueOf(common.getUpdate().getMessage().getChatId());

        SpringyBot springyBot= springyBotServiceImpl.findById(common.getSpringyBotId()).get();

        springyBot.getWhiteList().stream().filter(wl->wl.getUserId().equals(common.getUpdate().getMessage().getChatId())).findFirst().ifPresent(action->{
            action.setName(text);
        });

        springyBotServiceImpl.save(springyBot);

        
        // SendMessage response = new SendMessage(chatId, text + "");
        // common.sendResponseAsync(response);

        springyBot.getWhiteList().stream().filter(wl -> wl.getUserId().equals(common.getUpdate().getMessage().getChatId())).findFirst().ifPresent(action->{
            SendMessage response = new SendMessage(chatId, "Your user ID: " + chatId + "\nCurrent name: "+action.getName());
            response.setReplyMarkup(new KeyboardButton().loginMarkupInline());
            common.sendResponseAsync(response);
            common.getUserState().put(common.getUpdate().getMessage().getChatId(), "");
        });

        SendMessage response = new SendMessage(chatId, "歡迎使用 @" + common.getUsername() + "\n\n" + SpringyBotEnum.COMMANDS_HELP.getText());
        common.sendResponseAsync(response);


    }
}
