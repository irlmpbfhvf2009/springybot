package com.lwdevelop.botfactory.bot.telent.handler;

import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import com.lwdevelop.botfactory.Common;
import com.lwdevelop.botfactory.bot.telent.handler.commands.Job;
import com.lwdevelop.botfactory.bot.telent.utils.KeyboardButton;
import com.lwdevelop.dto.JobPostingDTO;
import com.lwdevelop.dto.JobSeekerDTO;
import com.lwdevelop.botfactory.bot.telent.utils.SpringyBotEnum;

public class PrivateMessage_ {

    private Common common;
    private Message message;
    private String text;
    private Job job;
    private Boolean isSubscribeChannel;

    public PrivateMessage_(Common common) {
        this.message = common.getUpdate().getMessage();
        this.common = common;
        this.text = this.message.getText();
        this.job = new Job(new JobPostingDTO(common), new JobSeekerDTO(common));
        this.job.saveJobUser(common);
        this.isSubscribeChannel = isSubscribeChannel();
    }

    public void handler() {
        // if (this.text.equals("/start")) {
        // this.setResponse_job();
        // }

        if (this.isSubscribeChannel) {
            switch (this.text.toLowerCase()) {
                case "发布招聘":
                    this.job.setResponse_jobPosting_management(common);
                    break;
                case "发布求职":
                    this.job.setResponse_jobSeeker_management(common);
                    break;
                case "招聘和求职信息管理":
                    this.job.setResponse_edit_jobPosting_management(common);
                    this.job.setResponse_edit_jobSeeker_management(common);
                    break;
                default:
                    break;
            }
            // 判斷事件
            if (text.length() > 4) {
                String post = text.substring(0, 4);
                // 發布招聘
                if (post.equals(SpringyBotEnum.RECRUITMENT.getText())) {
                    this.job.generateTextJobPosting(common, false);
                } else if (post.equals(SpringyBotEnum.EDIT_RECRUITMENT.getText())) {
                    this.job.generateTextJobPosting(common, true);
                // 發布求職
                } else if (post.equals(SpringyBotEnum.JOBSEARCH.getText())) {
                    this.job.generateTextJobSeeker(common, false);
                } else if (post.equals(SpringyBotEnum.EDIT_JOBSEARCH.getText())) {
                    this.job.generateTextJobSeeker(common, true);
                }
            }
        } else {
            this.setResponse_job();
        }
    }

    private void setResponse_job() {

        String firstName = this.message.getFrom().getFirstName() == null ? "" : this.message.getFrom().getFirstName();
        String lastName = this.message.getFrom().getLastName() == null ? "" : this.message.getFrom().getLastName();
        String name = firstName + lastName;
        String botName;

        try {
            botName = "@" + this.common.getBot().getMe().getUserName();
        } catch (TelegramApiException e) {
            botName = "";
            e.printStackTrace();
        }

        SendMessage response = new SendMessage();
        response.setChatId(String.valueOf(message.getChatId()));
        response.setText(SpringyBotEnum.help_text(name, botName));

        if (this.isSubscribeChannel) {
            response.setReplyMarkup(new KeyboardButton().jobReplyKeyboardMarkup());
        } else {
            response.setReplyMarkup(new KeyboardButton().keyboardSubscribeChannelMarkup());
        }
        this.common.executeAsync(response);
    }

    private Boolean isSubscribeChannel() {
        // -1001784108917 缅甸招聘频道
        // -1001989448617 测试频道
        GetChatMember getChatMember = new GetChatMember("-1001784108917", message.getChatId());
        Boolean status = this.common.executeAsync(getChatMember);
        return status;
    }

}
