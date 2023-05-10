package com.lwdevelop.bot.talentBot.handler;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import com.lwdevelop.bot.talentBot.Custom;
import com.lwdevelop.bot.talentBot.utils.Common;
import com.lwdevelop.bot.talentBot.utils.KeyboardButton;
import com.lwdevelop.bot.talentBot.utils.SpringyBotEnum;
import com.lwdevelop.dto.JobPostingDTO;
import com.lwdevelop.dto.JobSeekerDTO;
import com.lwdevelop.dto.SpringyBotDTO;
import com.lwdevelop.entity.ChannelMessageIdPostCounts;
import com.lwdevelop.entity.GroupMessageIdPostCounts;
import com.lwdevelop.entity.JobPosting;
import com.lwdevelop.entity.JobSeeker;
import com.lwdevelop.entity.SpringyBot;
import com.lwdevelop.service.impl.JobManagementServiceImpl;
import com.lwdevelop.service.impl.SpringyBotServiceImpl;
import com.lwdevelop.utils.SpringUtils;

public class CallbackQuerys {

    @Autowired
    private JobManagementServiceImpl jobManagementServiceImpl = SpringUtils.getApplicationContext()
            .getBean(JobManagementServiceImpl.class);
    @Autowired
    private SpringyBotServiceImpl springyBotServiceImpl = SpringUtils.getApplicationContext()
            .getBean(SpringyBotServiceImpl.class);

    private SendMessage response;

