package com.lwdevelop.bot.handler.messageEvent.private_.commands;

import java.util.Iterator;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import com.lwdevelop.bot.utils.Common;
import com.lwdevelop.bot.utils.KeyboardButton;
import com.lwdevelop.dto.JobPostingDTO;
import com.lwdevelop.dto.JobSeekerDTO;
import com.lwdevelop.entity.JobPosting;
import com.lwdevelop.entity.JobSeeker;
import com.lwdevelop.entity.JobUser;
import com.lwdevelop.entity.RobotChannelManagement;
import com.lwdevelop.entity.SpringyBot;
import com.lwdevelop.service.impl.JobManagementServiceImpl;
import com.lwdevelop.service.impl.SpringyBotServiceImpl;
import com.lwdevelop.utils.SpringUtils;

public class Job {
        SendMessage response;

        @Autowired
        private JobManagementServiceImpl jobManagementServiceImpl = SpringUtils.getApplicationContext()
                        .getBean(JobManagementServiceImpl.class);

        @Autowired
        private SpringyBotServiceImpl springyBotServiceImpl = SpringUtils.getApplicationContext()
                        .getBean(SpringyBotServiceImpl.class);

        private void jobMessageSetting(Message message) {
                this.response = new SendMessage();
                this.response.setChatId(String.valueOf(message.getChatId()));
                this.response.setDisableNotification(false);
                this.response.setDisableWebPagePreview(false);
        }

        public void postRecruitment(Common common) {

                // send to channel
                SpringyBot springyBot = springyBotServiceImpl.findById(common.getSpringyBotId()).get();
                Iterator<RobotChannelManagement> iterator = springyBot.getRobotChannelManagement().iterator();
                String userId = String.valueOf(common.getUpdate().getMessage().getChatId());
                springyBot.getJobUser().stream().filter(ju -> ju.getUserId().equals(userId))
                                .findFirst().ifPresent(j -> {
                                        j.getJobPosting().stream().filter(jp -> jp.getUserId().equals(userId))
                                                        .findFirst().ifPresent(
                                                                        jobPosting -> {
                                                                                while (iterator.hasNext()) {
                                                                                        sendTextWithJobPosting(
                                                                                                        jobPosting,
                                                                                                        common,
                                                                                                        iterator.next());
                                                                                }
                                                                        });
                                });
                ;

        }

        public void postAJobSearch(Common common) {
                SpringyBot springyBot = springyBotServiceImpl.findById(common.getSpringyBotId()).get();

                // send to channel
                Iterator<RobotChannelManagement> iterator = springyBot.getRobotChannelManagement()
                                .iterator();
                String userId = String.valueOf(common.getUpdate().getMessage().getChatId());
                springyBot.getJobUser().stream().filter(ju -> ju.getUserId().equals(userId)).findFirst()
                                .ifPresent(j -> {
                                        j.getJobSeeker()
                                                        .stream()
                                                        .filter(
                                                                        jp -> jp.getUserId().equals(userId))
                                                        .findFirst()
                                                        .ifPresent(
                                                                        jobSeeker -> {
                                                                                while (iterator
                                                                                                .hasNext()) {
                                                                                        this.sendTextWithJobSeeker(
                                                                                                        jobSeeker,
                                                                                                        common,
                                                                                                        iterator
                                                                                                                        .next());
                                                                                }
                                                                        });
                                });
                ;
        }

