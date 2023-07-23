package com.lwdevelop.bot.bots.talent.messageHandling.commands;

import java.util.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import com.lwdevelop.bot.bots.talent.messageHandling.PrivateMessage_;
import com.lwdevelop.bot.bots.utils.Common;
import com.lwdevelop.bot.bots.utils.enum_.TelentEnum;
import com.lwdevelop.bot.bots.utils.keyboardButton.TelentButton;
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
import com.lwdevelop.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

@Slf4j
public class Job extends PrivateMessage_ {

    @Autowired
    private JobManagementServiceImpl jobManagementServiceImpl = SpringUtils.getApplicationContext()
            .getBean(JobManagementServiceImpl.class);

    private JobPostingDTO jobPostingDTO;
    private JobSeekerDTO jobSeekerDTO;

    public Job(Common common) {
        super(common);
        this.jobPostingDTO = new JobPostingDTO(common);
        this.jobSeekerDTO = new JobSeekerDTO(common);
        this.saveJobUser();
    }

    private void saveJobUser() {
        String firstname = Optional.ofNullable(common.getUpdate().getMessage().getChat().getFirstName()).orElse("");
        String username = Optional.ofNullable(common.getUpdate().getMessage().getChat().getUserName()).orElse("");
        String lastname = Optional.ofNullable(common.getUpdate().getMessage().getChat().getLastName()).orElse("");
        SpringyBot springyBot = springyBotServiceImpl.findById(springyBotId).orElseThrow();
        List<JobUser> jobUserList = springyBotServiceImpl.findJobUserBySpringyBotId(springyBotId);

        jobUserList.stream().filter(j -> j.getUserId().equals(chatId_str)).findAny()
                .ifPresentOrElse(ju -> {
                    ju.setFirstname(firstname);
                    ju.setLastname(lastname);
                    ju.setUsername(username);
                }, () -> {
                    JobUser jobUser = new JobUser();
                    jobUser.setUserId(chatId_str);
                    jobUser.setFirstname(firstname);
                    jobUser.setLastname(lastname);
                    jobUser.setUsername(username);
                    jobUserList.add(jobUser);
                });
        springyBot.setJobUser(jobUserList);
        springyBotServiceImpl.save(springyBot);
    }

    public void setResponse_jobPosting_management() {
        log.info("Entering setResponse_jobPosting_management method...");
        List<ChannelMessageIdPostCounts> channelMessageIdPostCounts = jobManagementServiceImpl
                .findAllByBotIdAndUserIdAndTypeWithChannelMessageIdPostCounts(
                        springyBotId.toString(), chatId_str, "jobPosting");
        channelMessageIdPostCounts.stream().filter(cmpc -> cmpc.getPostCount() >= 1).findAny()
                .ifPresentOrElse(action -> {
                    SendMessage response = new SendMessage(chatId_str, "您已经发布过[招聘人才]信息，请点选[招聘和求职信息管理]进行编辑或删除信息后重新发布。");
                    common.executeAsync(response);
                }, () -> {
                    SendMessage response = new SendMessage();
                    response.setChatId(chatId_str);
                    JobPosting jobPosting = jobManagementServiceImpl.findByUserIdAndBotIdWithJobPosting(chatId_str,
                            springyBotId.toString());
                    // 沒有發布過
                    if (jobPosting == null) {
                        response.setText(TelentEnum.JOBPOSTING_DEFAULT_FORM.getText());
                        log.info("No job posting found for user {}, bot id {}", chatId_str, springyBotId);
                    } else {
                        response.setText(jobPostingDTO.generateJobPostingResponse(jobPosting, false));
                        log.info("Job posting found for user {}, bot id {}: {}", chatId_str, springyBotId, jobPosting);
                    }
                    common.executeAsync(response);
                    response.setText(TelentEnum.REMIND_EDITOR_.getText());
                    common.executeAsync(response);
                });
    }

