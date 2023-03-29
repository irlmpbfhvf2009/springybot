package com.lwdevelop.bot.handler.Commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import com.lwdevelop.bot.utils.Common;
import com.lwdevelop.bot.utils.KeyboardButton;

public class Job {
    SendMessage response;

    private void jobMessageSetting(Message message) {
        this.response = new SendMessage();
        this.response.setChatId(String.valueOf(message.getChatId()));
        this.response.setDisableNotification(false);
        this.response.setDisableWebPagePreview(false);
    }

    public void setResponse_jobSeeker_management(Common common) {
        this.jobMessageSetting(common.getUpdate().getMessage());

        String userId = String.valueOf(common.getUpdate().getMessage().getChatId());
        String firstname = common.getUpdate().getMessage().getChat().getFirstName();
        String username = common.getUpdate().getMessage().getChat().getUserName();
        String lastname = common.getUpdate().getMessage().getChat().getLastName();
        this.response.setText("求职人员\n" +
                "姓名：\n" +
                "男女：\n" +
                "出生_年_月_日\n" +
                "年龄：\n" +
                "国籍：\n" +
                "学历：\n" +
                "技能：\n" +
                "目标职位：\n" +
                "手上有什么资源：\n" +
                "期望薪资：\n" +
                "工作经历:(限50字以内)\n\n" +
                "自我介绍:(限50字以内)");
        this.response
                .setReplyMarkup(new KeyboardButton().jobFormManagement(userId, firstname, username, lastname));
        common.sendResponseAsync(this.response);

    }

    public void setResponse_jobPosting_management(Common common) {
        this.jobMessageSetting(common.getUpdate().getMessage());
        String userId = String.valueOf(common.getUpdate().getMessage().getChatId());
        String firstname = common.getUpdate().getMessage().getChat().getFirstName();
        String username = common.getUpdate().getMessage().getChat().getUserName();
        String lastname = common.getUpdate().getMessage().getChat().getLastName();

        this.response.setText("招聘人才\n" +
                "公司：\n" +
                "职位：\n" +
                "底薪：\n" +
                "提成：\n" +
                "上班时间：\n" +
                "要求内容：（限50字以内）\n" +
                "🐌 地址：\n" +
                "✈️咨询飞机号");
        this.response.setReplyMarkup(new KeyboardButton().jobFormManagement(userId, firstname, username, lastname));
        common.sendResponseAsync(this.response);
    }

}
