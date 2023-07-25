package com.lwdevelop.bot.bots.talent.messageHandling;

import java.util.List;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import com.lwdevelop.bot.bots.talent.TalentLongPollingBot;
import com.lwdevelop.bot.bots.utils.Common;
import com.lwdevelop.bot.bots.utils.enum_.TalentEnum;
import com.lwdevelop.bot.chatMessageHandlers.BaseCallbackQuerys;
import com.lwdevelop.dto.JobPostingDTO;
import com.lwdevelop.dto.JobSeekerDTO;
import com.lwdevelop.dto.SpringyBotDTO;
import com.lwdevelop.entity.ChannelMessageIdPostCounts;
import com.lwdevelop.entity.GroupMessageIdPostCounts;
import com.lwdevelop.entity.JobPosting;
import com.lwdevelop.entity.JobSeeker;
import com.lwdevelop.entity.SpringyBot;

public class CallbackQuerys extends BaseCallbackQuerys {

    public CallbackQuerys(Common common) {
        super(common);
    }

    public void handler() {

        SendMessage response = new SendMessage();
        response.setChatId(chatId_str);
        response.setDisableWebPagePreview(true);

        if (callbackQuery.getData().startsWith("clearJobPosting_")) {
            clearJob("jobPosting");
        } else if (callbackQuery.getData().startsWith("clearJobSeeker_")) {
            clearJob("jobSeeker");
        } else if (callbackQuery.getData().equals("editJobPosting_")) {
            response.setText(TalentEnum.REMIND_EDITOR.getText());
            common.executeAsync(response);
        } else if (callbackQuery.getData().equals("editJobSeeker_")) {
            response.setText(TalentEnum.REMIND_EDITOR.getText());
            common.executeAsync(response);
        }

    }

    private void clearJob(String type) {

        SpringyBot springyBot = null;
        String userId = "";
        Integer messageId = 0;
        String botId = callbackQuery.getData().substring(callbackQuery.getData().lastIndexOf("_") + 1);
        List<ChannelMessageIdPostCounts> cmpc = null;
        List<GroupMessageIdPostCounts> gmpc = null;

        SendMessage response = new SendMessage();
        response.setChatId(chatId_str);
        response.setDisableWebPagePreview(true);

        if (type.equals("jobPosting")) {
            userId = callbackQuery.getData().substring("clearJobPosting_".length(),
                    callbackQuery.getData().lastIndexOf("_"));


            JobPosting jobPosting = jobManagementServiceImpl.findByUserIdAndBotIdWithJobPosting(userId, botId);
            jobManagementServiceImpl.saveJobPosting(new JobPostingDTO().resetJobPostingFields(jobPosting));
            Long id = Long.valueOf(jobPosting.getBotId());
            springyBot = springyBotServiceImpl.findById(id).get();
            messageId = jobPosting.getLastMessageId();

            cmpc = jobManagementServiceImpl
                    .findAllByBotIdAndUserIdAndTypeWithChannelMessageIdPostCounts(jobPosting.getBotId(), userId, type);
            gmpc = jobManagementServiceImpl
                    .findAllByBotIdAndUserIdAndTypeWithGroupMessageIdPostCounts(jobPosting.getBotId(), userId, type);
        } else if (type.equals("jobSeeker")) {
            userId = callbackQuery.getData().substring("clearJobSeeker_".length(),
                    callbackQuery.getData().lastIndexOf("_"));


            JobSeeker jobSeeker = jobManagementServiceImpl.findByUserIdAndBotIdWithJobSeeker(userId, botId);
            jobManagementServiceImpl.saveJobSeeker(new JobSeekerDTO().resetJobSeekerFields(jobSeeker));
            Long id = Long.valueOf(jobSeeker.getBotId());
            springyBot = springyBotServiceImpl.findById(id).get();
            messageId = jobSeeker.getLastMessageId();

            cmpc = jobManagementServiceImpl
                    .findAllByBotIdAndUserIdAndTypeWithChannelMessageIdPostCounts(jobSeeker.getBotId(), userId, type);
            gmpc = jobManagementServiceImpl
                    .findAllByBotIdAndUserIdAndTypeWithGroupMessageIdPostCounts(jobSeeker.getBotId(), userId, type);
        }
        // // 清除訊息
        if (springyBot != null && cmpc != null && gmpc != null) {
            SpringyBotDTO springyBotDTO = new SpringyBotDTO();
            springyBotDTO.setToken(springyBot.getToken());
            springyBotDTO.setUsername(springyBot.getUsername());
            TalentLongPollingBot custom = new TalentLongPollingBot(springyBotDTO);
            DeleteMessage deleteMessage = new DeleteMessage(userId, messageId);
            try {
                custom.executeAsync(deleteMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

            cmpc.stream().forEach(cmp -> {
                DeleteMessage dm = new DeleteMessage(String.valueOf(cmp.getChannelId()), cmp.getMessageId());
                common.executeAsync(dm);
                cmp.setMessageId(-1);
                cmp.setPostCount(0);
                jobManagementServiceImpl.saveChannelMessageIdPostCounts(cmp);
            });

            gmpc.stream().forEach(cmp -> {
                DeleteMessage dm = new DeleteMessage(String.valueOf(cmp.getGroupId()), cmp.getMessageId());
                common.executeAsync(dm);
                cmp.setMessageId(-1);
                cmp.setPostCount(0);
                jobManagementServiceImpl.saveGroupMessageIdPostCounts(cmp);
            });

            response.setText(TalentEnum.SUCCESSFULLYDELETED.getText());
            common.executeAsync(response);

        }

    }

}
