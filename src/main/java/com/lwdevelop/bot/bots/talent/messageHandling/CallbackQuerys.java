package com.lwdevelop.bot.bots.talent.messageHandling;

import java.util.List;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import com.lwdevelop.bot.bots.talent.TalentLongPollingBot;
import com.lwdevelop.bot.bots.utils.Common;
import com.lwdevelop.bot.bots.utils.enum_.TelentEnum;
import com.lwdevelop.bot.bots.utils.keyboardButton.TelentButton;
import com.lwdevelop.bot.chatMessageHandlers.BaseCallbackQuerys;
import com.lwdevelop.dto.JobPostingDTO;
import com.lwdevelop.dto.JobSeekerDTO;
import com.lwdevelop.dto.SpringyBotDTO;
import com.lwdevelop.entity.ChannelMessageIdPostCounts;
import com.lwdevelop.entity.GroupMessageIdPostCounts;
import com.lwdevelop.entity.JobPosting;
import com.lwdevelop.entity.JobSeeker;
import com.lwdevelop.entity.SpringyBot;

public class CallbackQuerys extends BaseCallbackQuerys{

    public CallbackQuerys(Common common) {
        super(common);
    }

    public void handler() {
        SendMessage response = new SendMessage();
        response.setChatId(chatId_str);
        response.setDisableWebPagePreview(true);

        if (callbackQuery.getData().startsWith("clearJobPosting_")) {

            String userId = callbackQuery.getData().substring("clearJobPosting_".length(),
                    callbackQuery.getData().lastIndexOf("_"));
            String botId = callbackQuery.getData().substring(callbackQuery.getData().lastIndexOf("_") + 1);

            // springyBotId 和 userId 進行相應的清除操作
            JobPosting jobPosting = jobManagementServiceImpl.findByUserIdAndBotIdWithJobPosting(userId, botId);
            jobManagementServiceImpl.saveJobPosting(new JobPostingDTO().resetJobPostingFields(jobPosting));

            // // 清除訊息
            Long id = Long.valueOf(jobPosting.getBotId());
            SpringyBot springyBot = springyBotServiceImpl.findById(id).get();
            SpringyBotDTO springyBotDTO = new SpringyBotDTO();
            springyBotDTO.setToken(springyBot.getToken());
            springyBotDTO.setUsername(springyBot.getUsername());
            TalentLongPollingBot custom = new TalentLongPollingBot(springyBotDTO);

            JobPostingDTO jobPostingDTO = new JobPostingDTO().convertToJobPostingDTO(jobPosting);

            Integer messageId = jobPosting.getLastMessageId();
            EditMessageText editMessageText = new EditMessageText();
            editMessageText.setChatId(userId);
            editMessageText.setMessageId(messageId);
            editMessageText.setText(TelentEnum.JOBPOSTING_DEFAULT_FORM.getText());

            editMessageText.setReplyMarkup(new TelentButton().keyboard_jobPosting(jobPostingDTO, true));
            try {
                custom.executeAsync(editMessageText);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

            List<ChannelMessageIdPostCounts> channelMessageIdPostCounts = jobManagementServiceImpl
                    .findAllByBotIdAndUserIdAndTypeWithChannelMessageIdPostCounts(jobPosting.getBotId(), userId,
                            "jobPosting");
            List<GroupMessageIdPostCounts> groupMessageIdPostCounts = jobManagementServiceImpl
                    .findAllByBotIdAndUserIdAndTypeWithGroupMessageIdPostCounts(jobPosting.getBotId(), userId,
                            "jobPosting");

            channelMessageIdPostCounts.stream().forEach(cmp -> {
                DeleteMessage dm = new DeleteMessage(String.valueOf(cmp.getChannelId()), cmp.getMessageId());
                common.executeAsync(dm);
                cmp.setMessageId(-1);
                cmp.setPostCount(0);
                jobManagementServiceImpl.saveChannelMessageIdPostCounts(cmp);
            });

            groupMessageIdPostCounts.stream().forEach(cmp -> {
                DeleteMessage dm = new DeleteMessage(String.valueOf(cmp.getGroupId()), cmp.getMessageId());
                common.executeAsync(dm);
                cmp.setMessageId(-1);
                cmp.setPostCount(0);
                jobManagementServiceImpl.saveGroupMessageIdPostCounts(cmp);
            });

            response.setText(TelentEnum.SUCCESSFULLYDELETED.getText());
            common.executeAsync(response);
        } else if (callbackQuery.getData().startsWith("clearJobSeeker_")) {

            String userId = callbackQuery.getData().substring("clearJobSeeker_".length(),
                    callbackQuery.getData().lastIndexOf("_"));
            String botId = callbackQuery.getData().substring(callbackQuery.getData().lastIndexOf("_") + 1);
            // 在这里根据 springyBotId 和 userId 进行相应的清除操作
            JobSeeker jobSeeker = jobManagementServiceImpl.findByUserIdAndBotIdWithJobSeeker(userId, botId);

            jobManagementServiceImpl.saveJobSeeker(new JobSeekerDTO().resetJobSeekerFields(jobSeeker));

            // 清除訊息
            Long id = Long.valueOf(jobSeeker.getBotId());
            SpringyBot springyBot = springyBotServiceImpl.findById(id).get();
            SpringyBotDTO springyBotDTO = new SpringyBotDTO();
            springyBotDTO.setToken(springyBot.getToken());
            springyBotDTO.setUsername(springyBot.getUsername());
            TalentLongPollingBot custom = new TalentLongPollingBot(springyBotDTO);

            JobSeekerDTO jobSeekerDTO = new JobSeekerDTO().convertToJobSeekerDTO(jobSeeker);

            Integer messageId = jobSeeker.getLastMessageId();
            EditMessageText editMessageText = new EditMessageText();
            editMessageText.setChatId(userId);
            editMessageText.setMessageId(messageId);
            editMessageText.setText(TelentEnum.JOBSEEKER_DEFAULT_FORM.getText());

            editMessageText.setReplyMarkup(new TelentButton().keyboard_jobSeeker(jobSeekerDTO, true));
            try {
                custom.executeAsync(editMessageText);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            List<ChannelMessageIdPostCounts> channelMessageIdPostCounts = jobManagementServiceImpl
                    .findAllByBotIdAndUserIdAndTypeWithChannelMessageIdPostCounts(jobSeeker.getBotId(), userId,
                    "jobSeeker");
            List<GroupMessageIdPostCounts> groupMessageIdPostCounts = jobManagementServiceImpl
                    .findAllByBotIdAndUserIdAndTypeWithGroupMessageIdPostCounts(jobSeeker.getBotId(), userId,
                    "jobSeeker");
            channelMessageIdPostCounts.stream().forEach(cmp -> {
                DeleteMessage dm = new DeleteMessage(String.valueOf(cmp.getChannelId()), cmp.getMessageId());
                common.executeAsync(dm);
                cmp.setMessageId(-1);
                cmp.setPostCount(0);
                jobManagementServiceImpl.saveChannelMessageIdPostCounts(cmp);
            });

            groupMessageIdPostCounts.stream().forEach(cmp -> {
                DeleteMessage dm = new DeleteMessage(String.valueOf(cmp.getGroupId()), cmp.getMessageId());
                common.executeAsync(dm);
                cmp.setMessageId(-1);
                cmp.setPostCount(0);
                jobManagementServiceImpl.saveGroupMessageIdPostCounts(cmp);
            });

            response.setText(TelentEnum.SUCCESSFULLYDELETED.getText());
            common.executeAsync(response);
        } else if (callbackQuery.getData().equals("editJobPosting_")) {
            response.setText(TelentEnum.REMIND_EDITOR.getText());

            common.executeAsync(response);
        } else if (callbackQuery.getData().equals("editJobSeeker_")) {
            response.setText(TelentEnum.REMIND_EDITOR.getText());
            common.executeAsync(response);
        }

    }

}
