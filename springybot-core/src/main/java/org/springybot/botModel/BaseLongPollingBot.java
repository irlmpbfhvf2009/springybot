package org.springybot.botModel;

import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BaseLongPollingBot extends TelegramLongPollingBot {

    protected Update update;
    protected Message message;
    protected String type;
    protected ChatMember chatMember;
    protected String botUsername;
    protected Long chatId;
    protected String chatId_str;
    protected String text;

    public BaseLongPollingBot(DefaultBotOptions options, String botUsername, String botToken) {
        super(options, botToken);
        this.botUsername = botUsername;
    }

    @Override
    public void onUpdateReceived(Update update) {
        this.update = update;
        System.out.println(update);
        if (update.hasMessage()) {

            this.message = update.getMessage();
            this.chatId = update.getMessage().getChatId();
            this.chatId_str = update.getMessage().getChatId().toString();
            this.text = update.getMessage().getText();

            if (message.hasText()) {
                if (message.isUserMessage()) {
                    handlePrivateMessage();
                    User user = message.getFrom();
                    String userInfo = String.format("[%s] @%s (%s %s)", user.getId(), user.getUserName(),
                            user.getFirstName(), user.getLastName());
                    log.info("[{}] Private message received from {}: {}", this.botUsername, userInfo,
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
