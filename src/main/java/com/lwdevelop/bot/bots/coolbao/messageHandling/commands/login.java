package com.lwdevelop.bot.bots.coolbao.messageHandling.commands;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import com.lwdevelop.bot.bots.utils.Common;
import com.lwdevelop.bot.bots.utils.keyboardButton.CoolbaoButton;
import com.lwdevelop.entity.WhiteList;
import com.lwdevelop.service.impl.SpringyBotServiceImpl;
import com.lwdevelop.utils.SpringUtils;

public class login {

    @Autowired
    private SpringyBotServiceImpl springyBotServiceImpl = SpringUtils.getApplicationContext()
            .getBean(SpringyBotServiceImpl.class);

    public void cmd(Common common) {
        Message message = common.getUpdate().getMessage();
        String chatId = String.valueOf(message.getChatId());
        List<WhiteList> whiteList = springyBotServiceImpl.findWhiteListBySpringyBotId(common.getSpringyBotId());
        if (whiteList.stream().anyMatch(wl -> wl.getUserId().equals(message.getChatId()))) {
            whiteList.stream().filter(wl -> wl.getUserId().equals(message.getChatId())).findAny().ifPresent(action->{
                SendMessage response = new SendMessage(chatId, "Your user ID: " + chatId + "\nCurrent name: "+action.getName());
                response.setReplyMarkup(new CoolbaoButton().loginMarkupInline());
                common.executeAsync(response);
                common.getUserState().put(message.getChatId(), "");
            });
        } else {
            SendMessage response = new SendMessage(chatId, "Please enter password");
            common.executeAsync(response);
            common.getUserState().put(message.getChatId(), "enter_password");
        }
    }
}
