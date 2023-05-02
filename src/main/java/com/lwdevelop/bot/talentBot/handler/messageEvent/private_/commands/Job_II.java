package com.lwdevelop.bot.talentBot.handler.messageEvent.private_.commands;

import java.util.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import com.lwdevelop.bot.talentBot.utils.Common;
import com.lwdevelop.bot.talentBot.utils.KeyboardButton;
import com.lwdevelop.dto.JobPostingDTO;
import com.lwdevelop.dto.JobSeekerDTO;
import com.lwdevelop.entity.ChannelMessageIdPostCounts;
import com.lwdevelop.entity.GroupMessageIdPostCounts;
import com.lwdevelop.entity.JobPosting;
import com.lwdevelop.entity.JobSeeker;
import com.lwdevelop.entity.JobUser;
import com.lwdevelop.entity.RobotChannelManagement;
import com.lwdevelop.entity.RobotGroupManagement;
import com.lwdevelop.entity.SpringyBot;
import com.lwdevelop.service.impl.JobManagementServiceImpl;
import com.lwdevelop.service.impl.SpringyBotServiceImpl;
import com.lwdevelop.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;

@Slf4j
public class Job_II {

    @Autowired
    private JobManagementServiceImpl jobManagementServiceImpl = SpringUtils.getApplicationContext()
            .getBean(JobManagementServiceImpl.class);

    @Autowired
    private SpringyBotServiceImpl springyBotServiceImpl = SpringUtils.getApplicationContext()
            .getBean(SpringyBotServiceImpl.class);

    private Common common;

    public void setResponse_jobPosting_management(Common common) {
        log.info("Entering setResponse_jobPosting_management method...");
        Long id = common.getSpringyBotId();
        String userId = String.valueOf(common.getUpdate().getMessage().getChatId());

        SendMessage response = new SendMessage();
        response.setChatId(userId);

        JobPosting jobPosting = jobManagementServiceImpl.findByUserIdAndBotIdWithJobPosting(userId,
                String.valueOf(id));

        SpringyBot springyBot = springyBotServiceImpl.findById(id).orElseThrow();
        springyBot.getJobUser().stream().filter(j -> j.getUserId().equals(userId)).findFirst()
                .orElseGet(() -> {
                    JobUser ju = new JobUser();
                    ju.setUserId(userId);
                    springyBot.getJobUser().add(ju);
                    return ju;
                });
        String company = "", position = "", baseSalary = "", commission = "", workTime = "", requirements = "",
                location = "", flightNumber = "";
        if (jobPosting == null) {
            response.setText("招聘人才\n\n" + "公司：\n" + "职位：\n" + "底薪：\n" + "提成：\n" + "上班时间：\n" + "要求内容：（限50字以内）\n"
                    + "🐌地址：\n" + "✈️咨询飞机号：\n\n" + "关注 @rc499 点击 @rc899Bot 发布");
            log.info("No job posting found for user {}, bot id {}", userId, id);
        } else {
            company = Optional.ofNullable(jobPosting.getCompany()).orElse("");
            position = Optional.ofNullable(jobPosting.getPosition()).orElse("");
            baseSalary = Optional.ofNullable(jobPosting.getBaseSalary()).orElse("");
            commission = Optional.ofNullable(jobPosting.getCommission()).orElse("");
            workTime = Optional.ofNullable(jobPosting.getWorkTime()).orElse("");
            requirements = Optional.ofNullable(jobPosting.getRequirements()).orElse("（限50字以内）");
            location = Optional.ofNullable(jobPosting.getLocation()).orElse("");
            flightNumber = Optional.ofNullable(jobPosting.getFlightNumber()).orElse("");
            response.setText(
                    "招聘人才\n\n" + "公司：" + company + "\n" + "职位：" + position + "\n" + "底薪："
                            + baseSalary + "\n" + "提成：" + commission + "\n" + "上班时间："
                            + workTime + "\n" + "要求内容：" + requirements + "\n"
                            + "🐌地址：" + location + "\n" + "✈️咨询飞机号：" + flightNumber + "\n\n 关注 @rc499 点击 @rc899Bot 发布");
            log.info("Job posting found for user {}, bot id {}: {}", userId, id, jobPosting);

        }

        common.sendResponseAsync(response);

        response.setText("提醒：复制模板到输入框编辑发送，一个账号只能发布一次。可以删除重新发布显示最新时间，或是使用新的账号发布");
        common.sendResponseAsync(response);

    }