    public void setResponse_jobSeeker_management() {
        log.info("Entering setResponse_jobSeeker_management method...");
        List<ChannelMessageIdPostCounts> channelMessageIdPostCounts = jobManagementServiceImpl
                .findAllByBotIdAndUserIdAndTypeWithChannelMessageIdPostCounts(
                        springyBotId.toString(), chatId_str, "jobSeeker");
        channelMessageIdPostCounts.stream().filter(cmpc -> cmpc.getPostCount() >= 1).findAny()
                .ifPresentOrElse(action -> {
                    SendMessage response = new SendMessage(chatId_str, "您已经发布过[求职人员]信息，请点选[招聘和求职信息管理]进行编辑或删除信息后重新发布。");
                    common.executeAsync(response);
                }, () -> {
                    SendMessage response = new SendMessage();
                    response.setChatId(chatId_str);
                    JobSeeker jobSeeker = jobManagementServiceImpl.findByUserIdAndBotIdWithJobSeeker(chatId_str,
                            springyBotId.toString());
                    if (jobSeeker == null) {
                        response.setText(TelentEnum.JOBSEEKER_DEFAULT_FORM.getText());
                        log.info("No job seeker found for user {}, bot id {}", chatId_str, springyBotId);
                    } else {
                        response.setText(jobSeekerDTO.generateJobSeekerResponse(jobSeeker, false));
                        log.info("Job seeker found for user {}, bot id {}: {}", chatId_str, springyBotId, jobSeeker);

                    }
                    common.executeAsync(response);
                    response.setText(TelentEnum.REMIND_EDITOR_.getText());
                    common.executeAsync(response);
                });

    }

