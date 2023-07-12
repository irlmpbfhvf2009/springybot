package com.lwdevelop.bot.coolbao.messageHandling.commands;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import com.lwdevelop.bot.utils.Common;
import com.lwdevelop.bot.utils.enum_.CoolbaoEnum;
import com.lwdevelop.bot.utils.keyboardButton.CoolbaoButton;
import com.lwdevelop.entity.SpringyBot;
import com.lwdevelop.entity.WhiteList;
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
        List<WhiteList> whiteList = springyBotServiceImpl.findWhiteListBySpringyBotId(common.getSpringyBotId());

        whiteList.stream().filter(wl->wl.getUserId().equals(common.getUpdate().getMessage().getChatId())).findAny().ifPresent(action->{
            action.setName(text);
        });
        springyBot.setWhiteList(whiteList);
        springyBotServiceImpl.save(springyBot);

        
        // SendMessage response = new SendMessage(chatId, text + "");
        // common.sendResponseAsync(response);

        whiteList.stream().filter(wl -> wl.getUserId().equals(common.getUpdate().getMessage().getChatId())).findAny().ifPresent(action->{
            SendMessage response = new SendMessage(chatId, "Your user ID: " + chatId + "\nCurrent name: "+action.getName());
            response.setReplyMarkup(new CoolbaoButton().loginMarkupInline());
            common.executeAsync(response);
            common.getUserState().put(common.getUpdate().getMessage().getChatId(), "");
        });

        SendMessage response = new SendMessage(chatId, "歡迎使用 @" + common.getUsername() + "\n\n" + CoolbaoEnum.COMMANDS_HELP.getText());
        common.executeAsync(response);


    }
}
