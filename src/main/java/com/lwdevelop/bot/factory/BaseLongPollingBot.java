package com.lwdevelop.bot.factory;

import java.util.HashMap;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import com.lwdevelop.bot.model.CustomLongPollingBot;
import com.lwdevelop.bot.bots.utils.Common;
import com.lwdevelop.entity.SpringyBot;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public abstract class BaseLongPollingBot extends CustomLongPollingBot {

    protected Common common;
    protected Message message;
    protected String type;
    protected ChatMember chatMember;

    public BaseLongPollingBot(SpringyBot springyBot) {
        super(springyBot);
        this.common = initializeCommon(springyBot);
    }

    private Common initializeCommon(SpringyBot springyBot) {
        try {
            Common common = new Common();
            common.setSpringyBotId(springyBot.getId());
            common.setBotId(getMe().getId());
            common.setBotUsername(getMe().getUserName());
            common.setBot(this);
            common.setUserState(new HashMap<>());
            common.setGroupMessageMap(new HashMap<>());
            common.setGroupMessageMap2(new HashMap<>());
            return common;
        } catch (TelegramApiException e) {
            log.error("Failed to initialize Common", e);
            throw new RuntimeException("Failed to initialize Common", e);
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        common.setUpdate(update);
        message = update.getMessage();

        if (update.hasMessage()) {
            if (message.hasText()) {
                if (message.isUserMessage()) {
                    handlePrivateMessage();
                    User user = message.getFrom();
                    String userInfo = String.format("[%s] @%s (%s %s)", user.getId(), user.getUserName(),
                            user.getFirstName(), user.getLastName());
                    log.info("[{}] Private message received from {}: {}", this.common.getBotUsername(), userInfo,
                            this.message.getText());
                }
                if (message.isSuperGroupMessage() || message.isGroupMessage()) {
                    handleGroupMessage();
                }
            }

            if (message.hasPhoto()) {
                handlePhotoMessage();
            }
        }

        if (update.hasCallbackQuery()) {
            User user = update.getCallbackQuery().getFrom();
            String userInfo = String.format("[%s] %s (%s %s)", user.getId(), user.getUserName(), user.getFirstName(),
                    user.getLastName());
            handleCallbackQuery();
            log.info("CallbackQuery Data received from {}: {}", userInfo, update.getCallbackQuery().getData());
        }

        if (update.hasChannelPost()) {
            if (update.getChannelPost().getChat().getType().equals("channel")) {
                handleChannelPost();
            }

        }

        if (update.hasMyChatMember() || update.hasChatMember()) {
            if (update.hasMyChatMember()) {
                this.type = update.getMyChatMember().getChat().getType();
                this.chatMember = update.getMyChatMember().getNewChatMember();
            }
            if (update.hasChatMember()) {
                this.type = update.getChatMember().getChat().getType();
                this.chatMember = update.getChatMember().getNewChatMember();
            }

            if (this.chatMember != null && this.type != null) {
                handleChatMemberUpdate();
            }

        }
    }

    protected abstract void handlePrivateMessage();

    protected abstract void handleGroupMessage();

    protected abstract void handlePhotoMessage();

    protected abstract void handleCallbackQuery();

    protected abstract void handleChannelPost();

    protected abstract void handleChatMemberUpdate();

}