    public void setResponse_jobSeeker_management(Common common) {
        log.info("Entering setResponse_jobSeeker_management method...");
        Long id = common.getSpringyBotId();
        String userId = String.valueOf(common.getUpdate().getMessage().getChatId());

        SendMessage response = new SendMessage();
        response.setChatId(userId);

        JobSeeker jobSeeker = jobManagementServiceImpl.findByUserIdAndBotIdWithJobSeeker(userId,
                String.valueOf(id));

        SpringyBot springyBot = springyBotServiceImpl.findById(id).orElseThrow();
        springyBot.getJobUser().stream().filter(j -> j.getUserId().equals(userId)).findFirst()
                .orElseGet(() -> {
                    JobUser ju = new JobUser();
                    ju.setUserId(userId);
                    springyBot.getJobUser().add(ju);
                    return ju;
                });
        String name = "", gender = "", dateOfBirth = "", age = "", nationality = "", education = "",
                skills = "", targetPosition = "", resources = "", expectedSalary = "",
                workExperience = "", selfIntroduction = "", flightNumber = "";
        if (jobSeeker == null) {
            response.setText(
                    "求职人员\n\n姓名：\n男女：\n出生_年_月_日：\n年龄：\n国籍：\n学历：\n技能：\n目标职位：\n手上有什么资源：\n期望薪资：\n工作经历：（限50字以内）\n自我介绍：（限50字以内）\n✈️咨询飞机号：\n\n 关注 @rc499 点击 @rc899Bot 发布");
            log.info("No job seeker found for user {}, bot id {}", userId, id);

        } else {
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
            workExperience = Optional.ofNullable(jobSeeker.getWorkExperience()).orElse("（限50字以内）");
            selfIntroduction = Optional.ofNullable(jobSeeker.getSelfIntroduction()).orElse("（限50字以内）");
            flightNumber = Optional.ofNullable(jobSeeker.getFlightNumber()).orElse("");

            response.setText("求职人员\n\n姓名：" + name + "\n男女：" + gender + "\n出生_年_月_日："
                    + dateOfBirth
                    + "\n年龄：" + age + "\n国籍：" + nationality + "\n学历：" + education
                    + "\n技能：" + skills + "\n目标职位：" + targetPosition + "\n手上有什么资源："
                    + resources + "\n期望薪资：" + expectedSalary + "\n工作经历："
                    + workExperience + "\n自我介绍：" + selfIntroduction + "\n✈️咨询飞机号：" + flightNumber
                    + "\n\n 关注 @rc499 点击 @rc899Bot 发布");
            log.info("Job seeker found for user {}, bot id {}: {}", userId, id, jobSeeker);

        }

        common.sendResponseAsync(response);

        response.setText("提醒：复制模板到输入框编辑发送，一个账号只能发布一次。可以删除重新发布显示最新时间，或是使用新的账号发布");
        common.sendResponseAsync(response);

    }