    public void handler(Common common) {
        CallbackQuery callbackQuery = common.getUpdate().getCallbackQuery();

        this.messageSetting(common);

        if (callbackQuery.getData().startsWith("clearJobPosting_")) {

            String userId = callbackQuery.getData().substring("clearJobPosting_".length(),
                    callbackQuery.getData().lastIndexOf("_"));
            String botId = callbackQuery.getData().substring(callbackQuery.getData().lastIndexOf("_") + 1);

            // 在这里根据 springyBotId 和 userId 进行相应的清除操作
            JobPosting jobPosting =
            jobManagementServiceImpl.findByUserIdAndBotIdWithJobPosting(userId,botId);
            jobPosting.setBaseSalary("");
            jobPosting.setCommission("");
            jobPosting.setCompany("");
            jobPosting.setFlightNumber("");
            jobPosting.setLocation("");
            jobPosting.setPosition("");
            jobPosting.setRequirements("（限50字以内）");
            jobPosting.setWorkTime("");
            jobManagementServiceImpl.saveJobPosting(jobPosting);

            // // 清除訊息
            Long id = Long.valueOf(jobPosting.getBotId());
            SpringyBot springyBot = springyBotServiceImpl.findById(id).get();
            SpringyBotDTO springyBotDTO = new SpringyBotDTO();
            springyBotDTO.setToken(springyBot.getToken());
            springyBotDTO.setUsername(springyBot.getUsername());
            Custom custom = new Custom(springyBotDTO);

            JobPostingDTO jobPostingDTO = new JobPostingDTO(userId,
            jobPosting.getBotId(), jobPosting.getCompany(),
            jobPosting.getPosition(), jobPosting.getBaseSalary(),
            jobPosting.getCommission(),
            jobPosting.getWorkTime(), jobPosting.getRequirements(),
            jobPosting.getLocation(),
            jobPosting.getFlightNumber());

            Integer messageId = jobPosting.getLastMessageId();
            EditMessageText editMessageText = new EditMessageText();
            editMessageText.setChatId(userId);
            editMessageText.setMessageId(messageId);
            editMessageText.setText("招聘人才\n\n" +
            "公司：\n" +
            "职位：\n" +
            "底薪：\n" +
            "提成：\n" +
            "上班时间：\n" +
            "要求内容：（限50字以内）\n" +
            "🐌 地址：\n" +
            "✈️咨询飞机号：");

            editMessageText.setReplyMarkup(new
            KeyboardButton().keyboard_jobPosting(jobPostingDTO,true));
            try {
            custom.executeAsync(editMessageText);
            } catch (TelegramApiException e) {
            e.printStackTrace();
            }

            List<ChannelMessageIdPostCounts> channelMessageIdPostCounts = jobManagementServiceImpl
                    .findAllByBotIdAndUserIdAndTypeWithChannelMessageIdPostCounts(jobPosting.getBotId(), userId, SpringyBotEnum.JOBPOSTING.getText());
            List<GroupMessageIdPostCounts> groupMessageIdPostCounts = jobManagementServiceImpl
                    .findAllByBotIdAndUserIdAndTypeWithGroupMessageIdPostCounts(jobPosting.getBotId(), userId, SpringyBotEnum.JOBPOSTING.getText());

            channelMessageIdPostCounts.stream().forEach(cmp -> {
                DeleteMessage dm = new DeleteMessage();
                dm.setChatId(String.valueOf(cmp.getChannelId()));
                dm.setMessageId(cmp.getMessageId());
                try {
                    custom.executeAsync(dm);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                cmp.setMessageId(-1);
                cmp.setPostCount(0);
                jobManagementServiceImpl.saveChannelMessageIdPostCounts(cmp);
            });

            groupMessageIdPostCounts.stream().forEach(cmp -> {
                DeleteMessage dm = new DeleteMessage();
                dm.setChatId(String.valueOf(cmp.getGroupId()));
                dm.setMessageId(cmp.getMessageId());
                try {
                    custom.executeAsync(dm);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                cmp.setMessageId(-1);
                cmp.setPostCount(0);
                jobManagementServiceImpl.saveGroupMessageIdPostCounts(cmp);
            });

            this.response.setText(SpringyBotEnum.SUCCESSFULLYDELETED.getText());
            common.sendResponseAsync(this.response);
        } else if (callbackQuery.getData().startsWith("clearJobSeeker_")) {

            String userId = callbackQuery.getData().substring("clearJobSeeker_".length(),
                    callbackQuery.getData().lastIndexOf("_"));
            String botId = callbackQuery.getData().substring(callbackQuery.getData().lastIndexOf("_") + 1);
            // 在这里根据 springyBotId 和 userId 进行相应的清除操作
            JobSeeker jobSeeker = jobManagementServiceImpl.findByUserIdAndBotIdWithJobSeeker(userId, botId);
            jobSeeker.setName("");
            jobSeeker.setGender("");
            jobSeeker.setDateOfBirth("");
            jobSeeker.setAge("");
            jobSeeker.setNationality("");
            jobSeeker.setEducation("");
            jobSeeker.setSkills("");
            jobSeeker.setTargetPosition("");
            jobSeeker.setResources("");
            jobSeeker.setExpectedSalary("");
            jobSeeker.setWorkExperience("（限50字以内）");
            jobSeeker.setSelfIntroduction("（限50字以内）");
            jobSeeker.setFlightNumber("");
            jobManagementServiceImpl.saveJobSeeker(jobSeeker);

            // 清除訊息
            Long id = Long.valueOf(jobSeeker.getBotId());
            SpringyBot springyBot = springyBotServiceImpl.findById(id).get();
            SpringyBotDTO springyBotDTO = new SpringyBotDTO();
            springyBotDTO.setToken(springyBot.getToken());
            springyBotDTO.setUsername(springyBot.getUsername());
            Custom custom = new Custom(springyBotDTO);

            JobSeekerDTO jobSeekerDTO = new JobSeekerDTO(userId, jobSeeker.getBotId(), jobSeeker.getName(),
                    jobSeeker.getGender(), jobSeeker.getDateOfBirth(), jobSeeker.getAge(), jobSeeker.getNationality(),
                    jobSeeker.getEducation(), jobSeeker.getSkills(), jobSeeker.getTargetPosition(),
                    jobSeeker.getResources(), jobSeeker.getExpectedSalary(), jobSeeker.getWorkExperience(),
                    jobSeeker.getSelfIntroduction(), jobSeeker.getFlightNumber());

            Integer messageId = jobSeeker.getLastMessageId();
            EditMessageText editMessageText = new EditMessageText();
            editMessageText.setChatId(userId);
            editMessageText.setMessageId(messageId);
            editMessageText.setText(SpringyBotEnum.JOBSEEKERDEFAULTFORM.getText());

            editMessageText.setReplyMarkup(new KeyboardButton().keyboard_jobSeeker(jobSeekerDTO, true));
            try {
                custom.executeAsync(editMessageText);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            List<ChannelMessageIdPostCounts> channelMessageIdPostCounts = jobManagementServiceImpl
                    .findAllByBotIdAndUserIdAndTypeWithChannelMessageIdPostCounts(jobSeeker.getBotId(), userId,
                            SpringyBotEnum.JOBSEEKER.getText());
            List<GroupMessageIdPostCounts> groupMessageIdPostCounts = jobManagementServiceImpl
                    .findAllByBotIdAndUserIdAndTypeWithGroupMessageIdPostCounts(jobSeeker.getBotId(), userId,
                    SpringyBotEnum.JOBSEEKER.getText());
            channelMessageIdPostCounts.stream().forEach(cmp -> {
                DeleteMessage dm = new DeleteMessage();
                dm.setChatId(String.valueOf(cmp.getChannelId()));
                dm.setMessageId(cmp.getMessageId());
                try {
                    custom.executeAsync(dm);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                cmp.setMessageId(-1);
                cmp.setPostCount(0);
                jobManagementServiceImpl.saveChannelMessageIdPostCounts(cmp);
            });

            groupMessageIdPostCounts.stream().forEach(cmp -> {
                DeleteMessage dm = new DeleteMessage();
                dm.setChatId(String.valueOf(cmp.getGroupId()));
                dm.setMessageId(cmp.getMessageId());
                try {
                    custom.executeAsync(dm);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                cmp.setMessageId(-1);
                cmp.setPostCount(0);
                jobManagementServiceImpl.saveGroupMessageIdPostCounts(cmp);
            });

            this.response.setText(SpringyBotEnum.SUCCESSFULLYDELETED.getText());
            common.sendResponseAsync(this.response);
        } else if (callbackQuery.getData().equals(SpringyBotEnum.EDITJOBPOSTING.getText())) {
            response.setText(SpringyBotEnum.REMINDEDITOR.getText());

            common.sendResponseAsync(this.response);
        } else if (callbackQuery.getData().equals(SpringyBotEnum.EDITJOBSEEKER.getText())) {
            response.setText(SpringyBotEnum.REMINDEDITOR.getText());
            common.sendResponseAsync(this.response);
        }

    }

    private void messageSetting(Common common) {
        String chatId = String.valueOf(common.getUpdate().getCallbackQuery().getFrom().getId());
        this.response = new SendMessage();
        this.response.setChatId(chatId);
        this.response.setDisableNotification(false);
        this.response.setDisableWebPagePreview(false);
    }
}
