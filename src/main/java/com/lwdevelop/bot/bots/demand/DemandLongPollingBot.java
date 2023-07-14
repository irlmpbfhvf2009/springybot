package com.lwdevelop.bot.bots.demand;

import com.lwdevelop.bot.bots.demand.messageHandling.CallbackQuerys;
import com.lwdevelop.bot.bots.demand.messageHandling.ChannelMessage;
import com.lwdevelop.bot.bots.demand.messageHandling.GroupMessage;
import com.lwdevelop.bot.bots.demand.messageHandling.PrivateMessage_;
import com.lwdevelop.bot.chatMembershipHandlers.JoinChannel;
import com.lwdevelop.bot.chatMembershipHandlers.JoinGroup;
import com.lwdevelop.bot.chatMembershipHandlers.LeaveChannel;
import com.lwdevelop.bot.chatMembershipHandlers.LeaveGroup;
import com.lwdevelop.bot.factory.BaseLongPollingBot;
import com.lwdevelop.dto.SpringyBotDTO;

public class DemandLongPollingBot extends BaseLongPollingBot {

    public DemandLongPollingBot (SpringyBotDTO springyBotDTO) {
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
