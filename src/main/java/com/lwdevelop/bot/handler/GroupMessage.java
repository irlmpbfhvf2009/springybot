package com.lwdevelop.bot.handler;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import com.lwdevelop.bot.utils.CommonUtils;
import com.lwdevelop.entity.SpringyBot;

public class GroupMessage {
    public void handler(CommonUtils common,Message message,SendMessage response,SpringyBot springyBot){


        if(springyBot.getConfig().getInviteFriendsSet()){

        }
        // System.out.println(message.getNewChatMembers());
        // System.out.println(message);
        
    }
}
