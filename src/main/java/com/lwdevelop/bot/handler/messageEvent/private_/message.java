package com.lwdevelop.bot.handler.messageEvent.private_;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import com.lwdevelop.bot.handler.messageEvent.private_.commands.Job;
import com.lwdevelop.bot.handler.messageEvent.private_.commands.Manage;
import com.lwdevelop.bot.utils.Common;
import com.lwdevelop.bot.utils.KeyboardButton;
import com.lwdevelop.bot.utils.SpringyBotEnum;

public class message {

    private Common common;
    private Message message;
    private String text;
    private SendMessage response;

    public void handler(Common common) {
        this.init(common);

        switch (this.text) {
            case "/start2":
                this.setResponse_start();
                break;
            case "/manage":
                this.setResponse_manage();
                break;
            case "管理面板":
                break;
            case "支援团队列表":
                break;
            case "如何将我添加到您的群组":
                new Manage().setResponse_addToGroupOrChannel(common, SpringyBotEnum.CHAT_TYPE_GROUP.getText());
                break;
            case "如何将我添加到您的频道":
                new Manage().setResponse_addToGroupOrChannel(common, SpringyBotEnum.CHAT_TYPE_CHANNEL.getText());
                break;

            case "/start":
                this.setResponse_job();
                break;
            case "发布求职":
                if (hasUsername()) {
                    new Job().postAJobSearch(common);
                } else {
                    this.send_nullUsername();
                }
                break;
            case "发布招聘":
                if (hasUsername()) {
                    new Job().postRecruitment(common);
                } else {
                    this.send_nullUsername();
                }
                break;
            case "招聘和求职信息管理":
                new Job().setResponse_jobSeeker_management(common);
                new Job().setResponse_jobPosting_management(common);
                break;

            default:
                this.text = "";
                break;
        }
    }

    private void init(Common common) {
        this.message = common.getUpdate().getMessage();
        this.common = common;
        this.text = this.message.getText();
        this.privateMessageSettings(this.message);
    }

    private void setResponse_start() {
        String text = "欢迎使用我们的机器人！\n我们的机器人可以帮助您快速找到合适的工作或人才。\n\n以下是一些您可以使用的指令：\n" +
                "/job - 查看最新的招聘和求职信息。\n" +
                "/post - 发布您的招聘或求职信息。\n" +
                "/help - 查看机器人的使用指南。\n\n" +
                "我们希望这个机器人能为您提供帮助，如果您有任何问题或建议，请随时联系我们。谢谢！";
        this.response.setText(text);
        this.common.sendResponseAsync(this.response);
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

    private Boolean hasUsername() {
        if (this.common.getUpdate().getMessage().getChat().getUserName() == null) {
            return false;
        }
        return true;
    }

    private void send_nullUsername() {
        this.response.setText("请设置Telegram username");
        this.common.sendResponseAsync(this.response);
    }

}
