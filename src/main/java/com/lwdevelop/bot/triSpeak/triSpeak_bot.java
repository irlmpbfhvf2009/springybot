package com.lwdevelop.bot.triSpeak;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    private ExecutorService executor;
    
    public triSpeak_bot(SpringyBotDTO springyBotDTO) {
        super(new DefaultBotOptions());
        this.dto = springyBotDTO;
        executor = Executors.newFixedThreadPool(10);
        try {
            this.common = new Common(dto.getId(), getMe().getId(), getMe().getUserName());
            this.common.setBot(this);
            this.common.setUserState(new HashMap<>());
            this.common.setConfigDTO_map(new HashMap<>());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    public void onUpdateReceived(Update update) {

        this.common.setUpdate(update);
        Message message = update.getMessage();

        // deal message group or private chat
        if (update.hasMessage()) {

            if (message.hasText()) {
                User user = message.getFrom();
                String userInfo = String.format("[%s] @%s (%s %s)", user.getId(), user.getUserName(),
                        user.getFirstName(), user.getLastName());

                // private
                if (message.isUserMessage()) {
                    new PrivateMessage_(this.common).handler();
                    log.info("[{}] Private message received from {}: {}", this.common.getUsername(), userInfo,
                            message.getText());

                }
                // group
                if (message.isSuperGroupMessage()) {
                    executor.execute(() -> new GroupMessage(this.common).handler());
                // log.info("[{}] Group message received from {}", common.getUsername(),
                // userInfo);
                }

            }

            if (message.hasPhoto()) {
            }
        }

        // // deal message channel chat
        if (update.getChannelPost() != null) {
            if (update.getChannelPost().hasText()) {
                String chatType = update.getChannelPost().getChat().getType();
                if (chatTypeIsChannel(chatType)) {
                    new ChannelMessage(this.common).handler();
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

    private Boolean chatTypeIsChannel(String type) {
        return type.equals(SpringyBotEnum.CHAT_TYPE_CHANNEL.getText()) ? true : false;
    }
}
