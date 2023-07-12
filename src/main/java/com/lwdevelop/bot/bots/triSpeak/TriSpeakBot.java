package com.lwdevelop.bot.bots.triSpeak;

import com.lwdevelop.bot.chatMembershipHandlers.JoinChannel;
import com.lwdevelop.bot.chatMembershipHandlers.JoinGroup;
import com.lwdevelop.bot.chatMembershipHandlers.LeaveChannel;
import com.lwdevelop.bot.chatMembershipHandlers.LeaveGroup;
import com.lwdevelop.bot.factory.BaseBot;
import com.lwdevelop.bot.bots.triSpeak.messageHandling.CallbackQuerys;
import com.lwdevelop.bot.bots.triSpeak.messageHandling.ChannelMessage;
import com.lwdevelop.bot.bots.triSpeak.messageHandling.GroupMessage;
import com.lwdevelop.bot.bots.triSpeak.messageHandling.PrivateMessage_;
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
