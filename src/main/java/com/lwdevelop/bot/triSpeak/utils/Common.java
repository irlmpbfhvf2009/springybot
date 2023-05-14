package com.lwdevelop.bot.triSpeak.utils;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import org.springframework.scheduling.annotation.Async;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChat;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.ChatMemberUpdated;
import org.telegram.telegrambots.meta.api.objects.Update;

import lombok.Data;
import lombok.SneakyThrows;

@Data
public class Common {

    private Long springyBotId;

    private Long botId;

    private String username;

    private Update update;

    private String inviteLink;

    private TelegramLongPollingBot bot;

    private ChatMemberUpdated chatMemberUpdated;

    // 用来存储用户的状态(会话)
    private HashMap<Long, String> userState;

    public Common(Long springyBotId, Long botId, String username) {
        this.springyBotId = springyBotId;
        this.botId = botId;
        this.username = username;
    }

    public void deleteMessageTask(String chatId,Integer messageId,int second){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                DeleteMessage deleteMessage = new DeleteMessage(chatId, messageId);
                executeAsync(deleteMessage);
            }
        }, second, second);
    }
    
    @Async
    @SneakyThrows
    public Integer executeAsync(SendMessage response) {
        return this.bot.executeAsync(response).get().getMessageId();
    }

    @Async
    @SneakyThrows
    public Integer executeAsync(SendPhoto response) {
        return this.bot.executeAsync(response).get().getMessageId();
    }

    @Async
    @SneakyThrows
    public void executeAsync(EditMessageText response) {
        this.bot.executeAsync(response);
    }
    @Async
    @SneakyThrows
    public void executeAsync(DeleteMessage response) {
        this.bot.executeAsync(response);
    }

    @Async
    @SneakyThrows
    public String executeAsync(GetChatMember getChatMember) {
        return this.bot.executeAsync(getChatMember).get().getStatus();
    }

    @Async
    @SneakyThrows
    public Chat executeAsync(GetChat getChat) {
        return this.bot.executeAsync(getChat).get();
    }

}
