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
import com.lwdevelop.dto.JobPostingDTO;
import com.lwdevelop.dto.JobSeekerDTO;
import com.lwdevelop.dto.SpringyBotDTO;
import com.lwdevelop.entity.ChannelMessageIdPostCounts;
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
            String userId = callbackQuery.getData().substring("clearJobPosting_".length());
            // 在这里根据 springyBotId 和 userId 进行相应的清除操作
            JobPosting jobPosting = jobManagementServiceImpl.findByUserIdWithJobPosting(userId);
            jobPosting.setBaseSalary("");
            jobPosting.setCommission("");
            jobPosting.setCompany("");
            jobPosting.setFlightNumber("");
            jobPosting.setLocation("");
            jobPosting.setPosition("");
            jobPosting.setRequirements("");
            jobPosting.setWorkTime("");
            jobManagementServiceImpl.saveJobPosting(jobPosting);

            // 清除訊息
            Long id = Long.valueOf(jobPosting.getBotId());
            SpringyBot springyBot = springyBotServiceImpl.findById(id).get();
            SpringyBotDTO springyBotDTO = new SpringyBotDTO();
            springyBotDTO.setToken(springyBot.getToken());
            springyBotDTO.setUsername(springyBot.getUsername());
            Custom custom = new Custom(springyBotDTO);

            JobPostingDTO jobPostingDTO = new JobPostingDTO(userId, jobPosting.getBotId(), jobPosting.getCompany(),
                    jobPosting.getPosition(), jobPosting.getBaseSalary(), jobPosting.getCommission(),
                    jobPosting.getWorkTime(), jobPosting.getRequirements(), jobPosting.getLocation(),
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
                    "要求内容：\n" +
                    "🐌 地址：\n" +
                    "✈️咨询飞机号：");

            editMessageText.setReplyMarkup(new KeyboardButton().keyboard_jobPosting(jobPostingDTO,true));
            // editMessageText.setReplyMarkup(new KeyboardButton().keyboard_editJobPosting(jobPostingDTO));
            try {
                custom.executeAsync(editMessageText);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }


            List<ChannelMessageIdPostCounts> channelMessageIdPostCounts = jobManagementServiceImpl.findAllByBotIdAndUserIdAndTypeWithChannelMessageIdPostCounts(jobPosting.getBotId(), userId, "jobPosting");
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


            this.response.setText("删除成功");
            common.sendResponseAsync(this.response);
        } else if (callbackQuery.getData().startsWith("clearJobSeeker_")) {
            String userId = callbackQuery.getData().substring("clearJobSeeker_".length());
            // 在这里根据 springyBotId 和 userId 进行相应的清除操作
            JobSeeker jobSeeker = jobManagementServiceImpl.findByUserIdWithJobSeeker(userId);
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
            jobSeeker.setWorkExperience("");
            jobSeeker.setSelfIntroduction("");
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
                    jobSeeker.getSelfIntroduction(),jobSeeker.getFlightNumber());

            Integer messageId = jobSeeker.getLastMessageId();
            EditMessageText editMessageText = new EditMessageText();
            editMessageText.setChatId(userId);
            editMessageText.setMessageId(messageId);
            editMessageText.setText("求职人员\n\n" +
                    "姓名：\n" +
                    "男女：\n" +
                    "出生_年_月_日：\n" +
                    "年龄：\n" +
                    "国籍：\n" +
                    "学历：\n" +
                    "技能：\n" +
                    "目标职位：\n" +
                    "手上有什么资源：\n" +
                    "期望薪资：\n" +
                    "工作经历：\n" +
                    "自我介绍：\n" +
                    "✈️咨询飞机号：");

            editMessageText.setReplyMarkup(new KeyboardButton().keyboard_jobSeeker(jobSeekerDTO,true));
            // editMessageText.setReplyMarkup(new KeyboardButton().keyboard_JobSeeker(jobSeekerDTO));
            try {
                custom.executeAsync(editMessageText);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            List<ChannelMessageIdPostCounts> channelMessageIdPostCounts = jobManagementServiceImpl.findAllByBotIdAndUserIdAndTypeWithChannelMessageIdPostCounts(jobSeeker.getBotId(), userId, "jobSeeker");
            DeleteMessage dm = new DeleteMessage();
            channelMessageIdPostCounts.stream().forEach(cmp -> {
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

            this.response.setText("删除成功");
            common.sendResponseAsync(this.response);
        }else if (callbackQuery.getData().equals("editJobPosting_")){
            response.setText("提醒：请复制上列信息到输入框并进行编辑，编辑完毕发送");

            common.sendResponseAsync(this.response);
        } else if (callbackQuery.getData().equals("editJobSeeker_")) {
            response.setText("提醒：请复制上列信息到输入框并进行编辑，编辑完毕发送");
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
