package com.lwdevelop.bot.bots.talent.messageHandling;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.lwdevelop.bot.bots.talent.messageHandling.commands.Job;
import com.lwdevelop.bot.bots.utils.Common;
import com.lwdevelop.bot.bots.utils.enum_.TelentEnum;
import com.lwdevelop.bot.bots.utils.keyboardButton.TelentButton;
import com.lwdevelop.dto.JobPostingDTO;
import com.lwdevelop.dto.JobSeekerDTO;
import com.lwdevelop.service.impl.SpringyBotServiceImpl;
import com.lwdevelop.utils.SpringUtils;

public class PrivateMessage_ {

    private Common common;
    private Message message;
    private String text;
    private Job job;
    
    private Boolean isSubscribeChannel;
    @Autowired
    private SpringyBotServiceImpl springyBotServiceImpl = SpringUtils.getApplicationContext()
            .getBean(SpringyBotServiceImpl.class);
    public PrivateMessage_(Common common) {
        System.out.println("test3");
        this.message = common.getUpdate().getMessage();
        this.common = common;
        this.text = common.getUpdate().getMessage().getText();
        this.job = new Job(new JobPostingDTO(common), new JobSeekerDTO(common));
        this.job.saveJobUser(common);
        this.isSubscribeChannel = isSubscribeChannel();
    }

    public void handler() {
                System.out.println("test4");

        if (this.text.equals("/start")) {
            this.setResponse_job();
        }

        // if (true) {
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
                case "/start":
                    this.setResponse_job();
                default:
                    break;
            }
            // 判斷事件
            if (text.length() > 4) {
                String post = text.substring(0, 4);
                // 發布招聘
                if (post.equals(TelentEnum.RECRUITMENT.getText())) {
                    this.job.generateTextJobPosting(common, false);
                } else if (post.equals(TelentEnum.EDIT_RECRUITMENT.getText())) {
                    this.job.generateTextJobPosting(common, true);
                    // 發布求職
                } else if (post.equals(TelentEnum.JOBSEARCH.getText())) {
                    this.job.generateTextJobSeeker(common, false);
                } else if (post.equals(TelentEnum.EDIT_JOBSEARCH.getText())) {
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
        response.setText(TelentEnum.help_text(name, botName));

        if (this.isSubscribeChannel) {
            response.setReplyMarkup(new TelentButton().jobReplyKeyboardMarkup());
        } else {
            response.setReplyMarkup(new TelentButton().keyboardSubscribeChannelMarkup());
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