    public void generateTextJobPosting(Boolean isEdit) {
        // 将文本内容按行分割成字符串数组
        String[] lines = text.split("\\r?\\n");

        JobPosting jobPosting = jobManagementServiceImpl.findByUserIdAndBotIdWithJobPosting(
                chatId_str, springyBotId.toString());

        if (jobPosting == null) {
            jobPosting = new JobPosting();
        }

        String username = "@" + common.getUpdate().getMessage().getFrom().getUserName();
        jobPosting.setPublisher(username);

        String isSuccess = jobPostingDTO.fillJobPostingInfo(jobPosting, lines);

        if (!StringUtils.hasText(isSuccess)) {
            jobPosting.setBotId(springyBotId.toString());
            jobPosting.setUserId(chatId_str);
            jobPosting.setLastMessageId(message.getMessageId());
            // 處理資料表
            List<JobUser> jobUsers = springyBotServiceImpl.findJobUserBySpringyBotId(springyBotId);
            JobUser jobUser = jobUsers.stream().filter(j -> j.getUserId().equals(chatId_str)).findAny().get();
            final Long final_jobPostingId = jobPosting.getId();

            if (!jobUser.getJobPosting().stream().anyMatch(p -> p.getId().equals(final_jobPostingId))) {
                jobUser.getJobPosting().add(jobPosting);
            }

            jobManagementServiceImpl.saveJobPosting(jobPosting);

            String result = jobPostingDTO.generateJobPostingDetails(jobPosting);

            List<RobotChannelManagement> robotChannelManagements = springyBotServiceImpl
                    .findRobotChannelManagementBySpringyBotId(springyBotId);

            Iterator<RobotChannelManagement> iterator_channel = robotChannelManagements.iterator();

            while (iterator_channel.hasNext()) {
                RobotChannelManagement robotChannelManagement = iterator_channel.next();
                if (!result.isEmpty()) {
                    SendMessage response = new SendMessage();
                    Long channelId = robotChannelManagement.getChannelId();
                    String channelTitle = robotChannelManagement.getChannelTitle();
                    response.setChatId(channelId.toString());
                    response.setText(TelentEnum.send_recruitment_text(result));
                    response.setReplyMarkup(new TelentButton().keyboardJobMarkup());
                    response.setDisableWebPagePreview(true);
                    ChannelMessageIdPostCounts channelMessageIdPostCounts = jobManagementServiceImpl
                            .findByChannelIdAndUserIdAndTypeWithChannelMessageIdPostCounts(
                                    channelId, chatId_str, "jobPosting");

                    if (isEdit) {
                        EditMessageText editMessageText = new EditMessageText();
                        editMessageText.setChatId(String.valueOf(channelId));
                        editMessageText.setText(TelentEnum.send_recruitment_text(result));
                        editMessageText.setMessageId(channelMessageIdPostCounts.getMessageId());
                        editMessageText.setDisableWebPagePreview(true);
                        common.executeAsync(editMessageText);

                        response.setChatId(chatId_str);
                        response.setText("[ " + channelTitle + " ]编辑成功");
                        common.executeAsync(response);

                    } else {

                        if (channelMessageIdPostCounts == null) {
                            final Integer channelMessageId = common.executeAsync(response);

                            response.setChatId(chatId_str);
                            response.setText("[ " + channelTitle + " ]发送成功");
                            common.executeAsync(response);

                            channelMessageIdPostCounts = new ChannelMessageIdPostCounts();
                            channelMessageIdPostCounts.setBotId(springyBotId.toString());
                            channelMessageIdPostCounts.setUserId(chatId_str);
                            channelMessageIdPostCounts.setChannelId(channelId);
                            channelMessageIdPostCounts.setChannelTitle(channelTitle);
                            channelMessageIdPostCounts.setMessageId(channelMessageId);
                            channelMessageIdPostCounts.setPostCount(1);
                            channelMessageIdPostCounts.setType("jobPosting");
                            jobPosting = jobManagementServiceImpl.findByUserIdAndBotIdWithJobPosting(
                                    chatId_str, springyBotId.toString());
                            jobPosting.getChannelMessageIdPostCounts().add(channelMessageIdPostCounts);
                            jobManagementServiceImpl.saveJobPosting(jobPosting);
                        } else {
                            if (channelMessageIdPostCounts.getPostCount() <= 0) {
                                final Integer channelMessageId = common.executeAsync(response);
                                response.setChatId(chatId_str);
                                response.setText("[ " + channelTitle + " ]发送 [招聘人才] 信息成功");
                                common.executeAsync(response);
                                channelMessageIdPostCounts.setMessageId(channelMessageId);
                                channelMessageIdPostCounts.setPostCount(channelMessageIdPostCounts.getPostCount() + 1);
                                jobManagementServiceImpl.saveChannelMessageIdPostCounts(channelMessageIdPostCounts);
                            } else {
                                response.setChatId(chatId_str);
                                response.setText("您已在[ " + channelTitle + " ]發送一條 [招聘人才] 信息");
                                common.executeAsync(response);
                            }
                        }

                    }

                }
            }

            List<RobotGroupManagement> robotGroupManagements = springyBotServiceImpl
                    .findRobotGroupManagementBySpringyBotId(springyBotId);

            Iterator<RobotGroupManagement> iterator_group = robotGroupManagements.iterator();

            while (iterator_group.hasNext()) {
                RobotGroupManagement robotGroupManagement = iterator_group.next();
                if (!result.isEmpty()) {
                    SendMessage response = new SendMessage();
                    Long groupId = robotGroupManagement.getGroupId();
                    String groupTitle = robotGroupManagement.getGroupTitle();
                    response.setChatId(groupId.toString());
                    response.setText(TelentEnum.send_recruitment_text(result));
                    response.setReplyMarkup(new TelentButton().keyboardJobMarkup());
                    response.setDisableWebPagePreview(true);

                    GroupMessageIdPostCounts groupMessageIdPostCounts = jobManagementServiceImpl
                            .findByGroupIdAndUserIdAndTypeWithGroupMessageIdPostCounts(groupId,chatId_str, "jobPosting");
                    if (isEdit) {
                        EditMessageText editMessageText = new EditMessageText();
                        editMessageText.setChatId(groupId.toString());
                        editMessageText.setText(TelentEnum.send_recruitment_text(result));
                        editMessageText.setMessageId(groupMessageIdPostCounts.getMessageId());
                        editMessageText.setDisableWebPagePreview(true);
                        common.executeAsync(editMessageText);

                        response.setChatId(chatId_str);
                        response.setText("[ " + groupTitle + " ]编辑成功");
                        common.executeAsync(response);
                    } else {

                        if (groupMessageIdPostCounts == null) {
                            final Integer groupMessageId = common.executeAsync(response);
                            response.setChatId(chatId_str);
                            response.setText("[ " + groupTitle + " ]发送 [招聘人才] 信息成功");
                            common.executeAsync(response);

                            groupMessageIdPostCounts = new GroupMessageIdPostCounts();
                            groupMessageIdPostCounts.setBotId(springyBotId.toString());
                            groupMessageIdPostCounts.setUserId(chatId_str);
                            groupMessageIdPostCounts.setGroupId(groupId);
                            groupMessageIdPostCounts.setGroupTitle(groupTitle);
                            groupMessageIdPostCounts.setMessageId(groupMessageId);
                            groupMessageIdPostCounts.setPostCount(1);
                            groupMessageIdPostCounts.setType("jobPosting");
                            jobPosting = jobManagementServiceImpl.findByUserIdAndBotIdWithJobPosting(
                                    chatId_str, springyBotId.toString());
                            jobPosting.getGroupMessageIdPostCounts().add(groupMessageIdPostCounts);
                            jobManagementServiceImpl.saveJobPosting(jobPosting);
                        } else {
                            if (groupMessageIdPostCounts.getPostCount() == 0) {
                                final Integer groupMessageId = common.executeAsync(response);
                                response.setChatId(chatId_str);
                                response.setText("[ " + groupTitle + " ]发送 [招聘人才] 成功");
                                common.executeAsync(response);

                                groupMessageIdPostCounts.setMessageId(groupMessageId);
                                groupMessageIdPostCounts.setPostCount(groupMessageIdPostCounts.getPostCount() + 1);
                                jobManagementServiceImpl.saveGroupMessageIdPostCounts(groupMessageIdPostCounts);
                            } else {
                                response.setChatId(chatId_str);
                                response.setText("您已在[ " + groupTitle + " ]發送一條 [招聘人才] 信息");
                                common.executeAsync(response);
                            }
                        }

                    }

                }
            }
        } else {
            SendMessage response = new SendMessage();
            response.setChatId(chatId_str);
            response.setText(isSuccess);
            response.setDisableWebPagePreview(true);
            common.executeAsync(response);
        }
    }