        public void setResponse_jobPosting_management(Common common) {
                this.jobMessageSetting(common.getUpdate().getMessage());

                Long id = common.getSpringyBotId();
                String userId = String.valueOf(common.getUpdate().getMessage().getChatId());
                String firstname = Optional.ofNullable(common.getUpdate().getMessage().getChat().getFirstName())
                                .orElse("");
                String username = Optional.ofNullable(common.getUpdate().getMessage().getChat().getUserName())
                                .orElse("");
                String lastname = Optional.ofNullable(common.getUpdate().getMessage().getChat().getLastName())
                                .orElse("");

                SpringyBot springyBot = springyBotServiceImpl.findById(id).orElseThrow();
                JobPosting jobPosting = jobManagementServiceImpl.findByUserIdAndBotIdWithJobPosting(userId,
                                String.valueOf(id));

                JobUser jobUser = springyBot.getJobUser().stream().filter(j -> j.getUserId().equals(userId)).findFirst()
                                .orElseGet(() -> {
                                        JobUser ju = new JobUser();
                                        ju.setUserId(userId);
                                        springyBot.getJobUser().add(ju);
                                        return ju;
                                });

                jobUser.setFirstname(firstname);
                jobUser.setLastname(lastname);
                jobUser.setUsername(username);

                JobPostingDTO jobPostingDTO = new JobPostingDTO(userId, String.valueOf(id));
                String company = "", position = "", baseSalary = "", commission = "", workTime = "", requirements = "",
                                location = "", flightNumber = "";

                if (jobPosting != null) {
                        company = Optional.ofNullable(jobPosting.getCompany()).orElse("");
                        position = Optional.ofNullable(jobPosting.getPosition()).orElse("");
                        baseSalary = Optional.ofNullable(jobPosting.getBaseSalary()).orElse("");
                        commission = Optional.ofNullable(jobPosting.getCommission()).orElse("");
                        workTime = Optional.ofNullable(jobPosting.getWorkTime()).orElse("");
                        requirements = Optional.ofNullable(jobPosting.getRequirements()).orElse("");
                        location = Optional.ofNullable(jobPosting.getLocation()).orElse("");
                        flightNumber = Optional.ofNullable(jobPosting.getFlightNumber()).orElse("");
                        this.response.setText(
                                        "招聘人才\n" + "公司：" + company + "\n" + "职位：" + position + "\n" + "底薪：" + baseSalary
                                                        + "\n" + "提成：" + commission + "\n" + "上班时间：" + workTime + "\n"
                                                        + "要求内容：" + requirements + "\n"
                                                        + "🐌 地址：" + location + "\n" + "✈️咨询飞机号：" + flightNumber);
                        this.response.setReplyMarkup(new KeyboardButton().keyboard_jobPosting(jobPostingDTO));
                        jobPosting.setLastMessageId(common.sendResponseAsync(this.response));
                        jobManagementServiceImpl.saveJobPosting(jobPosting);
                } else {
                        this.response.setText("招聘人才\n" + "公司：\n" + "职位：\n" + "底薪：\n" + "提成：\n" + "上班时间：\n" + "要求内容：\n"
                                        + "🐌 地址：\n" + "✈️咨询飞机号： ");
                        this.response.setReplyMarkup(new KeyboardButton().keyboard_jobPosting(jobPostingDTO));
                        JobPosting jp = new JobPosting(userId, String.valueOf(id),
                                        common.sendResponseAsync(this.response));
                        jobUser.getJobPosting().add(jp);
                        jobManagementServiceImpl.saveJobPosting(jp);
                }
                springyBotServiceImpl.save(springyBot);
        }

        public void setResponse_jobSeeker_management(Common common) {
                this.jobMessageSetting(common.getUpdate().getMessage());

                Long id = common.getSpringyBotId();
                String userId = String.valueOf(common.getUpdate().getMessage().getChatId());
                String firstname = Optional.ofNullable(common.getUpdate().getMessage().getChat().getFirstName())
                                .orElse("");
                String username = Optional.ofNullable(common.getUpdate().getMessage().getChat().getUserName())
                                .orElse("");
                String lastname = Optional.ofNullable(common.getUpdate().getMessage().getChat().getLastName())
                                .orElse("");

                SpringyBot springyBot = springyBotServiceImpl.findById(id).orElseThrow();
                JobSeeker jobSeeker = jobManagementServiceImpl.findByUserIdAndBotIdWithJobSeeker(userId,
                                String.valueOf(id));

                JobUser jobUser = springyBot.getJobUser().stream().filter(j -> j.getUserId().equals(userId)).findFirst()
                                .orElseGet(() -> {
                                        JobUser ju = new JobUser();
                                        ju.setUserId(userId);
                                        springyBot.getJobUser().add(ju);
                                        return ju;
                                });

                jobUser.setFirstname(firstname);
                jobUser.setLastname(lastname);
                jobUser.setUsername(username);

                JobSeekerDTO jobSeekerDTO = new JobSeekerDTO(userId, String.valueOf(id));
                String name = "", gender = "", dateOfBirth = "", age = "", nationality = "", education = "",
                                skills = "", targetPosition = "", resources = "", expectedSalary = "",
                                workExperience = "", selfIntroduction = "",flightNumber="";
                if (jobSeeker != null) {
                        name = Optional.ofNullable(jobSeeker.getName()).orElse("");
                        gender = Optional.ofNullable(jobSeeker.getGender()).orElse("");
                        dateOfBirth = Optional.ofNullable(jobSeeker.getDateOfBirth()).orElse("");
                        age = Optional.ofNullable(jobSeeker.getAge()).orElse("");
                        nationality = Optional.ofNullable(jobSeeker.getNationality()).orElse("");
                        education = Optional.ofNullable(jobSeeker.getEducation()).orElse("");
                        skills = Optional.ofNullable(jobSeeker.getSkills()).orElse("");
                        targetPosition = Optional.ofNullable(jobSeeker.getTargetPosition()).orElse("");
                        resources = Optional.ofNullable(jobSeeker.getResources()).orElse("");
                        expectedSalary = Optional.ofNullable(jobSeeker.getExpectedSalary()).orElse("");
                        workExperience = Optional.ofNullable(jobSeeker.getWorkExperience()).orElse("");
                        selfIntroduction = Optional.ofNullable(jobSeeker.getSelfIntroduction()).orElse("");
                        flightNumber = Optional.ofNullable(jobSeeker.getFlightNumber()).orElse("");

                        this.response.setText("求职人员\n\n姓名：" + name + "\n男女：" + gender + "\n出生_年_月_日："
                                        + dateOfBirth
                                        + "\n年龄：" + age + "\n国籍：" + nationality + "\n学历：" + education
                                        + "\n技能：" + skills + "\n目标职位：" + targetPosition + "\n手上有什么资源："
                                        + resources + "\n期望薪资：" + expectedSalary + "\n工作经历："
                                        + workExperience + "\n自我介绍：" + selfIntroduction +"\n咨询飞机号：" + flightNumber );
                        this.response.setReplyMarkup(new KeyboardButton().keyboard_jobSeeker(jobSeekerDTO));
                        jobSeeker.setLastMessageId(common.sendResponseAsync(this.response));
                        jobManagementServiceImpl.saveJobSeeker(jobSeeker);
                } else {
                        this.response.setText(
                                        "求职人员\n姓名：\n男女：\n出生_年_月_日：\n年龄：\n国籍：\n学历：\n技能：\n目标职位：\n手上有什么资源：\n期望薪资：\n工作经历：\n自我介绍：\n咨询飞机号：");
                        this.response.setReplyMarkup(new KeyboardButton().keyboard_jobSeeker(jobSeekerDTO));
                        JobSeeker js = new JobSeeker(userId, String.valueOf(id),
                                        common.sendResponseAsync(this.response));
                        jobUser.getJobSeeker().add(js);
                        jobManagementServiceImpl.saveJobSeeker(js);
                }
                springyBotServiceImpl.save(springyBot);
        }

