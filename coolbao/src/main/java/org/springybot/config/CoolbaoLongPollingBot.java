package org.springybot.config;

import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.springybot.model.BaseLongPollingBot;

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
        throw new UnsupportedOperationException("Unimplemented method 'handleGroupMessage'");
    }

    @Override
    protected void handlePhotoMessage() {
        throw new UnsupportedOperationException("Unimplemented method 'handlePhotoMessage'");
    }

    @Override
    protected void handleCallbackQuery() {
        throw new UnsupportedOperationException("Unimplemented method 'handleCallbackQuery'");
    }

    @Override
    protected void handleChannelPost() {
        throw new UnsupportedOperationException("Unimplemented method 'handleChannelPost'");
    }

    @Override
    protected void handleChatMemberUpdate() {
        throw new UnsupportedOperationException("Unimplemented method 'handleChatMemberUpdate'");
    }

    @Override
    public String getBotUsername() {
        return this.botUsername;
    }
}
