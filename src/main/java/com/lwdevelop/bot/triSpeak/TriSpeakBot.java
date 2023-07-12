package com.lwdevelop.bot.triSpeak;

import com.lwdevelop.bot._factory.BaseBot;
import com.lwdevelop.bot._factory.chatMembership.JoinChannel;
import com.lwdevelop.bot._factory.chatMembership.JoinGroup;
import com.lwdevelop.bot._factory.chatMembership.LeaveChannel;
import com.lwdevelop.bot._factory.chatMembership.LeaveGroup;
import com.lwdevelop.bot.triSpeak.messageHandling.CallbackQuerys;
import com.lwdevelop.bot.triSpeak.messageHandling.ChannelMessage;
import com.lwdevelop.bot.triSpeak.messageHandling.GroupMessage;
import com.lwdevelop.bot.triSpeak.messageHandling.PrivateMessage_;
import com.lwdevelop.dto.SpringyBotDTO;

public class TriSpeakBot extends BaseBot {

    public TriSpeakBot(SpringyBotDTO dto) {
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

        Boolean isJoin = chatMember.getStatus().equals("left") || chatMember.getStatus().equals("kicked") ? false
                : true;

        if (type.equals("group") || type.equals("supergroup")) {
            if (isJoin) {
                new JoinGroup(common).handler();
            } else {
                new LeaveGroup(common).handler();
            }
        } else if (type.equals("channel")) {
            if (isJoin) {
                new JoinChannel(common).handler();
            } else {
                new LeaveChannel(common).handler();
            }
        }

    }

}
