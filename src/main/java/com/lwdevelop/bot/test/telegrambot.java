package com.lwdevelop.bot.test;

import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.lwdevelop.bot.coolbao.utils.Common;
import com.lwdevelop.dto.SpringyBotDTO;

public class telegrambot extends TelegramWebhookBot {

    public Common common;
    private SpringyBotDTO dto;

    public telegrambot(SpringyBotDTO springyBotDTO) {
        this.dto = springyBotDTO;
    }

    @Override
    public String getBotPath() {
        return "/api/your_bot_path";
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        this.common.setUpdate(update);
        Message message = update.getMessage();
        if (message.hasText()) {
            if (message.isSuperGroupMessage()) {
                System.out.println("message: "+message.getText());
            }
        }
        return null;
    }

    @Override
    public String getBotToken() {
        return this.dto.getToken();
    }

    @Override
    public String getBotUsername() {
        return this.dto.getUsername();
    }

}