    public void generateTextJobSeeker(Boolean isEdit) {

        String[] lines = text.split("\\r?\\n");

        JobSeeker jobSeeker = jobManagementServiceImpl.findByUserIdAndBotIdWithJobSeeker(
                chatId_str, springyBotId.toString());

        if (jobSeeker == null) {
            // 清除舊資料
            jobSeeker = new JobSeeker();
        }

        String username = "@" + common.getUpdate().getMessage().getFrom().getUserName();
        jobSeeker.setPublisher(username);

        String isSuccess = jobSeekerDTO.fillJobSeekerInfo(jobSeeker, lines);
        if (!StringUtils.hasText(isSuccess)) {

            jobSeeker.setBotId(springyBotId.toString());
            jobSeeker.setUserId(chatId_str);
            jobSeeker.setLastMessageId(message.getMessageId());

            // 處理資料表
            List<JobUser> jobUsers = springyBotServiceImpl.findJobUserBySpringyBotId(springyBotId);
            JobUser jobUser = jobUsers.stream().filter(j -> j.getUserId().equals(chatId_str)).findAny().get();

            final Long final_jobSeekerId = jobSeeker.getId();

            if (!jobUser.getJobSeeker().stream().anyMatch(p -> p.getId().equals(final_jobSeekerId))) {
                jobUser.getJobSeeker().add(jobSeeker);
            }
            jobManagementServiceImpl.saveJobSeeker(jobSeeker);

            String result = jobSeekerDTO.generateJobSeekerDetails(jobSeeker);

            List<RobotChannelManagement> robotChannelManagements = springyBotServiceImpl
                    .findRobotChannelManagementBySpringyBotId(springyBotId);
            Iterator<RobotChannelManagement> iterator_channel = robotChannelManagements.iterator();

            while (iterator_channel.hasNext()) {
                RobotChannelManagement robotChannelManagement = iterator_channel.next();
                if (!result.isEmpty()) {
                    SendMessage response = new SendMessage();
                    Long channelId = robotChannelManagement.getChannelId();
                    String channelTitle = robotChannelManagement.getChannelTitle();
                    String channelLink = robotChannelManagement.getLink();
                    response.setChatId(channelId.toString());
                    response.setText(TelentEnum.send_jobsearch_text(result));
                    response.setReplyMarkup(new TelentButton().keyboardJobMarkup());
                    response.setDisableWebPagePreview(true);
                    ChannelMessageIdPostCounts channelMessageIdPostCounts = jobManagementServiceImpl
                            .findByChannelIdAndUserIdAndTypeWithChannelMessageIdPostCounts(
                                    channelId, chatId_str, "jobSeeker");

                    if (isEdit) {
                        EditMessageText editMessageText = new EditMessageText();
                        editMessageText.setChatId(channelId.toString());
                        editMessageText.setText(TelentEnum.send_jobsearch_text(result));
                        editMessageText.setMessageId(channelMessageIdPostCounts.getMessageId());
                        editMessageText.setDisableWebPagePreview(true);
                        common.executeAsync(editMessageText);

                        response.setChatId(chatId_str);
                        response.setText("[ " + channelTitle + " ]编辑成功");
                        common.executeAsync(response);
                    } else {
                        if (channelMessageIdPostCounts == null) {

                            final Integer channelMessageId = common.executeAsync(response);
                            response.setChatId(chatId_str);
                            response.setText("[ " + channelTitle + " ]发送 [求职人员] 成功");
                            common.executeAsync(response);

                            channelMessageIdPostCounts = new ChannelMessageIdPostCounts();
                            channelMessageIdPostCounts.setBotId(springyBotId.toString());
                            channelMessageIdPostCounts.setUserId(chatId_str);
                            channelMessageIdPostCounts.setChannelId(channelId);
                            channelMessageIdPostCounts.setChannelTitle(channelTitle);
                            channelMessageIdPostCounts.setChannelLink(channelLink);
                            channelMessageIdPostCounts.setMessageId(channelMessageId);
                            channelMessageIdPostCounts.setPostCount(1);
                            channelMessageIdPostCounts.setType("jobSeeker");

                            jobSeeker = jobManagementServiceImpl.findByUserIdAndBotIdWithJobSeeker(
                                    chatId_str, springyBotId.toString());

                            jobSeeker.getChannelMessageIdPostCounts().add(channelMessageIdPostCounts);
                            jobManagementServiceImpl.saveJobSeeker(jobSeeker);
                        } else {
                            if (channelMessageIdPostCounts.getPostCount() == 0) {

                                final Integer channelMessageId = common.executeAsync(response);
                                response.setChatId(chatId_str);
                                response.setText("[ " + channelTitle + " ]发送 [求职人员] 成功");
                                common.executeAsync(response);

                                channelMessageIdPostCounts.setMessageId(channelMessageId);
                                channelMessageIdPostCounts.setPostCount(channelMessageIdPostCounts.getPostCount() + 1);
                                jobManagementServiceImpl.saveChannelMessageIdPostCounts(channelMessageIdPostCounts);
                            } else {
                                response.setChatId(chatId_str);
                                response.setText("您已在[ " + channelTitle + " ]發送一條 [求职人员] 信息");
                                common.executeAsync(response);
                            }
                        }
                    }
                }
            }

            List<RobotGroupManagement> robotGroupManagements = springyBotServiceImpl
                    .findRobotGroupManagementBySpringyBotId(springyBotId);
            Iterator<RobotGroupManagement> iterator_group = robotGroupManagements.iterator();

            while (iterator_group.hasNext()) {
                RobotGroupManagement robotGroupManagement = iterator_group.next();
                if (!result.isEmpty()) {
                    SendMessage response = new SendMessage();
                    Long groupId = robotGroupManagement.getGroupId();
                    String groupTitle = robotGroupManagement.getGroupTitle();
                    String groupLink = robotGroupManagement.getLink();
                    response.setChatId(groupId.toString());
                    response.setText(TelentEnum.send_jobsearch_text(result));
                    response.setReplyMarkup(new TelentButton().keyboardJobMarkup());
                    response.setDisableWebPagePreview(true);
                    GroupMessageIdPostCounts groupMessageIdPostCounts = jobManagementServiceImpl
                            .findByGroupIdAndUserIdAndTypeWithGroupMessageIdPostCounts(
                                    groupId, chatId_str,"jobSeeker");

                    if (isEdit) {
                        EditMessageText editMessageText = new EditMessageText();
                        editMessageText.setChatId(groupId.toString());
                        editMessageText.setText(TelentEnum.send_jobsearch_text(result));
                        editMessageText.setMessageId(groupMessageIdPostCounts.getMessageId());
                        editMessageText.setDisableWebPagePreview(true);
                        common.executeAsync(editMessageText);

                        response.setChatId(chatId_str);
                        response.setText("[ " + groupTitle + " ]编辑成功");
                        common.executeAsync(response);

                    } else {
                        if (groupMessageIdPostCounts == null) {
                            final Integer groupMessageId = common.executeAsync(response);
                            response.setChatId(chatId_str);
                            response.setText("[ " + groupTitle + " ]发送 [求职人员] 成功");
                            common.executeAsync(response);

                            groupMessageIdPostCounts = new GroupMessageIdPostCounts();
                            groupMessageIdPostCounts.setBotId(jobSeeker.getBotId());
                            groupMessageIdPostCounts.setUserId(chatId_str);
                            groupMessageIdPostCounts.setGroupId(groupId);
                            groupMessageIdPostCounts.setGroupTitle(groupTitle);
                            groupMessageIdPostCounts.setGroupLink(groupLink);
                            groupMessageIdPostCounts.setMessageId(groupMessageId);
                            groupMessageIdPostCounts.setPostCount(1);
                            groupMessageIdPostCounts.setType("jobSeeker");

                            jobSeeker = jobManagementServiceImpl.findByUserIdAndBotIdWithJobSeeker(
                                    chatId_str, springyBotId.toString());

                            jobSeeker.getGroupMessageIdPostCounts().add(groupMessageIdPostCounts);
                            jobManagementServiceImpl.saveJobSeeker(jobSeeker);
                        } else {
                            if (groupMessageIdPostCounts.getPostCount() == 0) {

                                final Integer groupMessageId = common.executeAsync(response);
                                response.setChatId(chatId_str);
                                response.setText("[ " + groupTitle + " ]发送 [求职人员] 成功");
                                common.executeAsync(response);

                                groupMessageIdPostCounts.setMessageId(groupMessageId);
                                groupMessageIdPostCounts.setPostCount(groupMessageIdPostCounts.getPostCount() + 1);
                                jobManagementServiceImpl.saveGroupMessageIdPostCounts(groupMessageIdPostCounts);
                            } else {
                                response.setChatId(chatId_str);
                                response.setText("您已在[ " + groupTitle + " ]發送一條 [求职人员] 信息");
                                common.executeAsync(response);
                            }
                        }
                    }
                }
            }
        } else {
            SendMessage response = new SendMessage();
            response.setChatId(chatId_str);
            response.setText(isSuccess);
            response.setDisableWebPagePreview(true);
            common.executeAsync(response);
        }

    }

