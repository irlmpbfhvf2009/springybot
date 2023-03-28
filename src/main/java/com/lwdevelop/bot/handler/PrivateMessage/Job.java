package com.lwdevelop.bot.handler.PrivateMessage;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import com.lwdevelop.bot.utils.Common;
import com.lwdevelop.bot.utils.KeyboardButton;

public class Job {

    private Common common;
    private Message message;
    private String text;
    private SendMessage response;
    private String username;

    public void handler(Common common) {
        this.init(common);
        switch (this.text) {
            case "发布招聘":
                break;
            case "发布求职":
                break;
            case "招聘和求职信息管理":
                this.setResponse_job_management();
                break;
            default:
                this.text = "";
                break;
        }

    }

    private void init(Common common) {
        this.message = common.getUpdate().getMessage();
        this.common = common;
        this.common.privateMessageSettings(this.message);
        this.text = message.getText();
        this.response = common.getResponse();
        this.username = common.getUsername();
    }

    private void setResponse_job_management() {
        Long userId = this.message.getChatId();
        String username = this.message.getChat().getUserName(); 
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
        this.response.setReplyMarkup(new KeyboardButton().jobManagement(userId,username));
    }

}
