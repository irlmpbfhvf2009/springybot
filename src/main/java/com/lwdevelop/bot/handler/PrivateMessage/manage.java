package com.lwdevelop.bot.handler.PrivateMessage;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import com.lwdevelop.bot.utils.Common;
import com.lwdevelop.bot.utils.KeyboardButton;
import com.lwdevelop.bot.utils.SpringyBotEnum;

public class Manage{
    
    private Common common;
    private Message message;
    private String text;
    private SendMessage response;
    private String username;


    public void handler(Common common) {
        this.init(common);

        switch (text) {
            case "管理面板":
                break;

            case "支援团队列表":
                break;

            case "如何将我添加到您的群组":
                setResponse_addToGroupOrChannel(SpringyBotEnum.CHAT_TYPE_GROUP.getText());
                break;

            case "如何将我添加到您的频道":
                setResponse_addToGroupOrChannel(SpringyBotEnum.CHAT_TYPE_CHANNEL.getText());
                break;
            default:
                this.text="";
                break;
        }
    }
    private void init(Common common){
        this.message = common.getUpdate().getMessage();
        this.common = common;
        this.common.privateMessageSettings(this.message);
        this.text = message.getText();
        this.response = common.getResponse();
        this.username = common.getUsername();
    }


    private void setResponse_addToGroupOrChannel(String type) {
        this.response
                .setText(howToAddForText(this.username, getUrl(type, this.username), type));
        this.response.setReplyMarkup(
                new KeyboardButton().addToGroupOrChannelMarkupInline(getUrl(type, this.username), type));
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
