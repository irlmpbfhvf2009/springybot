package com.lwdevelop.bot.bots.talent.messageHandling;

import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import com.lwdevelop.bot.bots.talent.messageHandling.commands.Job;
import com.lwdevelop.bot.bots.utils.Common;
import com.lwdevelop.bot.bots.utils.enum_.TelentEnum;
import com.lwdevelop.bot.bots.utils.keyboardButton.TelentButton;
import com.lwdevelop.bot.chatMessageHandlers.BasePrivateMessage;
import com.lwdevelop.dto.JobPostingDTO;
import com.lwdevelop.dto.JobSeekerDTO;
import com.lwdevelop.entity.SpringyBot;

public class PrivateMessage_ extends BasePrivateMessage{

    private Job job;

    public PrivateMessage_(Common common) {
        super(common);
        Job job = new Job(new JobPostingDTO(common), new JobSeekerDTO(common));
        job.saveJobUser(common);
        this.job = job;
    }

    @Override
    public void handler() {

        Boolean isSubscribeChannel = isSubscribeChannel();

        if (isSubscribeChannel) {
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
        Boolean isSubscribeChannel = isSubscribeChannel();

        try {
            botName = "@" + this.common.getBot().getMe().getUserName();
        } catch (TelegramApiException e) {
            botName = "";
            e.printStackTrace();
        }

        SendMessage response = new SendMessage();
        response.setChatId(String.valueOf(message.getChatId()));
        response.setText(TelentEnum.help_text(name, botName));

        if (isSubscribeChannel) {
            response.setReplyMarkup(new TelentButton().jobReplyKeyboardMarkup());
        } else {
            response.setReplyMarkup(new TelentButton().keyboardSubscribeChannelMarkup());
        }
        this.common.executeAsync(response);
    }

    private Boolean isSubscribeChannel() {
        SpringyBot springyBot = springyBotServiceImpl.findById(springyBotId).get();
        Long chatId = springyBot.getConfig().getFollowChannelSet_chatId();
        GetChatMember getChatMember = new GetChatMember(chatId.toString(), message.getChatId());
        Boolean status = this.common.executeAsync(getChatMember);
        return status;
    }

}
