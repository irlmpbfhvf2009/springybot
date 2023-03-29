package com.lwdevelop.bot.handler;


import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import com.lwdevelop.bot.handler.commands.Job;
import com.lwdevelop.bot.handler.commands.Manage;
import com.lwdevelop.bot.utils.Common;
import com.lwdevelop.bot.utils.KeyboardButton;
import com.lwdevelop.bot.utils.SpringyBotEnum;

public class PrivateMessage {

    private Common common;
    private Message message;
    private String text;
    private SendMessage response;
    private Long chatId;

    public void handler(Common common) {
        this.init(common);


        switch (this.text) {
            case "/manage":
                this.setResponse_manage();
                break;
            case "管理面板":
                break;
            case "支援团队列表":
                break;
            case "如何将我添加到您的群组":
                new Manage().setResponse_addToGroupOrChannel(common,SpringyBotEnum.CHAT_TYPE_GROUP.getText());
                break;
            case "如何将我添加到您的频道":
                new Manage().setResponse_addToGroupOrChannel(common,SpringyBotEnum.CHAT_TYPE_CHANNEL.getText());
                break;

            case "/job":
                this.setResponse_job();
                break;
            case "发布招聘":
                break;
            case "发布求职":
                break;
            case "招聘和求职信息管理":
                new Job().setResponse_jobSeeker_management(common);
                new Job().setResponse_jobPosting_management(common);
                break;
            
            default:
                this.text = "";
                break;
        }
        this.setUserState(this.text);
    }

    private void init(Common common) {
        this.message = common.getUpdate().getMessage();
        this.common = common;
        this.text = this.message.getText();
        this.chatId = common.getUpdate().getMessage().getChatId();
        this.privateMessageSettings(this.message);
    }

    // private String getUserState() {
    //     return this.common.getUserState().getOrDefault(this.chatId, "");
    // }

    private void setUserState(String text) {
        this.common.getUserState().put(this.chatId, text);
    }

    private void setResponse_job() {
        this.response.setText(SpringyBotEnum.COMMEND_JOB.getText());
        this.response.setReplyMarkup(new KeyboardButton().jobReplyKeyboardMarkup());
        this.common.sendResponseAsync(this.response);
    }

    private void setResponse_manage() {
        this.response.setText(SpringyBotEnum.COMMEND_MANAGE.getText());
        this.response.setReplyMarkup(new KeyboardButton().manageReplyKeyboardMarkup());
        this.common.sendResponseAsync(this.response);
    }

    public void privateMessageSettings(Message message) {
        String chatId = String.valueOf(message.getChatId());
        this.response = new SendMessage();
        this.response.setChatId(chatId);
        this.response.setDisableNotification(false);
        this.response.setDisableWebPagePreview(false);
    }


}
