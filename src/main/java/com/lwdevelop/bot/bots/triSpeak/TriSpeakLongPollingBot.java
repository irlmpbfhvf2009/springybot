package com.lwdevelop.bot.bots.triSpeak;

import com.lwdevelop.bot.chatMembershipHandlers.ChatHandler;
import com.lwdevelop.bot.factory.BaseLongPollingBot;
import com.lwdevelop.bot.bots.triSpeak.messageHandling.CallbackQuerys;
import com.lwdevelop.bot.bots.triSpeak.messageHandling.ChannelMessage;
import com.lwdevelop.bot.bots.triSpeak.messageHandling.GroupMessage;
import com.lwdevelop.bot.bots.triSpeak.messageHandling.PrivateMessage_;
import com.lwdevelop.dto.SpringyBotDTO;

public class TriSpeakLongPollingBot extends BaseLongPollingBot {

    public TriSpeakLongPollingBot(SpringyBotDTO dto) {
        super(dto);
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
        // Handle photo messages
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
