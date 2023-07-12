package com.lwdevelop.bot.model;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.lwdevelop.dto.SpringyBotDTO;

public class CustomLongPollingBot extends TelegramLongPollingBot {


    private String botUsername;

    public CustomLongPollingBot(SpringyBotDTO dto) {
        super(new CustomBotOptions(), dto.getToken());
        this.botUsername = dto.getUsername();
    }

    @Override
    public void onUpdateReceived(Update arg0) {}

    @Override
    public String getBotUsername() {
        return this.botUsername;
    }

}
