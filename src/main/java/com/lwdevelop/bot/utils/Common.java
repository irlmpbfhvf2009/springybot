package com.lwdevelop.bot.utils;

import org.springframework.scheduling.annotation.Async;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import lombok.Data;
import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;

@Data
public class Common {

    private Long springyBotId;

    private Long botId;

    private String username;

    private Update update;

    private String inviteLink;

    private TelegramLongPollingBot bot;

    private DefaultBotOptions defaultBotOptions;

    // 用来存储用户的状态(会话)
    // private HashMap<Long, String> userState;

    public Common(Long springyBotId, Long botId, String username) {
        this.springyBotId = springyBotId;
        this.botId = botId;
        this.username = username;
    }


    @Async
    @SneakyThrows
    public Integer sendResponseAsync(SendMessage response) {
        return this.bot.executeAsync(response).get().getMessageId();
    }

    public void sendMsg(String id,String text , String path) throws TelegramApiException {
        if (path != null){
            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setPhoto(new InputFile(new File(path)));
            sendPhoto.setCaption(text);
            sendPhoto.setChatId(id);
            this.bot.executeAsync(sendPhoto);
        }else {
            SendMessage message = new SendMessage();
            message.setText(text);
            message.setChatId(id);
        this.bot.executeAsync(message);
        }

    }


}
