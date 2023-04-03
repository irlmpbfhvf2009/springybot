package com.lwdevelop.bot.handler.messageEvent.private_.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import com.lwdevelop.bot.utils.Common;
import com.lwdevelop.bot.utils.KeyboardButton;
import com.lwdevelop.bot.utils.SpringyBotEnum;

public class Manage{
    SendMessage response;

    private void manageMessageSetting(Message message) {
        this.response = new SendMessage();
        this.response.setChatId(String.valueOf(message.getChatId()));
        this.response.setDisableNotification(false);
        this.response.setDisableWebPagePreview(false);
    }

    
    public void setResponse_addToGroupOrChannel(Common common,String type) {
        this.manageMessageSetting(common.getUpdate().getMessage());
        this.response.setText(howToAddForText(common.getUsername(), getUrl(type, common.getUsername()), type));
        this.response.setReplyMarkup(new KeyboardButton().addToGroupOrChannelMarkupInline(getUrl(type, common.getUsername()), type));
        common.sendResponseAsync(this.response);
    }

    public String howToAddForText(String username, String url, String type) {
        return "Tap on this link and then choose your " + type + ".\n" + url
                + "\n\n\"Add admins\" permission is required.";
    }

    public String getUrl(String type, String username) {
        return type == SpringyBotEnum.CHAT_TYPE_GROUP.getText()
                ? "http://t.me/" + username + "?startgroup&admin=change_info"
                : "http://t.me/" + username + "?startchannel&admin=change_info";
    }

    
}