        private void sendTextWithJobSeeker(JobSeeker jobSeeker, Common common,
                        RobotChannelManagement robotChannelManagement) {

                StringBuilder sb = new StringBuilder();
                appendIfNotEmpty(sb, "姓名：", jobSeeker.getName());
                appendIfNotEmpty(sb, "男女：", jobSeeker.getGender());
                appendIfNotEmpty(sb, "出生_年_月_日：", jobSeeker.getDateOfBirth());
                appendIfNotEmpty(sb, "年龄：", jobSeeker.getAge());
                appendIfNotEmpty(sb, "国籍：", jobSeeker.getNationality());
                appendIfNotEmpty(sb, "学历：", jobSeeker.getEducation());
                appendIfNotEmpty(sb, "技能：", jobSeeker.getSkills());
                appendIfNotEmpty(sb, "目标职位：", jobSeeker.getTargetPosition());
                appendIfNotEmpty(sb, "手上有什么资源：", jobSeeker.getResources());
                appendIfNotEmpty(sb, "期望薪资：", jobSeeker.getExpectedSalary());
                appendIfNotEmpty(sb, "工作经历：", jobSeeker.getWorkExperience());
                appendIfNotEmpty(sb, "自我介绍：", jobSeeker.getSelfIntroduction());
                appendIfNotEmpty(sb, "咨询飞机号：", jobSeeker.getFlightNumber());
                String result = sb.toString().trim(); // 去掉前后空格

                SendMessage response = new SendMessage();
                if (!result.isEmpty()) {
                        String username = common.getUpdate().getMessage().getChat().getUserName();
                        response.setChatId(String.valueOf(robotChannelManagement.getChannelId()));
                        response.setText("求职人员\n\n" + result);
                        response.setReplyMarkup(new KeyboardButton().keyboard_callme(username));
                        common.sendResponseAsync(response);
                }else{
                        Long chatId = common.getUpdate().getMessage().getChatId();
                        response.setChatId(String.valueOf(chatId));
                        response.setText("尚未编辑招聘表单");
                        common.sendResponseAsync(response);  
                }

        }

        private void sendTextWithJobPosting(JobPosting jobPosting, Common common,
                        RobotChannelManagement robotChannelManagement) {
                StringBuilder sb = new StringBuilder();
                appendIfNotEmpty(sb, "公司：", jobPosting.getCompany());
                appendIfNotEmpty(sb, "职位：", jobPosting.getPosition());
                appendIfNotEmpty(sb, "底薪：", jobPosting.getBaseSalary());
                appendIfNotEmpty(sb, "提成：", jobPosting.getCommission());
                appendIfNotEmpty(sb, "上班时间：", jobPosting.getWorkTime());
                appendIfNotEmpty(sb, "要求内容：", jobPosting.getRequirements());
                appendIfNotEmpty(sb, "🐌 地址：", jobPosting.getLocation());
                appendIfNotEmpty(sb, "✈️咨询飞机号：", jobPosting.getFlightNumber());
                String result = sb.toString().trim(); // 去掉前后空格

                SendMessage response = new SendMessage();
                if (!result.isEmpty()) {
                        String username = common.getUpdate().getMessage().getChat().getUserName();
                        response.setChatId(String.valueOf(robotChannelManagement.getChannelId()));
                        response.setText("招聘人才\n\n" + result);
                        response.setReplyMarkup(new KeyboardButton().keyboard_callme(username));
                        common.sendResponseAsync(response);
                }else{
                        Long chatId = common.getUpdate().getMessage().getChatId();
                        response.setChatId(String.valueOf(chatId));
                        response.setText("尚未编辑招聘表单");
                        common.sendResponseAsync(response);   
                }
        }

        private void appendIfNotEmpty(StringBuilder sb, String label, String value) {
                if (value != null && !value.isEmpty()) {
                        sb.append(label).append(value).append("\n");
                }
        }

}
