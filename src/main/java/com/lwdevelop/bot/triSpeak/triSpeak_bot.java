package com.lwdevelop.bot.triSpeak;

import java.util.HashMap;

import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import com.lwdevelop.bot.triSpeak.handler.CallbackQuerys;
import com.lwdevelop.bot.triSpeak.handler.ChannelMessage;
import com.lwdevelop.bot.triSpeak.handler.GroupMessage;
import com.lwdevelop.bot.triSpeak.handler.PrivateMessage_;
import com.lwdevelop.bot.triSpeak.utils.Common;
import com.lwdevelop.bot.triSpeak.utils.SpringyBotEnum;
import com.lwdevelop.dto.SpringyBotDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class triSpeak_bot extends TelegramLongPollingBot {

    public Common common;
    private SpringyBotDTO dto;
    private Message message;

    public triSpeak_bot(SpringyBotDTO springyBotDTO) {
        super(new DefaultBotOptions());
        this.dto = springyBotDTO;

        try {
            this.common = new Common(dto.getId(), getMe().getId(), getMe().getUserName());
            this.common.setBot(this);
            this.common.setUserState(new HashMap<>());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    public void onUpdateReceived(Update update) {
        this.init(update);

        // deal message group or private chat
        if (update.hasMessage()) {

            if (this.message.hasText()) {
                User user = this.message.getFrom();
                String userInfo = String.format("[%s] @%s (%s %s)", user.getId(), user.getUserName(),
                        user.getFirstName(), user.getLastName());

                // private
                if (this.message.isUserMessage()) {
                    new PrivateMessage_(this.common).handler();
                    log.info("[{}] Private message received from {}: {}", this.common.getUsername(), userInfo,
                            this.message.getText());

                }
                // group
                if (this.message.isSuperGroupMessage()) {
                    new GroupMessage(this.common).handler();
                    log.info("[{}] Group message received from {}", common.getUsername(), userInfo);
                }

            }

            if (this.message.hasPhoto()) {
            }
        }

        // // deal message channel chat
        if (update.getChannelPost() != null) {
            if (update.getChannelPost().hasText()) {
                String chatType = update.getChannelPost().getChat().getType();
                if (chatTypeIsChannel(chatType)) {
                    new ChannelMessage(this.common).handler();
                    log.info("[{}] Channel message received from {}",common.getUsername(), update.getChannelPost().getAuthorSignature());
                }
            }
        }

        if (update.hasCallbackQuery()) {
            new CallbackQuerys(this.common).handler();
            User user = update.getCallbackQuery().getFrom();
            String userInfo = String.format("[%s] %s (%s %s)", user.getId(), user.getUserName(), user.getFirstName(),
                    user.getLastName());
            String data = update.getCallbackQuery().getData();
            log.info("CallbackQuery Data received from {}: {}", userInfo, data);
        }
    }

    @Override
    public String getBotToken() {
        return this.dto.getToken();
    }

    @Override
    public String getBotUsername() {
        return this.dto.getUsername();
    }

    private void init(Update update) {
        this.common.setUpdate(update);
        this.message = update.getMessage();
    }
    private Boolean chatTypeIsChannel(String type) {
        return type.equals(SpringyBotEnum.CHAT_TYPE_CHANNEL.getText()) ? true : false;
    }
}
