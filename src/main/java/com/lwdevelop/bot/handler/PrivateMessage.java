package com.lwdevelop.bot.handler;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import com.lwdevelop.bot.utils.Common;
import com.lwdevelop.bot.utils.KeyboardButton;
import com.lwdevelop.bot.utils.SpringyBotEnum;

public class PrivateMessage {
    private Common common;
    private String text = common.getMessage().getText();
    private SendMessage response = common.getResponse();
    private String username = common.getUsername();

    public void handler(Common common) {
        this.common = common;
        common.privateMessageSettings(common.getMessage());

        switch (text) {
            case "管理面板":
                break;

            case "支援团队列表":
                break;

            case "管理员设置":
                break;

            case "/start":
                break;

            case "/manage":
                setResponse_manage();
                break;

            case "如何将我添加到您的群组":
                setResponse_addToGroupOrChannel(SpringyBotEnum.CHAT_TYPE_GROUP.getText());
                break;

            case "如何将我添加到您的频道":
                setResponse_addToGroupOrChannel(SpringyBotEnum.CHAT_TYPE_CHANNEL.getText());
                break;
            default:
                response.setText("");
                break;
        }
    }

    private void setResponse_manage() {
        this.response.setText(SpringyBotEnum.COMMONS_MANAGE.getText());
        this.response.setReplyMarkup(new KeyboardButton().manageReplyKeyboardMarkup());
    }

    private void setResponse_addToGroupOrChannel(String type) {
        this.response
                .setText(this.common.howToAddForText(this.username, this.common.getUrl(type, this.username), type));
        this.response.setReplyMarkup(
                new KeyboardButton().addToGroupOrChannelMarkupInline(this.common.getUrl(type, this.username), type));
    }

}
