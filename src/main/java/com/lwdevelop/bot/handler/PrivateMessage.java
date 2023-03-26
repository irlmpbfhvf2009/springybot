package com.lwdevelop.bot.handler;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import com.lwdevelop.bot.utils.Common;
import com.lwdevelop.bot.utils.KeyboardButton;
import com.lwdevelop.bot.utils.SpringyBotEnum;

public class PrivateMessage {

    public void handler(Common common) {

        common.privateMessageSettings(common.getMessage());
        
        String type;
        String text = common.getMessage().getText();
        SendMessage response = common.getResponse();
        String username = common.getUsername();

        switch (text) {
            case "管理面板":
                break;

            case "支援团队列表":
                break;

            case "管理员设置":
                break;

            case "/start":
                response.setText(SpringyBotEnum.COMMONS_START.getText());
                response.setReplyMarkup(new KeyboardButton().startReplyKeyboardMarkup());
                break;

            case "如何将我添加到您的群组":
                type = SpringyBotEnum.CHAT_TYPE_GROUP.getText();
                response.setText(common.howToAddForText(username, common.getUrl(type, username), type));
                response.setReplyMarkup(new KeyboardButton().addToGroupOrChannelMarkupInline(common.getUrl(type, username), type));
                break;

            case "如何将我添加到您的频道":
                type = SpringyBotEnum.CHAT_TYPE_CHANNEL.getText();
                response.setText(common.howToAddForText(username, common.getUrl(type, username),type));
                response.setReplyMarkup(new KeyboardButton().addToGroupOrChannelMarkupInline(common.getUrl(type, username), type));
                break;
            default:
                response.setText("");

        }
    }


}
