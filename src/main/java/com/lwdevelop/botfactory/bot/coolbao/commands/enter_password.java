package com.lwdevelop.botfactory.bot.coolbao.commands;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import com.lwdevelop.botfactory.Common;
import com.lwdevelop.botfactory.bot.coolbao.utils.CoolbaoEnum;
import com.lwdevelop.entity.SpringyBot;
import com.lwdevelop.entity.WhiteList;
import com.lwdevelop.service.impl.SpringyBotServiceImpl;
import com.lwdevelop.utils.SpringUtils;

public class enter_password {

    @Autowired
    private SpringyBotServiceImpl springyBotServiceImpl = SpringUtils.getApplicationContext()
            .getBean(SpringyBotServiceImpl.class);

    public void ep(Common common) {

        String text = common.getUpdate().getMessage().getText();
        Long chatId = common.getUpdate().getMessage().getChatId();
        String userId = String.valueOf(chatId);

        SpringyBot springyBot = springyBotServiceImpl.findById(common.getSpringyBotId()).get();
        String password = springyBot.getConfig().getPassword();
        List<WhiteList> whiteList = springyBotServiceImpl.findWhiteListBySpringyBotId(common.getSpringyBotId());

        if (text.equals(password)) {

            SendMessage response = new SendMessage(String.valueOf(chatId), userId + " 已加白");
            common.executeAsync(response);

            response = new SendMessage(String.valueOf(chatId),
                    "歡迎使用 @" + common.getUsername() + "\n\n" + CoolbaoEnum.COMMANDS_HELP.getText());
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
