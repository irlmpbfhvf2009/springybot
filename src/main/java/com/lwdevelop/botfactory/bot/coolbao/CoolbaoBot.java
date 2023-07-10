package com.lwdevelop.botfactory.bot.coolbao;

import com.lwdevelop.botfactory.BaseBot;
import com.lwdevelop.botfactory.bot.coolbao.handler.CallbackQuerys;
import com.lwdevelop.botfactory.bot.coolbao.handler.PrivateMessage_;
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
