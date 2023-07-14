package com.lwdevelop.bot.bots.talent;

import com.lwdevelop.bot.bots.talent.messageHandling.CallbackQuerys;
import com.lwdevelop.bot.bots.talent.messageHandling.ChannelMessage;
import com.lwdevelop.bot.bots.talent.messageHandling.GroupMessage;
import com.lwdevelop.bot.bots.talent.messageHandling.PrivateMessage_;
import com.lwdevelop.bot.chatMembershipHandlers.JoinChannel;
import com.lwdevelop.bot.chatMembershipHandlers.JoinGroup;
import com.lwdevelop.bot.chatMembershipHandlers.LeaveChannel;
import com.lwdevelop.bot.chatMembershipHandlers.LeaveGroup;
import com.lwdevelop.bot.factory.BaseLongPollingBot;
import com.lwdevelop.dto.SpringyBotDTO;

public class TalentLongPollingBot extends BaseLongPollingBot {

    public TalentLongPollingBot(SpringyBotDTO springyBotDTO) {
        super(springyBotDTO);
    }
    @Override
    protected void handlePrivateMessage() {
        System.out.println("test22");
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
