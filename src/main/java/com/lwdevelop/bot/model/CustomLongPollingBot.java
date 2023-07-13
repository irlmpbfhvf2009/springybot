package com.lwdevelop.bot.model;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import com.lwdevelop.entity.SpringyBot;

public class CustomLongPollingBot extends TelegramLongPollingBot {

    private String token;
    private String botUsername;

    public CustomLongPollingBot(SpringyBot springyBot) {
        super(new CustomBotOptions(), springyBot.getToken());
        this.token = springyBot.getToken();
        this.botUsername = springyBot.getUsername();
    }

    @Override
    public void onUpdateReceived(Update arg0) {}

    @Override
    public String getBotUsername() {
        return this.botUsername;
    }

    @Override
    public String getBotToken() {
        return this.token;
    }

}
