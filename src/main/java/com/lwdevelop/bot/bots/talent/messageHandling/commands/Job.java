package com.lwdevelop.bot.bots.talent.messageHandling.commands;

import java.util.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
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
import com.lwdevelop.service.impl.SpringyBotServiceImpl;
import com.lwdevelop.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;

@Slf4j
public class Job {

    @Autowired
    private JobManagementServiceImpl jobManagementServiceImpl = SpringUtils.getApplicationContext()
            .getBean(JobManagementServiceImpl.class);

    @Autowired
    private SpringyBotServiceImpl springyBotServiceImpl = SpringUtils.getApplicationContext()
            .getBean(SpringyBotServiceImpl.class);

    private JobPostingDTO posting;
    private JobSeekerDTO seeker;

    public Job() {

    }

    public Job(JobPostingDTO posting, JobSeekerDTO seeker) {
        this.posting = posting;
        this.seeker = seeker;
    }

    public void saveJobUser(Common common) {
        Long id = common.getSpringyBotId();
        String userId = String.valueOf(common.getUpdate().getMessage().getChatId());
        String firstname = Optional.ofNullable(common.getUpdate().getMessage().getChat().getFirstName()).orElse("");
        String username = Optional.ofNullable(common.getUpdate().getMessage().getChat().getUserName()).orElse("");
        String lastname = Optional.ofNullable(common.getUpdate().getMessage().getChat().getLastName()).orElse("");
        SpringyBot springyBot = springyBotServiceImpl.findById(id).orElseThrow();
        List<JobUser> jobUserList = springyBotServiceImpl.findJobUserBySpringyBotId(common.getSpringyBotId());

        jobUserList.stream().filter(j -> j.getUserId().equals(userId)).findAny()
                .ifPresentOrElse(ju -> {
                    ju.setFirstname(firstname);
                    ju.setLastname(lastname);
                    ju.setUsername(username);
                }, () -> {
                    JobUser jobUser = new JobUser();
                    jobUser.setUserId(userId);
                    jobUser.setFirstname(firstname);
                    jobUser.setLastname(lastname);
                    jobUser.setUsername(username);
                    jobUserList.add(jobUser);
                });
        springyBot.setJobUser(jobUserList);
        springyBotServiceImpl.save(springyBot);

    }

    public void setResponse_jobPosting_management(Common common) {

        log.info("Entering setResponse_jobPosting_management method...");

        Long id = common.getSpringyBotId();
        String userId = String.valueOf(common.getUpdate().getMessage().getChatId());

        List<ChannelMessageIdPostCounts> channelMessageIdPostCounts = jobManagementServiceImpl
                .findAllByBotIdAndUserIdAndTypeWithChannelMessageIdPostCounts(
                        String.valueOf(id), userId, TelentEnum.JOBPOSTING.getText());
        channelMessageIdPostCounts.stream().filter(cmpc -> cmpc.getPostCount() >= 1).findAny()
                .ifPresentOrElse(action -> {
                    SendMessage response = new SendMessage(userId, TelentEnum.ALREADY_POST_POSTING.getText());
                    common.executeAsync(response);
                }, () -> {

                    SendMessage response = new SendMessage();
                    response.setChatId(userId);

                    JobPosting jobPosting = jobManagementServiceImpl.findByUserIdAndBotIdWithJobPosting(userId,
                            String.valueOf(id));

                    // 沒有發布過
                    if (jobPosting == null) {
                        response.setText(TelentEnum.JOBPOSTING_DEFAULT_FORM.getText());
                        log.info("No job posting found for user {}, bot id {}", userId, id);
                    } else {
                        response.setText(posting.generateJobPostingResponse(jobPosting, false));
                        log.info("Job posting found for user {}, bot id {}: {}", userId, id, jobPosting);
                    }

                    common.executeAsync(response);
                    response.setText(TelentEnum.REMIND_EDITOR_.getText());
                    common.executeAsync(response);
                });

    }

