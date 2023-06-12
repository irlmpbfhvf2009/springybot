package com.lwdevelop.bot.test;

import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import com.lwdevelop.bot.Common;
import com.lwdevelop.dto.SpringyBotDTO;

public class adam extends TelegramWebhookBot {

    public Common common;
    private SpringyBotDTO dto;

    public adam(SpringyBotDTO springyBotDTO) {
        this.dto = springyBotDTO;
    }

    @Override
    public String getBotPath() {
        return "adam";
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        System.out.println("asd");
        Long chatId = update.getMessage().getChatId();
        System.out.println(chatId);
        System.out.println("asd");
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
