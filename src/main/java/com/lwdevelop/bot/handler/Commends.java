package com.lwdevelop.bot.handler;

import java.util.Arrays;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import com.lwdevelop.bot.handler.PrivateMessage.Job;
import com.lwdevelop.bot.handler.PrivateMessage.Manage;
import com.lwdevelop.bot.utils.Common;
import com.lwdevelop.bot.utils.KeyboardButton;
import com.lwdevelop.bot.utils.SpringyBotEnum;

public class Commends {

    private Common common;
    private Message message;
    private String text;
    private SendMessage response;
    private Long chatId;
    private String[] commands;

    public void handler(Common common) {

        this.init(common);

        if (this.isCommand()) {
            switch (this.text) {
                case "/manage":
                    this.setResponse_manage();
                    break;
                case "/job":
                    this.setResponse_job();
                    break;
                default:
                    this.text = "";
                    break;
            }
            this.setUserState(this.text);
        } else {
            switch (getUserState()) {
                case "/manage":
                    new Manage().handler(common);
                    break;
                case "/job":
                    new Job().handler(common);
                    break;
                default:
                    this.setUserState("");
                    break;
            }
        }
    }

    private void init(Common common) {
        this.message = common.getUpdate().getMessage();
        this.common = common;
        this.common.privateMessageSettings(this.message);
        this.text = message.getText();
        this.response = common.getResponse();
        this.chatId = common.getUpdate().getMessage().getChatId();
        this.commands = new String[] { "/job", "/manage" };
    }

    private Boolean isCommand() {
        return Arrays.asList(this.commands).contains(this.text);
    }

    private String getUserState() {
        return this.common.getUserState().getOrDefault(this.chatId, "");
    }

    private void setUserState(String text) {
        this.common.getUserState().put(this.chatId, text);
    }

    private void setResponse_job() {
        this.response.setText(SpringyBotEnum.COMMEND_JOB.getText());
        this.response.setReplyMarkup(new KeyboardButton().jobReplyKeyboardMarkup());
    }

    private void setResponse_manage() {
        this.response.setText(SpringyBotEnum.COMMEND_MANAGE.getText());
        this.response.setReplyMarkup(new KeyboardButton().manageReplyKeyboardMarkup());
    }

}
