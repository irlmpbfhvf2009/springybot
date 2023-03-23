package com.lwdevelop.bot.handler;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import com.lwdevelop.bot.utils.CommonUtils;
import com.lwdevelop.bot.utils.KeyboardButton;
import com.lwdevelop.bot.utils.SpringyBotEnum;


public class PrivateMessage {

    public String handler(CommonUtils common,Message message,SendMessage response,String username){
        String type;
        String text = message.getText();
        switch (text) {
            case "管理面板":
                break;

            case "支援团队列表":
                break;
                
                case "管理员设置":
                break;

            case "/start":
                text = SpringyBotEnum.CALLBACKS.getText();
                response.setReplyMarkup(new KeyboardButton().startReplyKeyboardMarkup());
                break;

            case "如何将我添加到您的群组":
                type = SpringyBotEnum.CHAT_TYPE_GROUP.getText();
                text = common.howToAddForText(username,common.getUrl(type,username),type);
                response.setReplyMarkup(new KeyboardButton().addToGroupOrChannelMarkupInline(common.getUrl(type,username),type));
                break;

            case "如何将我添加到您的频道":
                type = SpringyBotEnum.CHAT_TYPE_CHANNEL.getText();
                text = common.howToAddForText(username,common.getUrl(type,username),type);
                response.setReplyMarkup(new KeyboardButton().addToGroupOrChannelMarkupInline(common.getUrl(type,username),type));
                break;
            default:
                return null;
                
        }
        return text;
    }

}
