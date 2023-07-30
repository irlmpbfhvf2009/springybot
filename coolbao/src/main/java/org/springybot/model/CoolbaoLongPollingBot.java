package org.springybot.model;

import org.springybot.botModel.BaseLongPollingBot;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class CoolbaoLongPollingBot extends BaseLongPollingBot {

    public CoolbaoLongPollingBot(DefaultBotOptions options, String botUsername, String botToken) {
        super(options, botUsername, botToken);
    }

    @Override
    protected void handlePrivateMessage() {
        SendMessage sendMessage = new SendMessage(chatId_str, text);
        try {
            this.executeAsync(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void handleGroupMessage() {
    }

    @Override
    protected void handlePhotoMessage() {
    }

    @Override
    protected void handleCallbackQuery() {
    }

    @Override
    protected void handleChannelPost() {
    }

    @Override
    protected void handleChatMemberUpdate() {
    }

    @Override
    public String getBotUsername() {
        return this.botUsername;
    }
}
