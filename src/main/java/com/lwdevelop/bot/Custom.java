package com.lwdevelop.bot;

import org.springframework.scheduling.annotation.Async;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import com.lwdevelop.bot.handler.ChannelMessage;
import com.lwdevelop.bot.handler.GroupMessage;
import com.lwdevelop.bot.handler.JoinGroupEvent;
import com.lwdevelop.bot.handler.LeaveGroupEvent;
import com.lwdevelop.bot.handler.PrivateMessage;
import com.lwdevelop.bot.utils.CommonUtils;

import lombok.SneakyThrows;

public class Custom extends TelegramLongPollingBot {

    private String token;
    private String username;

    public Custom(String token, String username, DefaultBotOptions options) {
        super(options);
        this.token = token;
        this.username = username;
    }

    @Override
    public String getBotToken() {
        return this.token;
    }

    @Override
    public String getBotUsername() {
        try {
            this.username = getMe().getUserName();
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return this.username;
    }

    @Override
    public void onUpdateReceived(Update update) {
        CommonUtils commonUtils = new CommonUtils();

        // deal message if chatType = group or private
        if (update.hasMessage()) {
            Message message = update.getMessage();
            Long chatId = message.getChatId();
            // String text = message.getText();
            SendMessage response = new SendMessage();
            String chatType = message.getChat().getType();

            if (update.getMessage().hasText()) {
                // type : private
                if (commonUtils.chatTypeIsPrivate(chatType)) {
                    String privateMessage = new PrivateMessage().handler(commonUtils, message, response, this.username);
                        this.sendTextMsg(privateMessage, chatId.toString(), response);
                }

                // type : group
                if (commonUtils.chatTypeIsGroup(chatType)) {
                    new GroupMessage().handler(commonUtils, message, response);
                }
            }
        }

        // deal message if chatType = channel
        if (update.getChannelPost() != null) {
            // type : channel
            String chatType = update.getChannelPost().getChat().getType();
            if (update.getChannelPost().hasText()) {
                if (commonUtils.chatTypeIsChannel(chatType)) {
                    new ChannelMessage().handler();
                }
            }
        }

        // 群組新成員
        try {
            if (update.getMessage().getNewChatMembers() != null && update.getMessage().getNewChatMembers().size()!=0) {
                Message message = update.getMessage();
                new JoinGroupEvent().handler(message,username,this.token);
            }
            
            // 退群或被踢
            if (update.getMessage().getLeftChatMember() != null) {
                new LeaveGroupEvent().handler();
            }
        } catch (NullPointerException e) {
        }

    }

    @SneakyThrows
    @Async
    public void sendTextMsg(String text, String chatId) {
        SendMessage response = new SendMessage();
        response.setDisableNotification(false);
        response.setChatId(chatId);
        response.setText(text);
        executeAsync(response);
    }

    @SneakyThrows
    @Async
    public void sendTextMsg(String text,String chatId, SendMessage response) {
        response.setDisableNotification(false);
        response.setChatId(chatId);
        response.setText(text);
        executeAsync(response);
    }

}