package com.lwdevelop.bot.coolbao;

import java.util.HashMap;

import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.lwdevelop.bot.coolbao.handler.CallbackQuerys;
import com.lwdevelop.bot.coolbao.handler.messageEvent.private_.*;
import com.lwdevelop.bot.coolbao.utils.Common;
import com.lwdevelop.dto.SpringyBotDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class coolbao_bot extends TelegramLongPollingBot {

    public Common common;
    private SpringyBotDTO dto;
    private Message message;

    public coolbao_bot(SpringyBotDTO springyBotDTO) {
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

    @Override
    public String getBotToken() {
        return this.dto.getToken();
    }

    @Override
    public String getBotUsername() {
        return this.dto.getUsername();
    }

    @Override
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
                    new message().handler(this.common);
                    log.info("[{}] Private message received from {}: {}", this.common.getUsername(), userInfo,
                            this.message.getText());

                }

            }
            
            if(this.message.hasPhoto()){
            }
        }
        
        if (update.hasCallbackQuery()) {
            new CallbackQuerys().handler(common);
            User user = update.getCallbackQuery().getFrom();
            String userInfo = String.format("[%s] %s (%s %s)", user.getId(), user.getUserName(), user.getFirstName(), user.getLastName());
            String data = update.getCallbackQuery().getData();
            log.info("CallbackQuery Data received from {}: {}", userInfo, data);
        }
    }

    private void init(Update update) {
        this.common.setUpdate(update);
        this.message = update.getMessage();
    }

}
