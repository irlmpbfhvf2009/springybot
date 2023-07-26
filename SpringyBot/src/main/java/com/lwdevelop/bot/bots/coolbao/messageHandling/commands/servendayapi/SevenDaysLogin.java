package com.lwdevelop.bot.bots.coolbao.messageHandling.commands.servendayapi;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import com.lwdevelop.bot.bots.coolbao.messageHandling.commands.utils.SevenDaysApiUtil;
import com.lwdevelop.bot.bots.utils.Common;

public class SevenDaysLogin extends SevenDaysApiUtil{

    public SevenDaysLogin(Common common){

        String token = sendPostRequest(SEVENDAYS_VALIDATE, "token");
        SendMessage response = new SendMessage(common.getUpdate().getMessage().getChatId().toString(), "生成 token : "+token);
        common.executeAsync_(response);
    }
    
}