    public void generateTextJobPosting(Common common, Boolean isEdit) {
        this.common = common;
        Message message = common.getUpdate().getMessage();
        String text = message.getText();
        // 将文本内容按行分割成字符串数组
        String[] lines = text.split("\\r?\\n");

        JobPosting jobPosting = jobManagementServiceImpl.findByUserIdAndBotIdWithJobPosting(
                String.valueOf(message.getChatId()), String.valueOf(common.getSpringyBotId()));

        if (jobPosting == null) {
            jobPosting = new JobPosting();
        }

        String isSuccess = fillJobPostingInfo(jobPosting, lines);

        if (!StringUtils.hasText(isSuccess)) {
            jobPosting.setBotId(String.valueOf(common.getSpringyBotId()));
            jobPosting.setUserId(String.valueOf(message.getChatId()));
            jobPosting.setLastMessageId(message.getMessageId());
            // 處理資料表
            Long id = common.getSpringyBotId();
            String userId = String.valueOf(common.getUpdate().getMessage().getChatId());
            SpringyBot springyBot = springyBotServiceImpl.findById(id).orElseThrow();
            JobUser jobUser = springyBot.getJobUser().stream().filter(j -> j.getUserId().equals(userId)).findFirst()
                    .orElseGet(() -> {
                        JobUser ju = new JobUser();
                        ju.setUserId(userId);
                        springyBot.getJobUser().add(ju);
                        // 存到job_user_job_posting表
                        springyBotServiceImpl.save(springyBot);
                        return ju;
                    });

            final Long final_jobPostingId = jobPosting.getId();
            final JobPosting final_jobPosting = jobPosting;

            if (!jobUser.getJobPosting().stream().anyMatch(p -> p.getId().equals(final_jobPostingId))) {
                jobUser.getJobPosting().add(jobPosting);
                springyBot.getJobUser().add(jobUser);
                jobManagementServiceImpl.saveJobPosting(jobPosting);
                springyBotServiceImpl.save(springyBot);
            } else {
                jobUser.getJobPosting().stream().filter(jp -> jp.getId().equals(final_jobPostingId)).findFirst()
                        .ifPresent(action -> {
                            jobManagementServiceImpl.saveJobPosting(final_jobPosting);
                        });
            }

            StringBuilder sb = new StringBuilder();
            appendIfNotEmpty(sb, "公司：", jobPosting.getCompany());
            appendIfNotEmpty(sb, "职位：", jobPosting.getPosition());
            appendIfNotEmpty(sb, "底薪：", jobPosting.getBaseSalary());
            appendIfNotEmpty(sb, "提成：", jobPosting.getCommission());
            appendIfNotEmpty(sb, "上班时间：", jobPosting.getWorkTime());
            appendIfNotEmpty(sb, "要求内容：", jobPosting.getRequirements());
            appendIfNotEmpty(sb, "🐌地址：", jobPosting.getLocation());
            appendIfNotEmpty(sb, "✈️咨询飞机号：", jobPosting.getFlightNumber());

            String result = sb.toString().trim();

            Iterator<RobotChannelManagement> iterator_channel = springyBot.getRobotChannelManagement().iterator();

            while (iterator_channel.hasNext()) {
                RobotChannelManagement robotChannelManagement = iterator_channel.next();
                if (!result.isEmpty()) {
                    SendMessage response = new SendMessage();
                    Long channelId = robotChannelManagement.getChannelId();
                    String channelTitle = robotChannelManagement.getChannelTitle();
                    String channelLink = robotChannelManagement.getLink();
                    response.setChatId(String.valueOf(channelId));
                    response.setText("招聘人才\n\n" + result + "\n\n 关注 @rc499 点击 @rc899Bot 发布");
                    response.setDisableWebPagePreview(true);
                    response.setDisableNotification(true);
                    ChannelMessageIdPostCounts channelMessageIdPostCounts = jobManagementServiceImpl
                            .findByChannelIdAndUserIdAndTypeWithChannelMessageIdPostCounts(
                                    channelId, String.valueOf(message.getChatId()), "jobPosting");

                    if (isEdit) {
                        EditMessageText editMessageText = new EditMessageText();
                        editMessageText.setChatId(String.valueOf(channelId));
                        editMessageText.setText("招聘人才\n\n" + result + "\n\n 关注 @rc499 点击 @rc899Bot 发布");
                        editMessageText.setMessageId(channelMessageIdPostCounts.getMessageId());
                        editMessageText.setDisableWebPagePreview(true);
                        common.editResponseAsync(editMessageText);

                        response.setChatId(jobPosting.getUserId());
                        response.setText("[ "+ channelTitle +" ]编辑成功");
                        common.sendResponseAsync(response);
                        
                    } else {

                        if (channelMessageIdPostCounts == null) {
                            final Integer channelMessageId = common.sendResponseAsync(response);

                            response.setChatId(jobPosting.getUserId());
                            response.setText("[ "+ channelTitle +" ]发送成功");
                            common.sendResponseAsync(response);

                            channelMessageIdPostCounts = new ChannelMessageIdPostCounts();
                            channelMessageIdPostCounts.setBotId(jobPosting.getBotId());
                            channelMessageIdPostCounts.setUserId(jobPosting.getUserId());
                            channelMessageIdPostCounts.setChannelId(channelId);
                            channelMessageIdPostCounts.setChannelTitle(channelTitle);
                            channelMessageIdPostCounts.setChannelLink(channelLink);
                            channelMessageIdPostCounts.setMessageId(channelMessageId);
                            channelMessageIdPostCounts.setPostCount(1);
                            channelMessageIdPostCounts.setType("jobPosting");
                            jobPosting = jobManagementServiceImpl.findByUserIdAndBotIdWithJobPosting(
                                    String.valueOf(message.getChatId()), String.valueOf(common.getSpringyBotId()));
                            jobPosting.getChannelMessageIdPostCounts().add(channelMessageIdPostCounts);
                            jobManagementServiceImpl.saveJobPosting(jobPosting);
                        } else {
                            if (channelMessageIdPostCounts.getPostCount() <= 0) {
                                final Integer channelMessageId = common.sendResponseAsync(response);
                                response.setChatId(jobPosting.getUserId());
                                response.setText("[ "+ channelTitle +" ]发送 [招聘人才] 信息成功");
                                common.sendResponseAsync(response);
                                channelMessageIdPostCounts.setMessageId(channelMessageId);
                                channelMessageIdPostCounts.setPostCount(channelMessageIdPostCounts.getPostCount() + 1);
                                jobManagementServiceImpl.saveChannelMessageIdPostCounts(channelMessageIdPostCounts);
                            } else{
                                response.setChatId(jobPosting.getUserId());
                                response.setText("您已在[ "+ channelTitle + " ]發送一條 [招聘人才] 信息");
                                common.sendResponseAsync(response);
                            }
                        }

                    }

                }
            }

            Iterator<RobotGroupManagement> iterator_group = springyBot.getRobotGroupManagement().iterator();

            while (iterator_group.hasNext()) {
                RobotGroupManagement robotGroupManagement = iterator_group.next();
                if (!result.isEmpty()) {
                    SendMessage response = new SendMessage();
                    Long groupId = robotGroupManagement.getGroupId();
                    String groupTitle = robotGroupManagement.getGroupTitle();
                    String groupLink = robotGroupManagement.getLink();
                    response.setChatId(String.valueOf(groupId));
                    response.setText("招聘人才\n\n" + result + "\n\n 关注 @rc499 点击 @rc899Bot 发布");
                    response.setDisableNotification(true);
                    response.setDisableWebPagePreview(true);
                    GroupMessageIdPostCounts groupMessageIdPostCounts = jobManagementServiceImpl
                            .findByGroupIdAndTypeWithGroupMessageIdPostCounts(groupId, "jobPosting");
                    if (isEdit) {
                        EditMessageText editMessageText = new EditMessageText();
                        editMessageText.setChatId(String.valueOf(groupId));
                        editMessageText.setText("招聘人才\n\n" + result + "\n\n 关注 @rc499 点击 @rc899Bot 发布");
                        editMessageText.setMessageId(groupMessageIdPostCounts.getMessageId());
                        editMessageText.setDisableWebPagePreview(true);
                        common.editResponseAsync(editMessageText);

                        response.setChatId(jobPosting.getUserId());
                        response.setText("[ "+ groupTitle +" ]编辑成功");
                        common.sendResponseAsync(response);
                    } else {

                        if (groupMessageIdPostCounts == null) {
                            final Integer groupMessageId = common.sendResponseAsync(response);

                            response.setChatId(jobPosting.getUserId());
                            response.setText("[ "+ groupTitle +" ]发送 [招聘人才] 信息成功");
                            common.sendResponseAsync(response);

                            groupMessageIdPostCounts = new GroupMessageIdPostCounts();
                            groupMessageIdPostCounts.setBotId(jobPosting.getBotId());
                            groupMessageIdPostCounts.setUserId(jobPosting.getUserId());
                            groupMessageIdPostCounts.setGroupId(groupId);
                            groupMessageIdPostCounts.setGroupTitle(groupTitle);
                            groupMessageIdPostCounts.setGroupLink(groupLink);
                            groupMessageIdPostCounts.setMessageId(groupMessageId);
                            groupMessageIdPostCounts.setPostCount(1);
                            groupMessageIdPostCounts.setType("jobPosting");
                            jobPosting = jobManagementServiceImpl.findByUserIdAndBotIdWithJobPosting(
                                    String.valueOf(message.getChatId()), String.valueOf(common.getSpringyBotId()));
                            jobPosting.getGroupMessageIdPostCounts().add(groupMessageIdPostCounts);
                            jobManagementServiceImpl.saveJobPosting(jobPosting);
                        } else {
                            if (groupMessageIdPostCounts.getPostCount() == 0) {
                                final Integer groupMessageId = common.sendResponseAsync(response);

                                response.setChatId(jobPosting.getUserId());
                                response.setText("[ "+ groupTitle +" ]发送 [招聘人才] 成功");
                                common.sendResponseAsync(response);

                                groupMessageIdPostCounts.setMessageId(groupMessageId);
                                groupMessageIdPostCounts.setPostCount(groupMessageIdPostCounts.getPostCount() + 1);
                                jobManagementServiceImpl.saveGroupMessageIdPostCounts(groupMessageIdPostCounts);
                            }else{
                                response.setChatId(jobPosting.getUserId());
                                response.setText("您已在[ "+ groupTitle + " ]發送一條 [招聘人才] 信息");
                                common.sendResponseAsync(response);
                            }
                        }

                    }

                }
            }
        } else {
            SendMessage response = new SendMessage();
            response.setDisableNotification(true);
            response.setDisableWebPagePreview(true);
            common.sendResponseAsync(response);
        }
    }

