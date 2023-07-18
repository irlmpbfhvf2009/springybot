package com.lwdevelop.bot.bots.coolbao.messageHandling.commands;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;

import com.lwdevelop.bot.bots.utils.Common;
import com.lwdevelop.bot.bots.utils.enum_.CoolbaoEnum;
import com.lwdevelop.entity.SpringyBot;
import com.lwdevelop.entity.WhiteList;
import com.lwdevelop.service.impl.SpringyBotServiceImpl;
import com.lwdevelop.utils.SpringUtils;

public class EnterPassword {

    @Autowired
    private SpringyBotServiceImpl springyBotServiceImpl = SpringUtils.getApplicationContext()
            .getBean(SpringyBotServiceImpl.class);

    private String botUsername;
    private User user;
    private String text;
    private Long chatId;
    private String userId;

    public void ep(Common common) {

        this.text = common.getUpdate().getMessage().getText();
        this.chatId = common.getUpdate().getMessage().getChatId();
        this.userId = String.valueOf(chatId);
        this.botUsername = common.getBotUsername();
        this.user = common.getUpdate().getMessage().getFrom();
        SpringyBot springyBot = springyBotServiceImpl.findById(common.getSpringyBotId()).get();
        String password = springyBot.getConfig().getPassword();
        List<WhiteList> whiteList = springyBotServiceImpl.findWhiteListBySpringyBotId(common.getSpringyBotId());

        if (text.equals(password)) {

            SendMessage response = new SendMessage(String.valueOf(chatId), userId + " 已加白");
            common.executeAsync(response);

            response = new SendMessage(String.valueOf(chatId), CoolbaoEnum.commandsHelp(this.botUsername, user));
            response.setParseMode("HTML");
            response.setDisableWebPagePreview(true);
            common.executeAsync(response);

            common.getUserState().put(chatId, "");
            WhiteList wl = new WhiteList();
            wl.setUserId(chatId);
            whiteList.add(wl);
            springyBot.setWhiteList(whiteList);
            springyBotServiceImpl.save(springyBot);
        } else {
            SendMessage response = new SendMessage(String.valueOf(chatId), "輸入錯誤,退出輸入模式");
            common.executeAsync(response);
            common.getUserState().put(chatId, "");
        }

    }

}
