package com.lwdevelop.bot.bots.talent;

import com.lwdevelop.bot.bots.talent.messageHandling.CallbackQuerys;
import com.lwdevelop.bot.bots.talent.messageHandling.ChannelMessage;
import com.lwdevelop.bot.bots.talent.messageHandling.GroupMessage;
import com.lwdevelop.bot.bots.talent.messageHandling.PrivateMessage_;
import com.lwdevelop.bot.chatMembershipHandlers.ChatHandler;
import com.lwdevelop.bot.factory.BaseLongPollingBot;
import com.lwdevelop.dto.SpringyBotDTO;

public class TalentLongPollingBot extends BaseLongPollingBot {

    public TalentLongPollingBot(SpringyBotDTO springyBotDTO) {
        super(springyBotDTO);
    }
    @Override
    protected void handlePrivateMessage() {
        new PrivateMessage_(common).handler();
    }

    @Override
    protected void handleGroupMessage() {
        new GroupMessage(common).handler();
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
        new ChannelMessage(common).handler();
    }

    @Override
    protected void handleChatMemberUpdate() {

        new ChatHandler(common).handleChatMemberStatus(type, chatMember);

    }

}
