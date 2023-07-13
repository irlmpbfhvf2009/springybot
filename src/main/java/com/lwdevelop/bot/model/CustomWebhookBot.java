package com.lwdevelop.bot.model;

import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import com.lwdevelop.dto.SpringyBotDTO;

public class CustomWebhookBot extends TelegramWebhookBot {

    private String botToken;
    private String botUsername;
    // private String botPath;

    public CustomWebhookBot(SpringyBotDTO dto) {
        super(new CustomBotOptions(), dto.getToken());
        this.botToken = dto.getToken();
        this.botUsername = dto.getUsername();
        // this.botPath = dto.getBotPath();
    }

    @Override
    public String getBotUsername() {
        return this.botUsername;
    }

    @Override
    public String getBotToken() {
        return this.botToken;
    }

    @Override
    public String getBotPath() {
        return "";
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update arg0) {
        return null;
    }

}
