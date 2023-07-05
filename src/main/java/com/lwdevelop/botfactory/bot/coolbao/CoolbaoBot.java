package com.lwdevelop.botfactory.bot.coolbao;

import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import com.lwdevelop.botfactory.BaseBot;
import com.lwdevelop.botfactory.bot.coolbao.handler.CallbackQuerys;
import com.lwdevelop.botfactory.bot.coolbao.handler.messageEvent.private_.PrivateMessage_;
import com.lwdevelop.dto.SpringyBotDTO;

public class CoolbaoBot extends BaseBot {

    public CoolbaoBot(SpringyBotDTO springyBotDTO) {
        super(springyBotDTO);
    }

    @Override
    protected void handlePrivateMessage() {
        new PrivateMessage_().handler(this.common);
    }

    @Override
    protected void handleGroupMessage() {
    }

    @Override
    protected void handlePhotoMessage() {
    }

    @Override
    protected void handleCallbackQuery() {
        new CallbackQuerys().handler(common);
    }

    @Override
    protected void handleChannelPost() {
    }

    @Override
    protected void handleChatMemberUpdate(ChatMember chatMember,String type) {
    }

}