    public void setResponse_edit_jobPosting_management() {

        SendMessage response = new SendMessage();
        response.setChatId(chatId_str);
        response.setDisableWebPagePreview(true);

        List<ChannelMessageIdPostCounts> channelMessageIdPostCounts = jobManagementServiceImpl
                .findAllByBotIdAndUserIdAndTypeWithChannelMessageIdPostCounts(springyBotId.toString(),
                        chatId_str, "jobPosting");

        List<String> alertMessages_channel = channelMessageIdPostCounts.stream().map(cmpc -> {
            String markdown = "频道 [ " + cmpc.getChannelTitle() + " ] ";
            if (cmpc.getPostCount() <= 0) {
                return "";
            }
            return markdown + " 发布 " + cmpc.getPostCount() + " 则 [ 招聘人才 ] 信息 \n";
        }).filter(str -> !Objects.equals(str, "")).collect(Collectors.toList());

        List<GroupMessageIdPostCounts> groupMessageIdPostCounts = jobManagementServiceImpl
                .findAllByBotIdAndUserIdAndTypeWithGroupMessageIdPostCounts(springyBotId.toString(),
                        chatId_str, "jobPosting");

        List<String> alertMessages_group = groupMessageIdPostCounts.stream().map(gmpc -> {

            String markdown = "群组 [ " + gmpc.getGroupTitle() + " ] ";
            if (gmpc.getPostCount() <= 0) {
                return "";
            }
            return markdown + " 发布 " + gmpc.getPostCount() + " 则 [ 招聘人才 ] 信息 \n";
        }).filter(str -> !Objects.equals(str, "")).collect(Collectors.toList());

        String alert_channel = String.join("", alertMessages_channel);
        String alert_group = String.join("", alertMessages_group);
        String alert = alert_channel + alert_group;
        if (!alert.isEmpty()) {
            response.setText("通知：\n" + alert + "\n下方模版可对频道内信息进行编辑和删除操作");
            response.setDisableWebPagePreview(true);
            common.executeAsync(response);

            JobPosting jobPosting = jobManagementServiceImpl.findByUserIdAndBotIdWithJobPosting(chatId_str,
                    springyBotId.toString());

            SpringyBot springyBot = springyBotServiceImpl.findById(springyBotId).orElseThrow();
            List<JobUser> jobUsers = springyBotServiceImpl.findJobUserBySpringyBotId(springyBotId);
            JobUser jobUser = jobUsers.stream().filter(j -> j.getUserId().equals(chatId_str)).findAny().get();

            JobPostingDTO jobPostingDTO = new JobPostingDTO(chatId_str, springyBotId.toString());

            if (jobPosting != null) {

                response.setText(jobPostingDTO.generateJobPostingResponse(jobPosting, true));
                jobPostingDTO = jobPostingDTO.createJobPostingDTO(chatId_str, springyBot.toString());

                response.setReplyMarkup(new TelentButton().keyboard_jobPosting(jobPostingDTO, true));
                response.setDisableWebPagePreview(true);
                Integer messageId = common.executeAsync(response);
                jobPosting.setLastMessageId(messageId);
                jobUser.getJobPosting().add(jobPosting);
                jobManagementServiceImpl.saveJobPosting(jobPosting);
            } else {
                response.setText(TelentEnum.JOBPOSTING_EDITOR_DEFAULT_FORM.getText());
                response.setReplyMarkup(new TelentButton().keyboard_jobPosting(jobPostingDTO, false));
                response.setDisableWebPagePreview(true);

                JobPosting jp = new JobPosting(chatId_str, springyBot.toString(),
                        common.executeAsync(response));
                jobUser.getJobPosting().add(jp);
                jobManagementServiceImpl.saveJobPosting(jp);
                springyBotServiceImpl.save(springyBot);
            }

        } else {
            response.setText("未发布招聘");
            response.setDisableWebPagePreview(true);
            common.executeAsync(response);

        }

    }

