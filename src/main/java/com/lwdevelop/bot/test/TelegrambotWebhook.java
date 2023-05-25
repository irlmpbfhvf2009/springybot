package com.lwdevelop.bot.test;

import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import com.lwdevelop.bot.Common;
import com.lwdevelop.dto.SpringyBotDTO;

public class TelegrambotWebhook extends TelegramWebhookBot {

    public Common common;
    private SpringyBotDTO dto;

    public TelegrambotWebhook(SpringyBotDTO springyBotDTO) {
        this.dto = springyBotDTO;
    }

    @Override
    public String getBotPath() {
        return "adam";
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        Long chatId = update.getMessage().getChatId();
        Integer msgId = update.getMessage().getMessageId();
        String id = String.valueOf(chatId);
        DeleteMessage deleteMessage = new DeleteMessage(id, msgId);
        try {
            this.executeAsync(deleteMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
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
