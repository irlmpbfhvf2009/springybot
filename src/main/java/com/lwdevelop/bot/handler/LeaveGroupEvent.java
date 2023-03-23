package com.lwdevelop.bot.handler;

import org.telegram.telegrambots.meta.api.objects.Message;

import com.lwdevelop.service.impl.SpringyBotServiceImpl;

public class LeaveGroupEvent {
    public void handler(Message message,SpringyBotServiceImpl springyBotServiceImpl){
        System.out.println("test LeaveGroupEvent");
        System.out.println(message);
        System.out.println("------------------");
        System.out.println(message.getLeftChatMember());
    }
}
