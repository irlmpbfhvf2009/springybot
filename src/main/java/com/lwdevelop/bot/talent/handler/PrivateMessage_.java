package com.lwdevelop.bot.talent.handler;

import com.lwdevelop.service.impl.RobotGroupAndChannelManagementServiceImpl;
import com.lwdevelop.utils.SpringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.lwdevelop.bot.talent.handler.commands.Job;
import com.lwdevelop.bot.talent.utils.Common;
import com.lwdevelop.bot.talent.utils.KeyboardButton;
import com.lwdevelop.bot.talent.utils.SpringyBotEnum;
import com.lwdevelop.dto.JobPostingDTO;
import com.lwdevelop.dto.JobSeekerDTO;

public class PrivateMessage_ {

    private Common common;
    private Message message;
    private String text;
    private SendMessage response;
    private Job job;

    @Autowired
    private RobotGroupAndChannelManagementServiceImpl robotGroupAndChannelManagementService = SpringUtils
            .getApplicationContext()
            .getBean(RobotGroupAndChannelManagementServiceImpl.class);

    public PrivateMessage_(Common common){
        this.message = common.getUpdate().getMessage();
        this.common = common;
        this.text = this.message.getText();
        this.response = new SendMessage();
        this.response.setChatId(String.valueOf(message.getChatId()));
        this.response.setDisableNotification(false);
        this.response.setDisableWebPagePreview(false);
        this.job = new Job(new JobPostingDTO(common), new JobSeekerDTO(common));
    }

    public void handler() {
        boolean ifSubscribeChannel = robotGroupAndChannelManagementService.ifSubscribeChannel(common);
        // 判斷是否有關注頻道
        if (ifSubscribeChannel) {
            // 判斷事件
            if (text.length() >= 4) {
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

            switch (this.text.toLowerCase()) {
                case "/start":
                    this.setResponse_job();
                    break;

                case "发布招聘":
                    if (hasUsername()) {
                        this.job.setResponse_jobPosting_management(common);
                    } else {
                        this.send_nullUsername();
                    }
                    break;

                case "发布求职":
                    if (hasUsername()) {
                        this.job.setResponse_jobSeeker_management(common);
                    } else {
                        this.send_nullUsername();
                    }
                    break;

                case "招聘和求职信息管理":
                    this.job.setResponse_edit_jobPosting_management(common);
                    this.job.setResponse_edit_jobSeeker_management(common);
                    break;

                default:
                    this.text = "";
                    break;
            }
        } else {
            SendMessage s = new SendMessage();
            s.setChatId(common.getUpdate().getMessage().getChatId().toString());
            s.setText(SpringyBotEnum.subscribeChannel_text());
            common.executeAsync(s);
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

        this.response.setText(SpringyBotEnum.help_text(name, botName));
        this.response.setReplyMarkup(new KeyboardButton().jobReplyKeyboardMarkup());
        this.common.executeAsync(this.response);
    }


    private Boolean hasUsername() {
        if (this.common.getUpdate().getMessage().getChat().getUserName() == null) {
            return false;
        }
        return true;
    }

    private void send_nullUsername() {
        this.response.setText(SpringyBotEnum.PLEASE_SET_TELEGRAM_USERNAME.getText());
        this.common.executeAsync(this.response);
    }

}
