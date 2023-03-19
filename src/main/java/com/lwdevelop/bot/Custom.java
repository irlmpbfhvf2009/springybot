package com.lwdevelop.bot;

import org.springframework.scheduling.annotation.Async;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import lombok.SneakyThrows;

public class Custom extends TelegramLongPollingBot {
    // 填你自己的token和username
    private String token;
    private String username;

    public Custom(String token,String username,DefaultBotOptions options) {
        super(options);
        this.token = token;
        this.username = username;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            Long chatId = message.getChatId();
            String text = message.getText();
            switch (text) {
                case "/a":
                    text = "AAAAAAAAAA";
                    break;
                case "/b":
                    text = "BBBBBBBBBB";
                    break;
                case "/c":
                    text = "CCCCCCCCCC";
                    break;
                default:
                    text = "不处理该类指令";
                    break;
            }
            this.sendTextMsg(text, chatId.toString());
        }
    }

    @Override
    public String getBotToken() {
        return this.token;
    }

    @Override
    public String getBotUsername() {
        return this.username;
    }

    @SneakyThrows
    @Async
    public void sendTextMsg(String text, String chatId) {
        SendMessage response = new SendMessage();
        response.setReplyMarkup(null);
        response.setDisableNotification(false);
        response.setChatId(chatId);
        response.setText(text);
        executeAsync(response);
    }

}