    public void setResponse_edit_jobSeeker_management() {

        SendMessage response = new SendMessage();
        response.setChatId(chatId_str);
        response.setDisableWebPagePreview(true);

        List<ChannelMessageIdPostCounts> channelMessageIdPostCounts = jobManagementServiceImpl
                .findAllByBotIdAndUserIdAndTypeWithChannelMessageIdPostCounts(springyBotId.toString(),
                        chatId_str, "jobSeeker");

        List<String> alertMessages_channel = channelMessageIdPostCounts.stream().map(cmpc -> {
            String markdown = "频道 [ " + cmpc.getChannelTitle() + " ] ";
            if (cmpc.getPostCount() <= 0) {
                return "";
            }
            return markdown + " 发布 " + cmpc.getPostCount() + " 则 [ 求职人员 ] 信息 \n";
        }).filter(str -> !Objects.equals(str, "")).collect(Collectors.toList());

        List<GroupMessageIdPostCounts> groupMessageIdPostCounts = jobManagementServiceImpl
                .findAllByBotIdAndUserIdAndTypeWithGroupMessageIdPostCounts(springyBotId.toString(),
                        chatId_str, "jobSeeker");

        List<String> alertMessages_group = groupMessageIdPostCounts.stream().map(gmpc -> {

            String markdown = "群组 [ " + gmpc.getGroupTitle() + " ] ";
            if (gmpc.getPostCount() <= 0) {
                return "";
            }
            return markdown + " 发布 " + gmpc.getPostCount() + " 则 [ 求职人员 ] 信息 \n";
        }).filter(str -> !Objects.equals(str, "")).collect(Collectors.toList());

        String alert_channel = String.join("", alertMessages_channel);
        String alert_group = String.join("", alertMessages_group);
        String alert = alert_channel + alert_group;

        if (!alert.isEmpty()) {

            response.setText("通知：\n" + alert + "\n下方模版可对频道内信息进行编辑和删除操作");
            response.setDisableWebPagePreview(true);
            common.executeAsync(response);

            JobSeeker jobSeeker = jobManagementServiceImpl.findByUserIdAndBotIdWithJobSeeker(chatId_str,
                    springyBotId.toString());

            SpringyBot springyBot = springyBotServiceImpl.findById(springyBotId).orElseThrow();
            List<JobUser> jobUsers = springyBotServiceImpl.findJobUserBySpringyBotId(springyBotId);

            JobUser jobUser = jobUsers.stream().filter(j -> j.getUserId().equals(chatId_str)).findAny().get();

            JobSeekerDTO jobSeekerDTO = new JobSeekerDTO(chatId_str, springyBotId.toString());

            if (jobSeeker != null) {

                response.setText(jobSeekerDTO.generateJobSeekerResponse(jobSeeker, true));

                jobSeekerDTO = jobSeekerDTO.createJobSeekerDTO(chatId_str, springyBotId.toString());

                response.setReplyMarkup(new TelentButton().keyboard_jobSeeker(jobSeekerDTO, true));
                response.setDisableWebPagePreview(true);
                Integer messageId = common.executeAsync(response);
                jobSeeker.setLastMessageId(messageId);
                jobUser.getJobSeeker().add(jobSeeker);
                jobManagementServiceImpl.saveJobSeeker(jobSeeker);

            } else {
                response.setText(TelentEnum.JOBSEEKE_REDITOR_DEFAULT_FORM.getText());
                response.setReplyMarkup(new TelentButton().keyboard_jobSeeker(jobSeekerDTO, true));

                response.setDisableWebPagePreview(true);
                JobSeeker js = new JobSeeker(chatId_str, springyBotId.toString(),
                        common.executeAsync(response));
                jobUser.getJobSeeker().add(js);
                jobManagementServiceImpl.saveJobSeeker(js);
                springyBotServiceImpl.save(springyBot);
            }

        } else {
            response.setText("未发布求职");
            response.setDisableWebPagePreview(true);
            common.executeAsync(response);
        }
    }

}
