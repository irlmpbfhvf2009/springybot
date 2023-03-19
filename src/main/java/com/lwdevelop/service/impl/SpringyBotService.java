package com.lwdevelop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.WebhookBot;

import com.lwdevelop.config.SpringyBotConfig;

@Service
public class SpringyBotService implements WebhookBot {

    @Autowired
    private SpringyBotConfig botConfig;

    @Override
    public String getBotToken() {
        System.out.println(botConfig.getToken());
        return botConfig.getToken();
    }

    @Override
    public String getBotUsername() {
        System.out.println(botConfig.getBotUsername());
        return botConfig.getBotUsername();
    }

    @Override
    public String getBotPath() {
        System.out.println(botConfig.getBotPath());
        return botConfig.getBotPath();
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        if (update.getMessage() != null && update.getMessage().hasText()) {
            Long chatId = update.getMessage().getChatId();
            System.out.println(chatId);
            new SendMessage(chatId.toString(), "HI HANDSOME " + update.getMessage().getText());
          }
          return null;
    }

    @Override
    public void setWebhook(SetWebhook arg0) throws TelegramApiException {
    }



}
