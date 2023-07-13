package com.lwdevelop.bot.bots.coolbao;

import com.lwdevelop.bot.bots.coolbao.messageHandling.CallbackQuerys;
import com.lwdevelop.bot.bots.coolbao.messageHandling.PrivateMessage_;
import com.lwdevelop.bot.factory.BaseLongPollingBot;
import com.lwdevelop.entity.SpringyBot;

public class CoolbaoLongPollingBot extends BaseLongPollingBot {

    public CoolbaoLongPollingBot(SpringyBot springyBot) {
        super(springyBot);
    }

    @Override
    protected void handlePrivateMessage() {
        new PrivateMessage_(common).handler();
    }

    @Override
    protected void handleGroupMessage() {
    }

    @Override
    protected void handlePhotoMessage() {
    }

    @Override
    protected void handleCallbackQuery() {
        new CallbackQuerys(common).handler();
    }

    @Override
    protected void handleChannelPost() {
    }

    @Override
    protected void handleChatMemberUpdate() {
    }


}