    public void setResponse_jobSeeker_management(Common common) {
        Long id = common.getSpringyBotId();
        String userId = String.valueOf(common.getUpdate().getMessage().getChatId());

        List<ChannelMessageIdPostCounts> channelMessageIdPostCounts = jobManagementServiceImpl
                .findAllByBotIdAndUserIdAndTypeWithChannelMessageIdPostCounts(
                        String.valueOf(id), userId, TelentEnum.JOBSEEKER.getText());
        channelMessageIdPostCounts.stream().filter(cmpc -> cmpc.getPostCount() >= 1).findAny()
                .ifPresentOrElse(action -> {
                    SendMessage response = new SendMessage(userId, TelentEnum.ALREADY_POST_SEEKER.getText());
                    common.executeAsync(response);
                }, () -> {
                    log.info("Entering setResponse_jobSeeker_management method...");

                    SendMessage response = new SendMessage();
                    response.setChatId(userId);

                    JobSeeker jobSeeker = jobManagementServiceImpl.findByUserIdAndBotIdWithJobSeeker(userId,
                            String.valueOf(id));

                    if (jobSeeker == null) {
                        response.setText(TelentEnum.JOBSEEKER_DEFAULT_FORM.getText());
                        log.info("No job seeker found for user {}, bot id {}", userId, id);
                    } else {

                        response.setText(seeker.generateJobSeekerResponse(jobSeeker, false));
                        log.info("Job seeker found for user {}, bot id {}: {}", userId, id, jobSeeker);

                    }

                    common.executeAsync(response);
                    response.setText(TelentEnum.REMIND_EDITOR_.getText());
                    common.executeAsync(response);
                });

    }