    public void generateTextJobSeeker(Common common, Boolean isEdit) {
        this.common = common;

        Message message = common.getUpdate().getMessage();
        String text = message.getText();

        String[] lines = text.split("\\r?\\n");

        JobSeeker jobSeeker = jobManagementServiceImpl.findByUserIdAndBotIdWithJobSeeker(
                String.valueOf(message.getChatId()), String.valueOf(common.getSpringyBotId()));

        if (jobSeeker == null) {
            // 清除舊資料
            jobSeeker = new JobSeeker();

        }
        String isSuccess = fillJobSeekerInfo(jobSeeker, lines);
        if (!StringUtils.hasText(isSuccess)) {

            jobSeeker.setBotId(String.valueOf(common.getSpringyBotId()));
            jobSeeker.setUserId(String.valueOf(message.getChatId()));
            jobSeeker.setLastMessageId(message.getMessageId());

            // 處理資料表
            Long id = common.getSpringyBotId();
            String userId = String.valueOf(common.getUpdate().getMessage().getChatId());
            SpringyBot springyBot = springyBotServiceImpl.findById(id).orElseThrow();
            JobUser jobUser = springyBot.getJobUser().stream().filter(j -> j.getUserId().equals(userId)).findFirst()
                    .orElseGet(() -> {
                        JobUser ju = new JobUser();
                        ju.setUserId(userId);
                        springyBot.getJobUser().add(ju);
                        springyBotServiceImpl.save(springyBot);
                        return ju;
                    });

            final Long final_jobSeekerId = jobSeeker.getId();
            final JobSeeker final_jobSeeker = jobSeeker;

            if (!jobUser.getJobSeeker().stream().anyMatch(p -> p.getId().equals(final_jobSeekerId))) {
                jobUser.getJobSeeker().add(jobSeeker);
                springyBot.getJobUser().add(jobUser);
                jobManagementServiceImpl.saveJobSeeker(jobSeeker);
                springyBotServiceImpl.save(springyBot);
            } else {
                jobUser.getJobSeeker().stream().filter(jp -> jp.getId().equals(final_jobSeekerId)).findFirst()
                        .ifPresent(action -> {
                            jobManagementServiceImpl.saveJobSeeker(final_jobSeeker);
                        });
            }

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
            appendIfNotEmpty(sb, "✈️咨询飞机号：", jobSeeker.getFlightNumber());

            String result = sb.toString().trim();

            Iterator<RobotChannelManagement> iterator_channel = springyBot.getRobotChannelManagement().iterator();

            while (iterator_channel.hasNext()) {
                RobotChannelManagement robotChannelManagement = iterator_channel.next();
                if (!result.isEmpty()) {
                    SendMessage response = new SendMessage();
                    Long channelId = robotChannelManagement.getChannelId();
                    String channelTitle = robotChannelManagement.getChannelTitle();
                    String channelLink = robotChannelManagement.getLink();
                    response.setChatId(String.valueOf(channelId));
                    response.setText("求职人员\n\n" + result + "\n\n 关注 @rc499 点击 @rc899Bot 发布");
                    response.setDisableNotification(true);
                    response.setDisableWebPagePreview(true);
                    ChannelMessageIdPostCounts channelMessageIdPostCounts = jobManagementServiceImpl
                            .findByChannelIdAndUserIdAndTypeWithChannelMessageIdPostCounts(
                                    channelId, String.valueOf(message.getChatId()), "jobSeeker");

                    if (isEdit) {
                        EditMessageText editMessageText = new EditMessageText("求职人员\n\n" + result + "\n\n 关注 @rc499 点击 @rc899Bot 发布");
                        editMessageText.setChatId(String.valueOf(channelId));
                        editMessageText.setText("求职人员\n\n" + result + "\n\n 关注 @rc499 点击 @rc899Bot 发布");
                        editMessageText.setMessageId(channelMessageIdPostCounts.getMessageId());
                        editMessageText.setDisableWebPagePreview(true);
                        common.editResponseAsync(editMessageText);

                        response.setChatId(jobSeeker.getUserId());
                        response.setText("[ "+ channelTitle +" ]编辑成功");
                        common.sendResponseAsync(response);
                    } else {
                        if (channelMessageIdPostCounts == null) {
                            
                            final Integer channelMessageId = common.sendResponseAsync(response);
                            response.setChatId(jobSeeker.getUserId());
                            response.setText("[ "+ channelTitle +" ]发送 [求职人员] 成功");
                            common.sendResponseAsync(response);

                            channelMessageIdPostCounts = new ChannelMessageIdPostCounts();
                            channelMessageIdPostCounts.setBotId(jobSeeker.getBotId());
                            channelMessageIdPostCounts.setUserId(jobSeeker.getUserId());
                            channelMessageIdPostCounts.setChannelId(channelId);
                            channelMessageIdPostCounts.setChannelTitle(channelTitle);
                            channelMessageIdPostCounts.setChannelLink(channelLink);
                            channelMessageIdPostCounts.setMessageId(channelMessageId);
                            channelMessageIdPostCounts.setPostCount(1);
                            channelMessageIdPostCounts.setType("jobSeeker");

                            jobSeeker = jobManagementServiceImpl.findByUserIdAndBotIdWithJobSeeker(
                                    String.valueOf(message.getChatId()), String.valueOf(common.getSpringyBotId()));

                            jobSeeker.getChannelMessageIdPostCounts().add(channelMessageIdPostCounts);
                            jobManagementServiceImpl.saveJobSeeker(jobSeeker);
                        } else {
                            if (channelMessageIdPostCounts.getPostCount() == 0) {

                                final Integer channelMessageId = common.sendResponseAsync(response);
                                response.setChatId(jobSeeker.getUserId());
                                response.setText("[ "+ channelTitle +" ]发送 [求职人员] 成功");
                                common.sendResponseAsync(response);

                                channelMessageIdPostCounts.setMessageId(channelMessageId);
                                channelMessageIdPostCounts.setPostCount(channelMessageIdPostCounts.getPostCount() + 1);
                                jobManagementServiceImpl.saveChannelMessageIdPostCounts(channelMessageIdPostCounts);
                            } else {
                                response.setChatId(jobSeeker.getUserId());
                                response.setText("您已在[ "+ channelTitle + " ]發送一條 [求职人员] 信息");
                                common.sendResponseAsync(response);
                            }
                        }
                    }
                }
            }

            Iterator<RobotGroupManagement> iterator_group = springyBot.getRobotGroupManagement().iterator();

            while (iterator_group.hasNext()) {
                RobotGroupManagement robotGroupManagement = iterator_group.next();
                if (!result.isEmpty()) {
                    SendMessage response = new SendMessage();
                    Long groupId = robotGroupManagement.getGroupId();
                    String groupTitle = robotGroupManagement.getGroupTitle();
                    String groupLink = robotGroupManagement.getLink();
                    response.setChatId(String.valueOf(groupId));
                    response.setText("求职人员\n\n" + result + "\n\n 关注 @rc499 点击 @rc899Bot 发布");
                    response.setDisableNotification(true);
                    response.setDisableWebPagePreview(true);
                    GroupMessageIdPostCounts groupMessageIdPostCounts = jobManagementServiceImpl
                            .findByGroupIdAndTypeWithGroupMessageIdPostCounts(
                                    groupId, "jobSeeker");

                    if (isEdit) {
                        EditMessageText editMessageText = new EditMessageText();
                        editMessageText.setChatId(String.valueOf(groupId));
                        editMessageText.setText("求职人员\n\n" + result + "\n\n 关注 @rc499 点击 @rc899Bot 发布");
                        editMessageText.setMessageId(groupMessageIdPostCounts.getMessageId());
                        editMessageText.setDisableWebPagePreview(true);
                        common.editResponseAsync(editMessageText);

                        response.setChatId(jobSeeker.getUserId());
                        response.setText("[ "+ groupTitle +" ]编辑成功");
                        common.sendResponseAsync(response);

                    } else {
                        if (groupMessageIdPostCounts == null) {
                            final Integer groupMessageId = common.sendResponseAsync(response);
                            response.setChatId(jobSeeker.getUserId());
                            response.setText("[ "+ groupTitle +" ]发送 [求职人员] 成功");
                            common.sendResponseAsync(response);

                            groupMessageIdPostCounts = new GroupMessageIdPostCounts();
                            groupMessageIdPostCounts.setBotId(jobSeeker.getBotId());
                            groupMessageIdPostCounts.setUserId(jobSeeker.getUserId());
                            groupMessageIdPostCounts.setGroupId(groupId);
                            groupMessageIdPostCounts.setGroupTitle(groupTitle);
                            groupMessageIdPostCounts.setGroupLink(groupLink);
                            groupMessageIdPostCounts.setMessageId(groupMessageId);
                            groupMessageIdPostCounts.setPostCount(1);
                            groupMessageIdPostCounts.setType("jobSeeker");

                            jobSeeker = jobManagementServiceImpl.findByUserIdAndBotIdWithJobSeeker(
                                    String.valueOf(message.getChatId()), String.valueOf(common.getSpringyBotId()));

                            jobSeeker.getGroupMessageIdPostCounts().add(groupMessageIdPostCounts);
                            jobManagementServiceImpl.saveJobSeeker(jobSeeker);
                        } else {
                            if (groupMessageIdPostCounts.getPostCount() == 0) {

                                final Integer groupMessageId = common.sendResponseAsync(response);
                                response.setChatId(jobSeeker.getUserId());
                                response.setText("[ "+ groupTitle +" ]发送 [求职人员] 成功");
                                common.sendResponseAsync(response);

                                groupMessageIdPostCounts.setMessageId(groupMessageId);
                                groupMessageIdPostCounts.setPostCount(groupMessageIdPostCounts.getPostCount() + 1);
                                jobManagementServiceImpl.saveGroupMessageIdPostCounts(groupMessageIdPostCounts);
                            } else {
                                response.setChatId(jobSeeker.getUserId());
                                response.setText("您已在[ "+ groupTitle + " ]發送一條 [求职人员] 信息");
                                common.sendResponseAsync(response);
                            }
                        }
                    }
                }
            }
        } else {
            SendMessage response = new SendMessage();
            response.setChatId(jobSeeker.getUserId());
            response.setText(isSuccess);
            response.setDisableNotification(true);
            response.setDisableWebPagePreview(true);
            common.sendResponseAsync(response);
        }

    }

