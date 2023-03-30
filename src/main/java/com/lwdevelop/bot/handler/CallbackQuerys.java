package com.lwdevelop.bot.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import com.lwdevelop.bot.utils.Common;
import com.lwdevelop.entity.JobPosting;
import com.lwdevelop.service.impl.JobManagementServiceImpl;
import com.lwdevelop.utils.SpringUtils;

public class CallbackQuerys {

    @Autowired
    private JobManagementServiceImpl jobManagementServiceImpl = SpringUtils.getApplicationContext()
            .getBean(JobManagementServiceImpl.class);

    private SendMessage response;

    public void handler(Common common) {
        CallbackQuery callbackQuery = common.getUpdate().getCallbackQuery();
    

        this.messageSetting(common);

        if (callbackQuery.getData().startsWith("clearJobPosting_")) {
            String userId = callbackQuery.getData().substring("clearJobPosting_".length());

            // 在这里根据 springyBotId 和 userId 进行相应的清除操作
            JobPosting jobPosting = jobManagementServiceImpl.findByUserIdWithJobPosting(userId);
            if (jobPosting != null) {
                jobPosting.setBaseSalary("");
                jobPosting.setCommission("");
                jobPosting.setCompany("");
                jobPosting.setFlightNumber("");
                jobPosting.setLocation("");
                jobPosting.setPosition("");
                jobPosting.setRequirements("");
                jobPosting.setWorkTime("");
                jobManagementServiceImpl.saveJobPosting(jobPosting);
            }
            this.response.setText("清除成功");
            common.sendResponseAsync(this.response);
        }


    }

    private void messageSetting(Common common) {
        String chatId =  String.valueOf(common.getUpdate().getCallbackQuery().getFrom().getId());
        this.response = new SendMessage();
        this.response.setChatId(chatId);
        this.response.setDisableNotification(false);
        this.response.setDisableWebPagePreview(false);
    }
}
