package com.lwdevelop.botfactory.bot.telent;

import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import com.lwdevelop.botfactory.BaseBot;
import com.lwdevelop.botfactory.bot.telent.handler.CallbackQuerys;
import com.lwdevelop.botfactory.bot.telent.handler.ChannelMessage;
import com.lwdevelop.botfactory.bot.telent.handler.GroupMessage;
import com.lwdevelop.botfactory.bot.telent.handler.PrivateMessage_;
import com.lwdevelop.botfactory.event.JoinChannel;
import com.lwdevelop.botfactory.event.JoinGroup;
import com.lwdevelop.botfactory.event.LeaveChannel;
import com.lwdevelop.botfactory.event.LeaveGroup;
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
    protected void handleChatMemberUpdate(ChatMember chatMember, String type) {
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
