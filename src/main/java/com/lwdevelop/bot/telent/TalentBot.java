package com.lwdevelop.bot.telent;

import com.lwdevelop.bot._factory.BaseBot;
import com.lwdevelop.bot._factory.chatMembership.JoinChannel;
import com.lwdevelop.bot._factory.chatMembership.JoinGroup;
import com.lwdevelop.bot._factory.chatMembership.LeaveChannel;
import com.lwdevelop.bot._factory.chatMembership.LeaveGroup;
import com.lwdevelop.bot.telent.messageHandling.CallbackQuerys;
import com.lwdevelop.bot.telent.messageHandling.ChannelMessage;
import com.lwdevelop.bot.telent.messageHandling.GroupMessage;
import com.lwdevelop.bot.telent.messageHandling.PrivateMessage_;
import com.lwdevelop.dto.SpringyBotDTO;

public class TalentBot extends BaseBot {

    public TalentBot(SpringyBotDTO springyBotDTO) {
        super(springyBotDTO);
    }

    @Override
    protected void handlePrivateMessage() {
        new PrivateMessage_(this.common).handler();
    }

    @Override
    protected void handleGroupMessage() {
        new GroupMessage(this.common).handler();
    }

    @Override
    protected void handlePhotoMessage() {
    }

    @Override
    protected void handleCallbackQuery() {
        new CallbackQuerys(this.common).handler();
    }

    @Override
    protected void handleChannelPost() {
        new ChannelMessage(this.common).handler();
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
