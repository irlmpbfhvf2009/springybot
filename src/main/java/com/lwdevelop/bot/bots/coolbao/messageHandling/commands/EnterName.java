package com.lwdevelop.bot.bots.coolbao.messageHandling.commands;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;

import com.lwdevelop.bot.bots.utils.Common;
import com.lwdevelop.bot.bots.utils.enum_.CoolbaoEnum;
import com.lwdevelop.bot.bots.utils.keyboardButton.CoolbaoButton;
import com.lwdevelop.entity.SpringyBot;
import com.lwdevelop.entity.WhiteList;
import com.lwdevelop.service.impl.SpringyBotServiceImpl;
import com.lwdevelop.utils.SpringUtils;

public class EnterName {

    @Autowired
    private SpringyBotServiceImpl springyBotServiceImpl = SpringUtils.getApplicationContext()
            .getBean(SpringyBotServiceImpl.class);

    private String text;
    private String chatId;
    private User user;
    private String botUsername;

    public void en(Common common) {
        this.text = common.getUpdate().getMessage().getText();
        this.chatId = String.valueOf(common.getUpdate().getMessage().getChatId());
        this.user = common.getUpdate().getMessage().getFrom();
        this.botUsername = common.getBotUsername();

        SpringyBot springyBot= springyBotServiceImpl.findById(common.getSpringyBotId()).get();
        List<WhiteList> whiteList = springyBotServiceImpl.findWhiteListBySpringyBotId(common.getSpringyBotId());

        whiteList.stream().filter(wl->wl.getUserId().equals(common.getUpdate().getMessage().getChatId())).findAny().ifPresent(action->{
            action.setName(text);
        });
        springyBot.setWhiteList(whiteList);
        springyBotServiceImpl.save(springyBot);


        whiteList.stream().filter(wl -> wl.getUserId().equals(common.getUpdate().getMessage().getChatId())).findAny().ifPresent(action->{
            SendMessage response = new SendMessage(chatId, "Your user ID: " + chatId + "\nCurrent name: "+action.getName());
            response.setReplyMarkup(new CoolbaoButton().loginMarkupInline());
            common.executeAsync(response);
            common.getUserState().put(common.getUpdate().getMessage().getChatId(), "");
        });

        SendMessage response = new SendMessage(chatId, CoolbaoEnum.commandsHelp(this.botUsername, user));
        response.setParseMode("HTML");
        response.setDisableWebPagePreview(true);
        common.executeAsync(response);


    }
}