    private void appendIfNotEmpty(StringBuilder sb, String label, String value) {
        if (value != null && !value.isEmpty()) {
            sb.append(label).append(value).append("\n");
        }
    }

    public void setResponse_edit_jobPosting_management(Common common) {

        Long id = common.getSpringyBotId();
        String userId = String.valueOf(common.getUpdate().getMessage().getChatId());

        SendMessage response = new SendMessage();
        response.setChatId(userId);
        response.setDisableNotification(true);
        response.setDisableWebPagePreview(true);

        List<ChannelMessageIdPostCounts> channelMessageIdPostCounts = jobManagementServiceImpl
                .findAllByBotIdAndUserIdAndTypeWithChannelMessageIdPostCounts(String.valueOf(id),
                        userId, "jobPosting");

        List<String> alertMessages_channel = channelMessageIdPostCounts.stream().map(cmpc -> {
            String markdown = "频道 [ " + cmpc.getChannelTitle()+ " ] ";
            if (cmpc.getPostCount() <= 0) {
                return "";
            }
            return markdown + " 发布 " + cmpc.getPostCount() + " 则 [ 招聘人才 ] 信息 \n";
        }).filter(str -> !Objects.equals(str, "")).collect(Collectors.toList());

        List<GroupMessageIdPostCounts> groupMessageIdPostCounts = jobManagementServiceImpl
                .findAllByBotIdAndUserIdAndTypeWithGroupMessageIdPostCounts(String.valueOf(id),
                        userId, "jobPosting");

        List<String> alertMessages_group = groupMessageIdPostCounts.stream().map(gmpc -> {

            String markdown = "群组 [ " + gmpc.getGroupTitle()+" ] ";
            if (gmpc.getPostCount() <= 0) {
                return "";
            }
            return markdown + " 发布 " + gmpc.getPostCount() + " 则 [ 招聘人才 ] 信息 \n";
        }).filter(str -> !Objects.equals(str, "")).collect(Collectors.toList());

        // String alert_channel = String.join("\n", alertMessages_channel);
        // String alert_group = String.join("\n", alertMessages_group);
        String alert_channel = String.join("", alertMessages_channel);
        String alert_group = String.join("", alertMessages_group);
        String alert = alert_channel + alert_group;
        if (!alert.isEmpty()) {
            response.setText("通知：\n" + alert + "\n下方模版可对频道内信息进行编辑和删除操作");
            response.setDisableNotification(true);
            response.setDisableWebPagePreview(true);
            common.sendResponseAsync(response);

            JobPosting jobPosting = jobManagementServiceImpl.findByUserIdAndBotIdWithJobPosting(userId,
                    String.valueOf(id));

            SpringyBot springyBot = springyBotServiceImpl.findById(id).orElseThrow();
            JobUser jobUser = springyBot.getJobUser().stream().filter(j -> j.getUserId().equals(userId)).findFirst()
                    .orElseGet(() -> {
                        JobUser ju = new JobUser();
                        ju.setUserId(userId);
                        springyBot.getJobUser().add(ju);
                        return ju;
                    });

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
                response.setText(
                        "编辑招聘\n\n" + "公司：" + company + "\n" + "职位：" + position + "\n" + "底薪："
                                + baseSalary + "\n" + "提成：" + commission + "\n" + "上班时间："
                                + workTime + "\n" + "要求内容：" + requirements + "\n"
                                + "🐌地址：" + location + "\n" + "✈️咨询飞机号：" + flightNumber
                                + "\n\n 关注 @rc499 点击 @rc899Bot 发布");
                jobPostingDTO = new JobPostingDTO(userId, String.valueOf(id), company, position, baseSalary,
                        commission, workTime, requirements, location, flightNumber);
                response.setReplyMarkup(new KeyboardButton().keyboard_jobPosting(jobPostingDTO, true));
                response.setDisableNotification(true);
                response.setDisableWebPagePreview(true);
                Integer messageId = common.sendResponseAsync(response);
                jobPosting.setLastMessageId(messageId);
                jobUser.getJobPosting().add(jobPosting);
                jobManagementServiceImpl.saveJobPosting(jobPosting);
            } else {
                response.setText("编辑招聘\n\n" + "公司：\n" + "职位：\n" + "底薪：\n" + "提成：\n" + "上班时间：\n" + "要求内容：\n"
                        + "🐌地址：\n" + "✈️咨询飞机号：\n\n" + "关注 @rc499 点击 @rc899Bot 发布");
                response.setReplyMarkup(new KeyboardButton().keyboard_jobPosting(jobPostingDTO, false));
                response.setDisableNotification(true);
                response.setDisableWebPagePreview(true);

                JobPosting jp = new JobPosting(userId, String.valueOf(id),
                        common.sendResponseAsync(response));
                jobUser.getJobPosting().add(jp);
                jobManagementServiceImpl.saveJobPosting(jp);
                springyBotServiceImpl.save(springyBot);
            }

        } else {
            response.setText("未发布招聘");
            response.setDisableNotification(true);
            response.setDisableWebPagePreview(true);
            common.sendResponseAsync(response);

        }

    }

    public void setResponse_edit_jobSeeker_management(Common common) {

        Long id = common.getSpringyBotId();
        String userId = String.valueOf(common.getUpdate().getMessage().getChatId());

        SendMessage response = new SendMessage();
        response.setChatId(String.valueOf(common.getUpdate().getMessage().getChatId()));
        response.setDisableNotification(true);
        response.setDisableWebPagePreview(true);

        List<ChannelMessageIdPostCounts> channelMessageIdPostCounts = jobManagementServiceImpl
                .findAllByBotIdAndUserIdAndTypeWithChannelMessageIdPostCounts(String.valueOf(id),
                        userId, "jobSeeker");

        List<String> alertMessages_channel = channelMessageIdPostCounts.stream().map(cmpc -> {
            String markdown = "频道 [ " + cmpc.getChannelTitle() + " ] ";
            if (cmpc.getPostCount() <= 0) {
                return "";
            }
            return markdown + " 发布 " + cmpc.getPostCount() + " 则 [ 求职人员 ] 信息 \n";
        }).filter(str -> !Objects.equals(str, "")).collect(Collectors.toList());

        List<GroupMessageIdPostCounts> groupMessageIdPostCounts = jobManagementServiceImpl
                .findAllByBotIdAndUserIdAndTypeWithGroupMessageIdPostCounts(String.valueOf(id),
                        userId, "jobSeeker");

        List<String> alertMessages_group = groupMessageIdPostCounts.stream().map(gmpc -> {

            String markdown = "群组 [ " + gmpc.getGroupTitle() + " ] ";
            if (gmpc.getPostCount() <= 0) {
                return "";
            }
            return markdown + " 发布 " + gmpc.getPostCount() + " 则 [ 求职人员 ] 信息 \n";
        }).filter(str -> !Objects.equals(str, "")).collect(Collectors.toList());

        String alert_channel = String.join("", alertMessages_channel);
        String alert_group = String.join("", alertMessages_group);
        // String alert_channel = String.join("\n", alertMessages_channel);
        // String alert_group = String.join("\n", alertMessages_group);
        String alert = alert_channel + alert_group;

        if (!alert.isEmpty()) {

            // response.enableMarkdown(true);
            response.setText("通知：\n" + alert + "\n下方模版可对频道内信息进行编辑和删除操作");
            response.setDisableNotification(true);
            response.setDisableWebPagePreview(true);
            common.sendResponseAsync(response);
            // response.enableMarkdown(false);

            JobSeeker jobSeeker = jobManagementServiceImpl.findByUserIdAndBotIdWithJobSeeker(userId,
                    String.valueOf(id));

            SpringyBot springyBot = springyBotServiceImpl.findById(id).orElseThrow();

            JobUser jobUser = springyBot.getJobUser().stream().filter(j -> j.getUserId().equals(userId)).findFirst()
                    .orElseGet(() -> {
                        JobUser ju = new JobUser();
                        ju.setUserId(userId);
                        springyBot.getJobUser().add(ju);
                        return ju;
                    });

            JobSeekerDTO jobSeekerDTO = new JobSeekerDTO(userId, String.valueOf(id));
            String name = "", gender = "", dateOfBirth = "", age = "", nationality = "", education = "",
                    skills = "", targetPosition = "", resources = "", expectedSalary = "",
                    workExperience = "", selfIntroduction = "", flightNumber = "";

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

                jobSeekerDTO = new JobSeekerDTO(userId, String.valueOf(id), name, gender, dateOfBirth, age,
                        nationality, education, skills, targetPosition, resources, expectedSalary,
                        workExperience, selfIntroduction, flightNumber);

                response.setText("编辑求职\n\n姓名：" + name + "\n男女：" + gender + "\n出生_年_月_日："
                        + dateOfBirth
                        + "\n年龄：" + age + "\n国籍：" + nationality + "\n学历：" + education
                        + "\n技能：" + skills + "\n目标职位：" + targetPosition + "\n手上有什么资源："
                        + resources + "\n期望薪资：" + expectedSalary + "\n工作经历："
                        + workExperience + "\n自我介绍：" + selfIntroduction + "\n✈️咨询飞机号：" + flightNumber
                        + "\n\n 关注 @rc499 点击 @rc899Bot 发布");
                response.setReplyMarkup(new KeyboardButton().keyboard_jobSeeker(jobSeekerDTO, true));
                response.setDisableNotification(true);
                response.setDisableWebPagePreview(true);
                // response.setReplyMarkup(new
                // KeyboardButton().keyboard_editJobSeeker(jobSeekerDTO));
                Integer messageId = common.sendResponseAsync(response);
                jobSeeker.setLastMessageId(messageId);
                jobUser.getJobSeeker().add(jobSeeker);
                jobManagementServiceImpl.saveJobSeeker(jobSeeker);

            } else {
                response.setText(
                        "编辑求职\n\n姓名：\n男女：\n出生_年_月_日：\n年龄：\n国籍：\n学历：\n技能：\n目标职位：\n手上有什么资源：\n期望薪资：\n工作经历：\n自我介绍：\n✈️咨询飞机号：\n\n 关注 @rc499 点击 @rc899Bot 发布");
                response.setReplyMarkup(new KeyboardButton().keyboard_jobSeeker(jobSeekerDTO, true));
                // response.setReplyMarkup(new
                // KeyboardButton().keyboard_jobSeeker(jobSeekerDTO));
                response.setDisableNotification(true);
                response.setDisableWebPagePreview(true);
                JobSeeker js = new JobSeeker(userId, String.valueOf(id),
                        common.sendResponseAsync(response));
                jobUser.getJobSeeker().add(js);
                jobManagementServiceImpl.saveJobSeeker(js);
                springyBotServiceImpl.save(springyBot);
            }

        } else {
            response.setText("未发布求职");
            response.setDisableNotification(true);
            response.setDisableWebPagePreview(true);
            common.sendResponseAsync(response);
        }
    }

    private String fillJobSeekerInfo(JobSeeker jobSeeker, String[] lines) {
        String returnStr = "";
        for (String line : lines) {
            int colonIndex = line.indexOf("：");
            if (colonIndex >= 0) {
                String key = line.substring(0, colonIndex).trim();
                String value = line.substring(colonIndex + 1).trim();
                if(value.startsWith("（限50字以内）")){
                    value = value.substring(8);
                }
                switch (key) {
                    case "姓名":
                        jobSeeker.setName(value);
                        break;
                    case "男女":
                        jobSeeker.setGender(value);
                        break;
                    case "出生_年_月_日":
                        jobSeeker.setDateOfBirth(value);
                        break;
                    case "年龄":
                        jobSeeker.setAge(value);
                        break;
                    case "国籍":
                        jobSeeker.setNationality(value);
                        break;
                    case "学历":
                        jobSeeker.setEducation(value);
                        break;
                    case "技能":
                        jobSeeker.setSkills(value);
                        break;
                    case "目标职位":
                        jobSeeker.setTargetPosition(value);
                        break;
                    case "手上有什么资源":
                        jobSeeker.setResources(value);
                        break;
                    case "期望薪资":
                        jobSeeker.setExpectedSalary(value);
                        break;
                    case "工作经历":
                        if (value.length() >= 50) {
                            returnStr = "发送失败,工作经历超过50字";
                        }
                        jobSeeker.setWorkExperience(value);
                        break;
                    case "自我介绍":
                        if (value.length() >= 50) {
                            returnStr = "发送失败,自我介绍超过50字";
                        }
                        jobSeeker.setSelfIntroduction(value);
                        break;
                    case "✈️咨询飞机号":
                        jobSeeker.setFlightNumber(value);
                        break;
                    default:
                        // 未知键值对，可以忽略或抛出异常
                        SendMessage response = new SendMessage(jobSeeker.getUserId(), "过滤模版外键值 " + key);
                        this.common.sendResponseAsync(response);
                        break;
                }
            }
        }
        return returnStr;
    }

    private String fillJobPostingInfo(JobPosting jobPosting, String[] lines) {
        String returnStr = "";
        for (String line : lines) {
            int colonIndex = line.indexOf("：");
            if (colonIndex >= 0) {
                String key = line.substring(0, colonIndex).trim();
                String value = line.substring(colonIndex + 1).trim();
                if(value.startsWith("（限50字以内）")){
                    value = value.substring(8);
                }
                switch (key) {
                    case "公司":
                        jobPosting.setCompany(value);
                        break;
                    case "职位":
                        jobPosting.setPosition(value);
                        break;
                    case "底薪":
                        jobPosting.setBaseSalary(value);
                        break;
                    case "提成":
                        jobPosting.setCommission(value);
                        break;
                    case "上班时间":
                        jobPosting.setWorkTime(value);
                        break;
                    case "要求内容":
                        if (value.length() >= 50) {
                            returnStr = "发送失败,要求内容超过50字";
                        }
                        jobPosting.setRequirements(value);
                        break;
                    case "🐌地址":
                        jobPosting.setLocation(value);
                        break;
                    case "✈️咨询飞机号":
                        jobPosting.setFlightNumber(value);
                        break;
                    default:
                        // 未知键值对，可以忽略或抛出异常
                        SendMessage response = new SendMessage(jobPosting.getUserId(), "过滤模版外键值 " + key);
                        this.common.sendResponseAsync(response);
                        break;
                }
            }
        }
        return returnStr;
    }
}