    public void generateTextJobPosting(Common common, Boolean isEdit) {
        Message message = common.getUpdate().getMessage();
        String text = message.getText();
        // 将文本内容按行分割成字符串数组
        String[] lines = text.split("\\r?\\n");

        JobPosting jobPosting = jobManagementServiceImpl.findByUserIdAndBotIdWithJobPosting(
                String.valueOf(message.getChatId()), String.valueOf(common.getSpringyBotId()));

        if (jobPosting == null) {
            jobPosting = new JobPosting();
        }

        String username = "@" + common.getUpdate().getMessage().getFrom().getUserName();
        jobPosting.setPublisher(username);

        String isSuccess = posting.fillJobPostingInfo(jobPosting, lines);

        if (!StringUtils.hasText(isSuccess)) {
            jobPosting.setBotId(String.valueOf(common.getSpringyBotId()));
            jobPosting.setUserId(String.valueOf(message.getChatId()));
            jobPosting.setLastMessageId(message.getMessageId());
            // 處理資料表
            Long id = common.getSpringyBotId();
            String userId = String.valueOf(common.getUpdate().getMessage().getChatId());
            List<JobUser> jobUsers = springyBotServiceImpl.findJobUserBySpringyBotId(id);
            JobUser jobUser = jobUsers.stream().filter(j -> j.getUserId().equals(userId)).findAny().get();
            final Long final_jobPostingId = jobPosting.getId();

            if (!jobUser.getJobPosting().stream().anyMatch(p -> p.getId().equals(final_jobPostingId))) {
                jobUser.getJobPosting().add(jobPosting);
            }

            jobManagementServiceImpl.saveJobPosting(jobPosting);

            String result = posting.generateJobPostingDetails(jobPosting);

            List<RobotChannelManagement> robotChannelManagements = springyBotServiceImpl
                    .findRobotChannelManagementBySpringyBotId(common.getSpringyBotId());

            Iterator<RobotChannelManagement> iterator_channel = robotChannelManagements.iterator();

            while (iterator_channel.hasNext()) {
                RobotChannelManagement robotChannelManagement = iterator_channel.next();
                if (!result.isEmpty()) {
                    SendMessage response = new SendMessage();
                    Long channelId = robotChannelManagement.getChannelId();
                    String channelTitle = robotChannelManagement.getChannelTitle();
                    String channelLink = robotChannelManagement.getLink();
                    response.setChatId(String.valueOf(channelId));
                    response.setText(TelentEnum.send_recruitment_text(result));
                    response.setReplyMarkup(new TelentButton().keyboardJobMarkup());
                    response.setDisableWebPagePreview(true);
                    response.setDisableNotification(true);
                    ChannelMessageIdPostCounts channelMessageIdPostCounts = jobManagementServiceImpl
                            .findByChannelIdAndUserIdAndTypeWithChannelMessageIdPostCounts(
                                    channelId, String.valueOf(message.getChatId()),
                                    TelentEnum.JOBPOSTING.getText());

                    if (isEdit) {
                        EditMessageText editMessageText = new EditMessageText();
                        editMessageText.setChatId(String.valueOf(channelId));
                        editMessageText.setText(TelentEnum.send_recruitment_text(result));
                        editMessageText.setMessageId(channelMessageIdPostCounts.getMessageId());
                        editMessageText.setDisableWebPagePreview(true);
                        common.executeAsync(editMessageText);

                        response.setChatId(jobPosting.getUserId());
                        response.setText("[ " + channelTitle + " ]编辑成功");
                        common.executeAsync(response);

                    } else {

                        if (channelMessageIdPostCounts == null) {
                            final Integer channelMessageId = common.executeAsync(response);

                            response.setChatId(jobPosting.getUserId());
                            response.setText("[ " + channelTitle + " ]发送成功");
                            common.executeAsync(response);

                            channelMessageIdPostCounts = new ChannelMessageIdPostCounts();
                            channelMessageIdPostCounts.setBotId(jobPosting.getBotId());
                            channelMessageIdPostCounts.setUserId(jobPosting.getUserId());
                            channelMessageIdPostCounts.setChannelId(channelId);
                            channelMessageIdPostCounts.setChannelTitle(channelTitle);
                            channelMessageIdPostCounts.setChannelLink(channelLink);
                            channelMessageIdPostCounts.setMessageId(channelMessageId);
                            channelMessageIdPostCounts.setPostCount(1);
                            channelMessageIdPostCounts.setType(TelentEnum.JOBPOSTING.getText());
                            jobPosting = jobManagementServiceImpl.findByUserIdAndBotIdWithJobPosting(
                                    String.valueOf(message.getChatId()), String.valueOf(common.getSpringyBotId()));
                            jobPosting.getChannelMessageIdPostCounts().add(channelMessageIdPostCounts);
                            jobManagementServiceImpl.saveJobPosting(jobPosting);
                        } else {
                            if (channelMessageIdPostCounts.getPostCount() <= 0) {
                                final Integer channelMessageId = common.executeAsync(response);
                                response.setChatId(jobPosting.getUserId());
                                response.setText("[ " + channelTitle + " ]发送 [招聘人才] 信息成功");
                                common.executeAsync(response);
                                channelMessageIdPostCounts.setMessageId(channelMessageId);
                                channelMessageIdPostCounts.setPostCount(channelMessageIdPostCounts.getPostCount() + 1);
                                jobManagementServiceImpl.saveChannelMessageIdPostCounts(channelMessageIdPostCounts);
                            } else {
                                response.setChatId(jobPosting.getUserId());
                                response.setText("您已在[ " + channelTitle + " ]發送一條 [招聘人才] 信息");
                                common.executeAsync(response);
                            }
                        }

                    }

                }
            }

            List<RobotGroupManagement> robotGroupManagements = springyBotServiceImpl
                    .findRobotGroupManagementBySpringyBotId(common.getSpringyBotId());

            Iterator<RobotGroupManagement> iterator_group = robotGroupManagements.iterator();

            while (iterator_group.hasNext()) {
                RobotGroupManagement robotGroupManagement = iterator_group.next();
                if (!result.isEmpty()) {
                    SendMessage response = new SendMessage();
                    Long groupId = robotGroupManagement.getGroupId();
                    String groupTitle = robotGroupManagement.getGroupTitle();
                    String groupLink = robotGroupManagement.getLink();
                    response.setChatId(String.valueOf(groupId));
                    response.setText(TelentEnum.send_recruitment_text(result));
                    response.setReplyMarkup(new TelentButton().keyboardJobMarkup());
                    response.setDisableNotification(true);
                    response.setDisableWebPagePreview(true);
                    GroupMessageIdPostCounts groupMessageIdPostCounts = jobManagementServiceImpl
                            .findByGroupIdAndTypeWithGroupMessageIdPostCounts(groupId,
                                    TelentEnum.JOBPOSTING.getText());
                    if (isEdit) {
                        EditMessageText editMessageText = new EditMessageText();
                        editMessageText.setChatId(String.valueOf(groupId));
                        editMessageText.setText(TelentEnum.send_recruitment_text(result));
                        editMessageText.setMessageId(groupMessageIdPostCounts.getMessageId());
                        editMessageText.setDisableWebPagePreview(true);
                        common.executeAsync(editMessageText);

                        response.setChatId(jobPosting.getUserId());
                        response.setText("[ " + groupTitle + " ]编辑成功");
                        common.executeAsync(response);
                    } else {

                        if (groupMessageIdPostCounts == null) {
                            final Integer groupMessageId = common.executeAsync(response);

                            response.setChatId(jobPosting.getUserId());
                            response.setText("[ " + groupTitle + " ]发送 [招聘人才] 信息成功");
                            common.executeAsync(response);

                            groupMessageIdPostCounts = new GroupMessageIdPostCounts();
                            groupMessageIdPostCounts.setBotId(jobPosting.getBotId());
                            groupMessageIdPostCounts.setUserId(jobPosting.getUserId());
                            groupMessageIdPostCounts.setGroupId(groupId);
                            groupMessageIdPostCounts.setGroupTitle(groupTitle);
                            groupMessageIdPostCounts.setGroupLink(groupLink);
                            groupMessageIdPostCounts.setMessageId(groupMessageId);
                            groupMessageIdPostCounts.setPostCount(1);
                            groupMessageIdPostCounts.setType(TelentEnum.JOBPOSTING.getText());
                            jobPosting = jobManagementServiceImpl.findByUserIdAndBotIdWithJobPosting(
                                    String.valueOf(message.getChatId()), String.valueOf(common.getSpringyBotId()));
                            jobPosting.getGroupMessageIdPostCounts().add(groupMessageIdPostCounts);
                            jobManagementServiceImpl.saveJobPosting(jobPosting);
                        } else {
                            if (groupMessageIdPostCounts.getPostCount() == 0) {
                                final Integer groupMessageId = common.executeAsync(response);

                                response.setChatId(jobPosting.getUserId());
                                response.setText("[ " + groupTitle + " ]发送 [招聘人才] 成功");
                                common.executeAsync(response);

                                groupMessageIdPostCounts.setMessageId(groupMessageId);
                                groupMessageIdPostCounts.setPostCount(groupMessageIdPostCounts.getPostCount() + 1);
                                jobManagementServiceImpl.saveGroupMessageIdPostCounts(groupMessageIdPostCounts);
                            } else {
                                response.setChatId(jobPosting.getUserId());
                                response.setText("您已在[ " + groupTitle + " ]發送一條 [招聘人才] 信息");
                                common.executeAsync(response);
                            }
                        }

                    }

                }
            }
        } else {
            SendMessage response = new SendMessage();
            response.setChatId(jobPosting.getUserId());
            response.setText(isSuccess);
            response.setDisableNotification(true);
            response.setDisableWebPagePreview(true);
            common.executeAsync(response);
        }
    }

