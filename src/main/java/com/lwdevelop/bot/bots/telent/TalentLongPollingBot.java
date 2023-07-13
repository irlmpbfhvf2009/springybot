package com.lwdevelop.bot.bots.telent;

import com.lwdevelop.bot.chatMembershipHandlers.JoinChannel;
import com.lwdevelop.bot.chatMembershipHandlers.JoinGroup;
import com.lwdevelop.bot.chatMembershipHandlers.LeaveChannel;
import com.lwdevelop.bot.chatMembershipHandlers.LeaveGroup;
import com.lwdevelop.bot.factory.BaseLongPollingBot;
import com.lwdevelop.bot.bots.telent.messageHandling.CallbackQuerys;
import com.lwdevelop.bot.bots.telent.messageHandling.ChannelMessage;
import com.lwdevelop.bot.bots.telent.messageHandling.GroupMessage;
import com.lwdevelop.bot.bots.telent.messageHandling.PrivateMessage_;
import com.lwdevelop.entity.SpringyBot;

public class TalentLongPollingBot extends BaseLongPollingBot {

    public TalentLongPollingBot(SpringyBot springyBot) {
        super(springyBot);
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
