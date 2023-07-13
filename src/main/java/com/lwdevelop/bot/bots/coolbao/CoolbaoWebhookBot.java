package com.lwdevelop.bot.bots.coolbao;

import com.lwdevelop.bot.bots.coolbao.messageHandling.CallbackQuerys;
import com.lwdevelop.bot.bots.coolbao.messageHandling.PrivateMessage_;
import com.lwdevelop.bot.factory.BaseWebhookBot;
import com.lwdevelop.dto.SpringyBotDTO;

public class CoolbaoWebhookBot extends BaseWebhookBot {

    public CoolbaoWebhookBot(SpringyBotDTO dto) {
        super(dto);
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
    
    @Override
    public String getBotPath() {
        return "adam";
    }


}
