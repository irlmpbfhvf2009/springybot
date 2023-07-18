package com.lwdevelop.bot.chatMembershipHandlers;

import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;

import com.lwdevelop.bot.bots.utils.Common;

public class ChatHandler {
    
    private Common common;

    public ChatHandler(Common common) {
        this.common = common;
    }

    public void handleChatMemberStatus(String type, ChatMember chatMember) {
        boolean isJoin = chatMember.getStatus().equals("left") || chatMember.getStatus().equals("kicked") ? false
                : true;

        if (type.equals("group") || type.equals("supergroup")) {
            handleGroup(isJoin);
        } else if (type.equals("channel")) {
            handleChannel(isJoin);
        }
    }

    private void handleGroup(boolean isJoin) {
        if (isJoin) {
            new JoinGroup(common).handler();
        } else {
            new LeaveGroup(common).handler();
        }
    }

    private void handleChannel(boolean isJoin) {
        if (isJoin) {
            new JoinChannel(common).handler();
        } else {
            new LeaveChannel(common).handler();
        }
    }
}
