package com.lwdevelop.bot.handler.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import com.lwdevelop.bot.utils.Common;
import com.lwdevelop.bot.utils.KeyboardButton;
import com.lwdevelop.entity.JobPosting;
import com.lwdevelop.entity.JobSeeker;
import com.lwdevelop.service.impl.JobManagementServiceImpl;
import com.lwdevelop.utils.SpringUtils;

public class Job {
    SendMessage response;

    @Autowired
    private JobManagementServiceImpl jobManagementServiceImpl = SpringUtils.getApplicationContext()
            .getBean(JobManagementServiceImpl.class);

    private void jobMessageSetting(Message message) {
        this.response = new SendMessage();
        this.response.setChatId(String.valueOf(message.getChatId()));
        this.response.setDisableNotification(false);
        this.response.setDisableWebPagePreview(false);
    }

    public void setResponse_jobSeeker_management(Common common) {
        this.jobMessageSetting(common.getUpdate().getMessage());
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
        this.response
                .setReplyMarkup(new KeyboardButton().jobFormManagement(common, "jobSeekerForm",new JobPosting(),new JobSeeker()));
        common.sendResponseAsync(this.response);

    }

    public void setResponse_jobPosting_management(Common common) {
        this.jobMessageSetting(common.getUpdate().getMessage());
        String userId = String.valueOf(common.getUpdate().getMessage().getChatId());
        JobPosting jobPosting = this.jobManagementServiceImpl.findByUserIdWithJobPosting(userId);

        String company = ""; // 公司名称
        String position = ""; // 职位名称
        String baseSalary = ""; // 底薪
        String commission = ""; // 提成
        String workTime = ""; // 上班时间
        String requirements = ""; // 要求内容
        String location = ""; // 地址
        String flightNumber = ""; // 咨询飞机号

        if (jobPosting != null) {
            company = jobPosting.getCompany();
            position = jobPosting.getPosition();
            baseSalary = jobPosting.getBaseSalary();
            commission = jobPosting.getCommission();
            workTime = jobPosting.getWorkTime();
            requirements = jobPosting.getRequirements();
            location = jobPosting.getLocation();
            flightNumber = jobPosting.getFlightNumber();
        }
        this.response.setText("招聘人才\n" +
                "公司：" + company + "\n" +
                "职位：" + position + "\n" +
                "底薪：" + baseSalary + "\n" +
                "提成：" + commission + "\n" +
                "上班时间：" + workTime + "\n" +
                "要求内容：" + requirements + "\n" +
                "🐌 地址：" + location + "\n" +
                "✈️咨询飞机号： " + flightNumber);
        this.response.setReplyMarkup(new KeyboardButton().jobFormManagement(common, "jobPostingForm",jobPosting,new JobSeeker()));
        common.sendResponseAsync(this.response);

    }

}