    public void generateTextJobSeeker(Common common, Boolean isEdit) {

        Message message = common.getUpdate().getMessage();
        String text = message.getText();

        String[] lines = text.split("\\r?\\n");

        JobSeeker jobSeeker = jobManagementServiceImpl.findByUserIdAndBotIdWithJobSeeker(
                String.valueOf(message.getChatId()), String.valueOf(common.getSpringyBotId()));

        if (jobSeeker == null) {
            // 清除舊資料
            jobSeeker = new JobSeeker();
        }

        String username = "@" + common.getUpdate().getMessage().getFrom().getUserName();
        jobSeeker.setPublisher(username);

        String isSuccess = seeker.fillJobSeekerInfo(jobSeeker, lines);
        if (!StringUtils.hasText(isSuccess)) {

            jobSeeker.setBotId(String.valueOf(common.getSpringyBotId()));
            jobSeeker.setUserId(String.valueOf(message.getChatId()));
            jobSeeker.setLastMessageId(message.getMessageId());

            // 處理資料表
            Long id = common.getSpringyBotId();
            String userId = String.valueOf(common.getUpdate().getMessage().getChatId());
            List<JobUser> jobUsers = springyBotServiceImpl.findJobUserBySpringyBotId(id);
            JobUser jobUser = jobUsers.stream().filter(j -> j.getUserId().equals(userId)).findAny().get();

            final Long final_jobSeekerId = jobSeeker.getId();

            if (!jobUser.getJobSeeker().stream().anyMatch(p -> p.getId().equals(final_jobSeekerId))) {
                jobUser.getJobSeeker().add(jobSeeker);
            }
            jobManagementServiceImpl.saveJobSeeker(jobSeeker);

            String result = seeker.generateJobSeekerDetails(jobSeeker);

            List<RobotChannelManagement> robotChannelManagements = springyBotServiceImpl
                    .findRobotChannelManagementBySpringyBotId(common.getSpringyBotId());
            Iterator<RobotChannelManagement> iterator_channel = robotChannelManagements.iterator();

            while (iterator_channel.hasNext()) {
                RobotChannelManagement robotChannelManagement = iterator_channel.next();
                if (!result.isEmpty()) {
                    SendMessage response = new SendMessage();
                    Long channelId = robotChannelManagement.getChannelId();
                    String channelTitle = robotChannelManagement.getChannelTitle();
                    String channelLink = robotChannelManagement.getLink();
                    response.setChatId(String.valueOf(channelId));
                    response.setText(TelentEnum.send_jobsearch_text(result));
                    response.setReplyMarkup(new TelentButton().keyboardJobMarkup());
                    response.setDisableNotification(true);
                    response.setDisableWebPagePreview(true);
                    ChannelMessageIdPostCounts channelMessageIdPostCounts = jobManagementServiceImpl
                            .findByChannelIdAndUserIdAndTypeWithChannelMessageIdPostCounts(
                                    channelId, String.valueOf(message.getChatId()), "jobSeeker");

                    if (isEdit) {
                        EditMessageText editMessageText = new EditMessageText(
                                TelentEnum.send_jobsearch_text(result));
                        editMessageText.setChatId(String.valueOf(channelId));
                        editMessageText.setText(TelentEnum.send_jobsearch_text(result));
                        editMessageText.setMessageId(channelMessageIdPostCounts.getMessageId());
                        editMessageText.setDisableWebPagePreview(true);
                        common.executeAsync(editMessageText);

                        response.setChatId(jobSeeker.getUserId());
                        response.setText("[ " + channelTitle + " ]编辑成功");
                        common.executeAsync(response);
                    } else {
                        if (channelMessageIdPostCounts == null) {

                            final Integer channelMessageId = common.executeAsync(response);
                            response.setChatId(jobSeeker.getUserId());
                            response.setText("[ " + channelTitle + " ]发送 [求职人员] 成功");
                            common.executeAsync(response);

                            channelMessageIdPostCounts = new ChannelMessageIdPostCounts();
                            channelMessageIdPostCounts.setBotId(jobSeeker.getBotId());
                            channelMessageIdPostCounts.setUserId(jobSeeker.getUserId());
                            channelMessageIdPostCounts.setChannelId(channelId);
                            channelMessageIdPostCounts.setChannelTitle(channelTitle);
                            channelMessageIdPostCounts.setChannelLink(channelLink);
                            channelMessageIdPostCounts.setMessageId(channelMessageId);
                            channelMessageIdPostCounts.setPostCount(1);
                            channelMessageIdPostCounts.setType(TelentEnum.JOBSEEKER.getText());

                            jobSeeker = jobManagementServiceImpl.findByUserIdAndBotIdWithJobSeeker(
                                    String.valueOf(message.getChatId()), String.valueOf(common.getSpringyBotId()));

                            jobSeeker.getChannelMessageIdPostCounts().add(channelMessageIdPostCounts);
                            jobManagementServiceImpl.saveJobSeeker(jobSeeker);
                        } else {
                            if (channelMessageIdPostCounts.getPostCount() == 0) {

                                final Integer channelMessageId = common.executeAsync(response);
                                response.setChatId(jobSeeker.getUserId());
                                response.setText("[ " + channelTitle + " ]发送 [求职人员] 成功");
                                common.executeAsync(response);

                                channelMessageIdPostCounts.setMessageId(channelMessageId);
                                channelMessageIdPostCounts.setPostCount(channelMessageIdPostCounts.getPostCount() + 1);
                                jobManagementServiceImpl.saveChannelMessageIdPostCounts(channelMessageIdPostCounts);
                            } else {
                                response.setChatId(jobSeeker.getUserId());
                                response.setText("您已在[ " + channelTitle + " ]發送一條 [求职人员] 信息");
                                common.executeAsync(response);
                            }
                        }
                    }
                }
            }

            List<RobotGroupManagement> robotGroupManagements = springyBotServiceImpl
                    .findRobotGroupManagementBySpringyBotId(common.getSpringyBotId());
            Iterator<RobotGroupManagement> iterator_group = robotGroupManagements.iterator();

            while (iterator_group.hasNext()) {
                RobotGroupManagement robotGroupManagement = iterator_group.next();
                if (!result.isEmpty()) {
                    SendMessage response = new SendMessage();
                    Long groupId = robotGroupManagement.getGroupId();
                    String groupTitle = robotGroupManagement.getGroupTitle();
                    String groupLink = robotGroupManagement.getLink();
                    response.setChatId(String.valueOf(groupId));
                    response.setText(TelentEnum.send_jobsearch_text(result));
                    response.setReplyMarkup(new TelentButton().keyboardJobMarkup());
                    response.setDisableNotification(true);
                    response.setDisableWebPagePreview(true);
                    GroupMessageIdPostCounts groupMessageIdPostCounts = jobManagementServiceImpl
                            .findByGroupIdAndTypeWithGroupMessageIdPostCounts(
                                    groupId, TelentEnum.JOBSEEKER.getText());

                    if (isEdit) {
                        EditMessageText editMessageText = new EditMessageText();
                        editMessageText.setChatId(String.valueOf(groupId));
                        editMessageText.setText(TelentEnum.send_jobsearch_text(result));
                        editMessageText.setMessageId(groupMessageIdPostCounts.getMessageId());
                        editMessageText.setDisableWebPagePreview(true);
                        common.executeAsync(editMessageText);

                        response.setChatId(jobSeeker.getUserId());
                        response.setText("[ " + groupTitle + " ]编辑成功");
                        common.executeAsync(response);

                    } else {
                        if (groupMessageIdPostCounts == null) {
                            final Integer groupMessageId = common.executeAsync(response);
                            response.setChatId(jobSeeker.getUserId());
                            response.setText("[ " + groupTitle + " ]发送 [求职人员] 成功");
                            common.executeAsync(response);

                            groupMessageIdPostCounts = new GroupMessageIdPostCounts();
                            groupMessageIdPostCounts.setBotId(jobSeeker.getBotId());
                            groupMessageIdPostCounts.setUserId(jobSeeker.getUserId());
                            groupMessageIdPostCounts.setGroupId(groupId);
                            groupMessageIdPostCounts.setGroupTitle(groupTitle);
                            groupMessageIdPostCounts.setGroupLink(groupLink);
                            groupMessageIdPostCounts.setMessageId(groupMessageId);
                            groupMessageIdPostCounts.setPostCount(1);
                            groupMessageIdPostCounts.setType(TelentEnum.JOBSEEKER.getText());

                            jobSeeker = jobManagementServiceImpl.findByUserIdAndBotIdWithJobSeeker(
                                    String.valueOf(message.getChatId()), String.valueOf(common.getSpringyBotId()));

                            jobSeeker.getGroupMessageIdPostCounts().add(groupMessageIdPostCounts);
                            jobManagementServiceImpl.saveJobSeeker(jobSeeker);
                        } else {
                            if (groupMessageIdPostCounts.getPostCount() == 0) {

                                final Integer groupMessageId = common.executeAsync(response);
                                response.setChatId(jobSeeker.getUserId());
                                response.setText("[ " + groupTitle + " ]发送 [求职人员] 成功");
                                common.executeAsync(response);

                                groupMessageIdPostCounts.setMessageId(groupMessageId);
                                groupMessageIdPostCounts.setPostCount(groupMessageIdPostCounts.getPostCount() + 1);
                                jobManagementServiceImpl.saveGroupMessageIdPostCounts(groupMessageIdPostCounts);
                            } else {
                                response.setChatId(jobSeeker.getUserId());
                                response.setText("您已在[ " + groupTitle + " ]發送一條 [求职人员] 信息");
                                common.executeAsync(response);
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
            common.executeAsync(response);
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
                        userId, TelentEnum.JOBPOSTING.getText());

        List<String> alertMessages_channel = channelMessageIdPostCounts.stream().map(cmpc -> {
            String markdown = "频道 [ " + cmpc.getChannelTitle() + " ] ";
            if (cmpc.getPostCount() <= 0) {
                return "";
            }
            return markdown + " 发布 " + cmpc.getPostCount() + " 则 [ 招聘人才 ] 信息 \n";
        }).filter(str -> !Objects.equals(str, "")).collect(Collectors.toList());

        List<GroupMessageIdPostCounts> groupMessageIdPostCounts = jobManagementServiceImpl
                .findAllByBotIdAndUserIdAndTypeWithGroupMessageIdPostCounts(String.valueOf(id),
                        userId, TelentEnum.JOBPOSTING.getText());

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
            response.setDisableNotification(true);
            response.setDisableWebPagePreview(true);
            common.executeAsync(response);

            JobPosting jobPosting = jobManagementServiceImpl.findByUserIdAndBotIdWithJobPosting(userId,
                    String.valueOf(id));

            SpringyBot springyBot = springyBotServiceImpl.findById(id).orElseThrow();
            List<JobUser> jobUsers = springyBotServiceImpl.findJobUserBySpringyBotId(id);
            JobUser jobUser = jobUsers.stream().filter(j -> j.getUserId().equals(userId)).findAny().get();

            JobPostingDTO jobPostingDTO = new JobPostingDTO(userId, String.valueOf(id));

            if (jobPosting != null) {

                response.setText(posting.generateJobPostingResponse(jobPosting, true));
                jobPostingDTO = posting.createJobPostingDTO(userId, String.valueOf(id));

                response.setReplyMarkup(new TelentButton().keyboard_jobPosting(jobPostingDTO, true));
                response.setDisableNotification(true);
                response.setDisableWebPagePreview(true);
                Integer messageId = common.executeAsync(response);
                jobPosting.setLastMessageId(messageId);
                jobUser.getJobPosting().add(jobPosting);
                jobManagementServiceImpl.saveJobPosting(jobPosting);
            } else {
                response.setText(TelentEnum.JOBPOSTING_EDITOR_DEFAULT_FORM.getText());
                response.setReplyMarkup(new TelentButton().keyboard_jobPosting(jobPostingDTO, false));
                response.setDisableNotification(true);
                response.setDisableWebPagePreview(true);

                JobPosting jp = new JobPosting(userId, String.valueOf(id),
                        common.executeAsync(response));
                jobUser.getJobPosting().add(jp);
                jobManagementServiceImpl.saveJobPosting(jp);
                springyBotServiceImpl.save(springyBot);
            }

        } else {
            response.setText(TelentEnum.UNPUBLISHED_RECRUITMENT.getText());
            response.setDisableNotification(true);
            response.setDisableWebPagePreview(true);
            common.executeAsync(response);

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
                        userId, TelentEnum.JOBSEEKER.getText());

        List<String> alertMessages_channel = channelMessageIdPostCounts.stream().map(cmpc -> {
            String markdown = "频道 [ " + cmpc.getChannelTitle() + " ] ";
            if (cmpc.getPostCount() <= 0) {
                return "";
            }
            return markdown + " 发布 " + cmpc.getPostCount() + " 则 [ 求职人员 ] 信息 \n";
        }).filter(str -> !Objects.equals(str, "")).collect(Collectors.toList());

        List<GroupMessageIdPostCounts> groupMessageIdPostCounts = jobManagementServiceImpl
                .findAllByBotIdAndUserIdAndTypeWithGroupMessageIdPostCounts(String.valueOf(id),
                        userId, TelentEnum.JOBSEEKER.getText());

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
            response.setDisableNotification(true);
            response.setDisableWebPagePreview(true);
            common.executeAsync(response);

            JobSeeker jobSeeker = jobManagementServiceImpl.findByUserIdAndBotIdWithJobSeeker(userId,
                    String.valueOf(id));

            SpringyBot springyBot = springyBotServiceImpl.findById(id).orElseThrow();
            List<JobUser> jobUsers = springyBotServiceImpl.findJobUserBySpringyBotId(id);

            JobUser jobUser = jobUsers.stream().filter(j -> j.getUserId().equals(userId)).findAny().get();

            JobSeekerDTO jobSeekerDTO = new JobSeekerDTO(userId, String.valueOf(id));

            if (jobSeeker != null) {

                response.setText(seeker.generateJobSeekerResponse(jobSeeker, true));

                jobSeekerDTO = seeker.createJobSeekerDTO(userId, String.valueOf(id));

                response.setReplyMarkup(new TelentButton().keyboard_jobSeeker(jobSeekerDTO, true));
                response.setDisableNotification(true);
                response.setDisableWebPagePreview(true);
                Integer messageId = common.executeAsync(response);
                jobSeeker.setLastMessageId(messageId);
                jobUser.getJobSeeker().add(jobSeeker);
                jobManagementServiceImpl.saveJobSeeker(jobSeeker);

            } else {
                response.setText(TelentEnum.JOBSEEKE_REDITOR_DEFAULT_FORM.getText());
                response.setReplyMarkup(new TelentButton().keyboard_jobSeeker(jobSeekerDTO, true));

                response.setDisableNotification(true);
                response.setDisableWebPagePreview(true);
                JobSeeker js = new JobSeeker(userId, String.valueOf(id),
                        common.executeAsync(response));
                jobUser.getJobSeeker().add(js);
                jobManagementServiceImpl.saveJobSeeker(js);
                springyBotServiceImpl.save(springyBot);
            }

        } else {
            response.setText(TelentEnum.UNPUBLISHED_JOBSEARCH.getText());
            response.setDisableNotification(true);
            response.setDisableWebPagePreview(true);
            common.executeAsync(response);
        }
    }

}
