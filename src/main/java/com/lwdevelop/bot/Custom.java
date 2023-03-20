package com.lwdevelop.bot;

import org.springframework.scheduling.annotation.Async;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

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
        return this.username;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            Long chatId = message.getChatId();
            String text = message.getText();
            if (text != null) {
                switch (text) {
                    case "/start":
                        if(message.getChat().getType().equals("private")){
                            text = SpringyBotEnum.CALLBACKSENUM.getText();
                        }
                        break;
                    default:
                        text = "不处理该类指令";
                        break;
                }
            }
            this.sendTextMsg(text, chatId.toString());
        }
    }

    @SneakyThrows
    @Async
    public void sendTextMsg(String text, String chatId) {
        SendMessage response = new SendMessage();
        KeyboardButton k = new KeyboardButton();
        response.setReplyMarkup(k.StartReplyKeyboardMarkup());
        response.setDisableNotification(false);
        response.setChatId(chatId);
        response.setText(text);
        executeAsync(response);
    }

}