package com.lwdevelop.botfactory.bot.telent.handler.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import com.lwdevelop.botfactory.Common;
import com.lwdevelop.botfactory.bot.telent.utils.TelentButton;
import com.lwdevelop.botfactory.bot.telent.utils.TelentEnum;

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
        this.response.setReplyMarkup(new TelentButton().addToGroupOrChannelMarkupInline(getUrl(type, common.getUsername()), type));
        common.executeAsync(this.response);
    }

    public String howToAddForText(String username, String url, String type) {
        return "Tap on this link and then choose your " + type + ".\n" + url
                + "\n\n\"Add admins\" permission is required.";
    }

    public String getUrl(String type, String username) {
        return type == TelentEnum.CHAT_TYPE_GROUP.getText()
                ? "http://t.me/" + username + "?startgroup&admin=change_info"
                : "http://t.me/" + username + "?startchannel&admin=change_info";
    }

    
}
