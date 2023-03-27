package com.lwdevelop.bot.utils;

import java.util.HashMap;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import lombok.Data;

@Data
public class Common {

    private Long springyBotId;

    private Long botId;

    private String username;

    private Update update;

    private String inviteLink;

    private SendMessage response;

    // 用来存储用户的状态(会话)
    private HashMap<Long, String> userState;

    public Common(Long springyBotId, Long botId, String username) {
        this.springyBotId = springyBotId;
        this.botId = botId;
        this.username = username;
    }

    // Message Setting
    public void privateMessageSettings(Message message) {
        String chatId = String.valueOf(message.getChatId());
        this.response = new SendMessage();
        this.response.setChatId(chatId);
        this.response.setDisableNotification(false);
        this.response.setDisableWebPagePreview(false);
    }

    public void notEnoughRightsMessageSettings(Message message) {
        String chatId = String.valueOf(message.getFrom().getId());
        String title = message.getChat().getTitle();
        this.response = new SendMessage();
        this.response.setChatId(chatId);
        this.response.setDisableNotification(false);
        this.response.setText(title + SpringyBotEnum.BOT_NOT_ENOUGH_RIGHTS.getText());
    }


}
