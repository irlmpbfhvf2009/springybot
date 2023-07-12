package com.lwdevelop.bot.bots.coolbao;

import com.lwdevelop.bot.bots.coolbao.messageHandling.CallbackQuerys;
import com.lwdevelop.bot.bots.coolbao.messageHandling.PrivateMessage_;
import com.lwdevelop.bot.factory.BaseBot;
import com.lwdevelop.dto.SpringyBotDTO;

public class CoolbaoBot extends BaseBot {

    public CoolbaoBot(SpringyBotDTO springyBotDTO) {
        super(springyBotDTO);
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
