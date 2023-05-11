package com.lwdevelop.bot.talentBot.handler.messageEvent.private_.commands;

import java.util.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import com.lwdevelop.bot.talentBot.utils.Common;
import com.lwdevelop.bot.talentBot.utils.KeyboardButton;
import com.lwdevelop.bot.talentBot.utils.SpringyBotEnum;
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

    public Job(){
        
    }

    public Job(JobPostingDTO posting, JobSeekerDTO seeker) {
        this.posting = posting;
        this.seeker = seeker;
    }

    public void saveJobUser(Common common) {
        Long id = common.getSpringyBotId();
        String userId = String.valueOf(common.getUpdate().getMessage().getChatId());
        String firstname = Optional.ofNullable(common.getUpdate().getMessage().getChat().getFirstName())
                        .orElse("");
        String username = Optional.ofNullable(common.getUpdate().getMessage().getChat().getUserName())
                        .orElse("");
        String lastname = Optional.ofNullable(common.getUpdate().getMessage().getChat().getLastName())
                        .orElse("");

        SpringyBot springyBot = springyBotServiceImpl.findById(id).orElseThrow();

        springyBot.getJobUser().stream().filter(j -> j.getUserId().equals(userId)).findFirst()
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
                                springyBot.getJobUser().add(jobUser);
                        });
        springyBotServiceImpl.save(springyBot);

}

    public void setResponse_jobPosting_management(Common common) {

        log.info("Entering setResponse_jobPosting_management method...");

        Long id = common.getSpringyBotId();
        String userId = String.valueOf(common.getUpdate().getMessage().getChatId());

        List<ChannelMessageIdPostCounts> channelMessageIdPostCounts = jobManagementServiceImpl
                .findAllByBotIdAndUserIdAndTypeWithChannelMessageIdPostCounts(
                        String.valueOf(id), userId, SpringyBotEnum.JOBPOSTING.getText());
        channelMessageIdPostCounts.stream().filter(cmpc -> cmpc.getPostCount() >= 1).findAny()
                .ifPresentOrElse(action -> {
                    SendMessage response = new SendMessage(userId, SpringyBotEnum.ALREADY_POST_POSTING.getText());
                    common.sendResponseAsync(response);
                }, () -> {

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

                    // 沒有發布過
                    if (jobPosting == null) {
                        response.setText(SpringyBotEnum.JOBPOSTING_DEFAULT_FORM.getText());
                        log.info("No job posting found for user {}, bot id {}", userId, id);
                    } else {
                        response.setText(posting.generateJobPostingResponse(jobPosting, false));
                        log.info("Job posting found for user {}, bot id {}: {}", userId, id, jobPosting);

                    }

                    common.sendResponseAsync(response);
                    response.setText(SpringyBotEnum.REMIND_EDITOR_.getText());
                    common.sendResponseAsync(response);
                });

    }

    public void setResponse_jobSeeker_management(Common common) {
        Long id = common.getSpringyBotId();
        String userId = String.valueOf(common.getUpdate().getMessage().getChatId());

        List<ChannelMessageIdPostCounts> channelMessageIdPostCounts = jobManagementServiceImpl
                .findAllByBotIdAndUserIdAndTypeWithChannelMessageIdPostCounts(
                        String.valueOf(id), userId, SpringyBotEnum.JOBSEEKER.getText());
        channelMessageIdPostCounts.stream().filter(cmpc -> cmpc.getPostCount() >= 1).findAny()
                .ifPresentOrElse(action -> {
                    SendMessage response = new SendMessage(userId, SpringyBotEnum.ALREADY_POST_SEEKER.getText());
                    common.sendResponseAsync(response);
                }, () -> {
                    log.info("Entering setResponse_jobSeeker_management method...");

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

                    if (jobSeeker == null) {
                        response.setText(SpringyBotEnum.JOBSEEKER_DEFAULT_FORM.getText());
                        log.info("No job seeker found for user {}, bot id {}", userId, id);
                    } else {

                        response.setText(seeker.generateJobSeekerResponse(jobSeeker, false));
                        log.info("Job seeker found for user {}, bot id {}: {}", userId, id, jobSeeker);

                    }

                    common.sendResponseAsync(response);

                    response.setText(SpringyBotEnum.REMIND_EDITOR_.getText());
                    common.sendResponseAsync(response);
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

        String isSuccess = posting.fillJobPostingInfo(jobPosting, lines);

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

            String result = posting.generateJobPostingDetails(jobPosting);

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
                                    channelId, String.valueOf(message.getChatId()),
                                    SpringyBotEnum.JOBPOSTING.getText());

                    if (isEdit) {
                        EditMessageText editMessageText = new EditMessageText();
                        editMessageText.setChatId(String.valueOf(channelId));
                        editMessageText.setText("招聘人才\n\n" + result + "\n\n 关注 @rc499 点击 @rc899Bot 发布");
                        editMessageText.setMessageId(channelMessageIdPostCounts.getMessageId());
                        editMessageText.setDisableWebPagePreview(true);
                        common.editResponseAsync(editMessageText);

                        response.setChatId(jobPosting.getUserId());
                        response.setText("[ " + channelTitle + " ]编辑成功");
                        common.sendResponseAsync(response);

                    } else {

                        if (channelMessageIdPostCounts == null) {
                            final Integer channelMessageId = common.sendResponseAsync(response);

                            response.setChatId(jobPosting.getUserId());
                            response.setText("[ " + channelTitle + " ]发送成功");
                            common.sendResponseAsync(response);

                            channelMessageIdPostCounts = new ChannelMessageIdPostCounts();
                            channelMessageIdPostCounts.setBotId(jobPosting.getBotId());
                            channelMessageIdPostCounts.setUserId(jobPosting.getUserId());
                            channelMessageIdPostCounts.setChannelId(channelId);
                            channelMessageIdPostCounts.setChannelTitle(channelTitle);
                            channelMessageIdPostCounts.setChannelLink(channelLink);
                            channelMessageIdPostCounts.setMessageId(channelMessageId);
                            channelMessageIdPostCounts.setPostCount(1);
                            channelMessageIdPostCounts.setType(SpringyBotEnum.JOBPOSTING.getText());
                            jobPosting = jobManagementServiceImpl.findByUserIdAndBotIdWithJobPosting(
                                    String.valueOf(message.getChatId()), String.valueOf(common.getSpringyBotId()));
                            jobPosting.getChannelMessageIdPostCounts().add(channelMessageIdPostCounts);
                            jobManagementServiceImpl.saveJobPosting(jobPosting);
                        } else {
                            if (channelMessageIdPostCounts.getPostCount() <= 0) {
                                final Integer channelMessageId = common.sendResponseAsync(response);
                                response.setChatId(jobPosting.getUserId());
                                response.setText("[ " + channelTitle + " ]发送 [招聘人才] 信息成功");
                                common.sendResponseAsync(response);
                                channelMessageIdPostCounts.setMessageId(channelMessageId);
                                channelMessageIdPostCounts.setPostCount(channelMessageIdPostCounts.getPostCount() + 1);
                                jobManagementServiceImpl.saveChannelMessageIdPostCounts(channelMessageIdPostCounts);
                            } else {
                                response.setChatId(jobPosting.getUserId());
                                response.setText("您已在[ " + channelTitle + " ]發送一條 [招聘人才] 信息");
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
                            .findByGroupIdAndTypeWithGroupMessageIdPostCounts(groupId,
                                    SpringyBotEnum.JOBPOSTING.getText());
                    if (isEdit) {
                        EditMessageText editMessageText = new EditMessageText();
                        editMessageText.setChatId(String.valueOf(groupId));
                        editMessageText.setText("招聘人才\n\n" + result + "\n\n 关注 @rc499 点击 @rc899Bot 发布");
                        editMessageText.setMessageId(groupMessageIdPostCounts.getMessageId());
                        editMessageText.setDisableWebPagePreview(true);
                        common.editResponseAsync(editMessageText);

                        response.setChatId(jobPosting.getUserId());
                        response.setText("[ " + groupTitle + " ]编辑成功");
                        common.sendResponseAsync(response);
                    } else {

                        if (groupMessageIdPostCounts == null) {
                            final Integer groupMessageId = common.sendResponseAsync(response);

                            response.setChatId(jobPosting.getUserId());
                            response.setText("[ " + groupTitle + " ]发送 [招聘人才] 信息成功");
                            common.sendResponseAsync(response);

                            groupMessageIdPostCounts = new GroupMessageIdPostCounts();
                            groupMessageIdPostCounts.setBotId(jobPosting.getBotId());
                            groupMessageIdPostCounts.setUserId(jobPosting.getUserId());
                            groupMessageIdPostCounts.setGroupId(groupId);
                            groupMessageIdPostCounts.setGroupTitle(groupTitle);
                            groupMessageIdPostCounts.setGroupLink(groupLink);
                            groupMessageIdPostCounts.setMessageId(groupMessageId);
                            groupMessageIdPostCounts.setPostCount(1);
                            groupMessageIdPostCounts.setType(SpringyBotEnum.JOBPOSTING.getText());
                            jobPosting = jobManagementServiceImpl.findByUserIdAndBotIdWithJobPosting(
                                    String.valueOf(message.getChatId()), String.valueOf(common.getSpringyBotId()));
                            jobPosting.getGroupMessageIdPostCounts().add(groupMessageIdPostCounts);
                            jobManagementServiceImpl.saveJobPosting(jobPosting);
                        } else {
                            if (groupMessageIdPostCounts.getPostCount() == 0) {
                                final Integer groupMessageId = common.sendResponseAsync(response);

                                response.setChatId(jobPosting.getUserId());
                                response.setText("[ " + groupTitle + " ]发送 [招聘人才] 成功");
                                common.sendResponseAsync(response);

                                groupMessageIdPostCounts.setMessageId(groupMessageId);
                                groupMessageIdPostCounts.setPostCount(groupMessageIdPostCounts.getPostCount() + 1);
                                jobManagementServiceImpl.saveGroupMessageIdPostCounts(groupMessageIdPostCounts);
                            } else {
                                response.setChatId(jobPosting.getUserId());
                                response.setText("您已在[ " + groupTitle + " ]發送一條 [招聘人才] 信息");
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

        Message message = common.getUpdate().getMessage();
        String text = message.getText();

        String[] lines = text.split("\\r?\\n");

        JobSeeker jobSeeker = jobManagementServiceImpl.findByUserIdAndBotIdWithJobSeeker(
                String.valueOf(message.getChatId()), String.valueOf(common.getSpringyBotId()));

        if (jobSeeker == null) {
            // 清除舊資料
            jobSeeker = new JobSeeker();

        }
        String isSuccess = seeker.fillJobSeekerInfo(jobSeeker, lines);
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

            String result = seeker.generateJobSeekerDetails(jobSeeker);

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
                        EditMessageText editMessageText = new EditMessageText(
                                "求职人员\n\n" + result + "\n\n 关注 @rc499 点击 @rc899Bot 发布");
                        editMessageText.setChatId(String.valueOf(channelId));
                        editMessageText.setText("求职人员\n\n" + result + "\n\n 关注 @rc499 点击 @rc899Bot 发布");
                        editMessageText.setMessageId(channelMessageIdPostCounts.getMessageId());
                        editMessageText.setDisableWebPagePreview(true);
                        common.editResponseAsync(editMessageText);

                        response.setChatId(jobSeeker.getUserId());
                        response.setText("[ " + channelTitle + " ]编辑成功");
                        common.sendResponseAsync(response);
                    } else {
                        if (channelMessageIdPostCounts == null) {

                            final Integer channelMessageId = common.sendResponseAsync(response);
                            response.setChatId(jobSeeker.getUserId());
                            response.setText("[ " + channelTitle + " ]发送 [求职人员] 成功");
                            common.sendResponseAsync(response);

                            channelMessageIdPostCounts = new ChannelMessageIdPostCounts();
                            channelMessageIdPostCounts.setBotId(jobSeeker.getBotId());
                            channelMessageIdPostCounts.setUserId(jobSeeker.getUserId());
                            channelMessageIdPostCounts.setChannelId(channelId);
                            channelMessageIdPostCounts.setChannelTitle(channelTitle);
                            channelMessageIdPostCounts.setChannelLink(channelLink);
                            channelMessageIdPostCounts.setMessageId(channelMessageId);
                            channelMessageIdPostCounts.setPostCount(1);
                            channelMessageIdPostCounts.setType(SpringyBotEnum.JOBSEEKER.getText());

                            jobSeeker = jobManagementServiceImpl.findByUserIdAndBotIdWithJobSeeker(
                                    String.valueOf(message.getChatId()), String.valueOf(common.getSpringyBotId()));

                            jobSeeker.getChannelMessageIdPostCounts().add(channelMessageIdPostCounts);
                            jobManagementServiceImpl.saveJobSeeker(jobSeeker);
                        } else {
                            if (channelMessageIdPostCounts.getPostCount() == 0) {

                                final Integer channelMessageId = common.sendResponseAsync(response);
                                response.setChatId(jobSeeker.getUserId());
                                response.setText("[ " + channelTitle + " ]发送 [求职人员] 成功");
                                common.sendResponseAsync(response);

                                channelMessageIdPostCounts.setMessageId(channelMessageId);
                                channelMessageIdPostCounts.setPostCount(channelMessageIdPostCounts.getPostCount() + 1);
                                jobManagementServiceImpl.saveChannelMessageIdPostCounts(channelMessageIdPostCounts);
                            } else {
                                response.setChatId(jobSeeker.getUserId());
                                response.setText("您已在[ " + channelTitle + " ]發送一條 [求职人员] 信息");
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
                                    groupId, SpringyBotEnum.JOBSEEKER.getText());

                    if (isEdit) {
                        EditMessageText editMessageText = new EditMessageText();
                        editMessageText.setChatId(String.valueOf(groupId));
                        editMessageText.setText("求职人员\n\n" + result + "\n\n 关注 @rc499 点击 @rc899Bot 发布");
                        editMessageText.setMessageId(groupMessageIdPostCounts.getMessageId());
                        editMessageText.setDisableWebPagePreview(true);
                        common.editResponseAsync(editMessageText);

                        response.setChatId(jobSeeker.getUserId());
                        response.setText("[ " + groupTitle + " ]编辑成功");
                        common.sendResponseAsync(response);

                    } else {
                        if (groupMessageIdPostCounts == null) {
                            final Integer groupMessageId = common.sendResponseAsync(response);
                            response.setChatId(jobSeeker.getUserId());
                            response.setText("[ " + groupTitle + " ]发送 [求职人员] 成功");
                            common.sendResponseAsync(response);

                            groupMessageIdPostCounts = new GroupMessageIdPostCounts();
                            groupMessageIdPostCounts.setBotId(jobSeeker.getBotId());
                            groupMessageIdPostCounts.setUserId(jobSeeker.getUserId());
                            groupMessageIdPostCounts.setGroupId(groupId);
                            groupMessageIdPostCounts.setGroupTitle(groupTitle);
                            groupMessageIdPostCounts.setGroupLink(groupLink);
                            groupMessageIdPostCounts.setMessageId(groupMessageId);
                            groupMessageIdPostCounts.setPostCount(1);
                            groupMessageIdPostCounts.setType(SpringyBotEnum.JOBSEEKER.getText());

                            jobSeeker = jobManagementServiceImpl.findByUserIdAndBotIdWithJobSeeker(
                                    String.valueOf(message.getChatId()), String.valueOf(common.getSpringyBotId()));

                            jobSeeker.getGroupMessageIdPostCounts().add(groupMessageIdPostCounts);
                            jobManagementServiceImpl.saveJobSeeker(jobSeeker);
                        } else {
                            if (groupMessageIdPostCounts.getPostCount() == 0) {

                                final Integer groupMessageId = common.sendResponseAsync(response);
                                response.setChatId(jobSeeker.getUserId());
                                response.setText("[ " + groupTitle + " ]发送 [求职人员] 成功");
                                common.sendResponseAsync(response);

                                groupMessageIdPostCounts.setMessageId(groupMessageId);
                                groupMessageIdPostCounts.setPostCount(groupMessageIdPostCounts.getPostCount() + 1);
                                jobManagementServiceImpl.saveGroupMessageIdPostCounts(groupMessageIdPostCounts);
                            } else {
                                response.setChatId(jobSeeker.getUserId());
                                response.setText("您已在[ " + groupTitle + " ]發送一條 [求职人员] 信息");
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

    public void setResponse_edit_jobPosting_management(Common common) {

        Long id = common.getSpringyBotId();
        String userId = String.valueOf(common.getUpdate().getMessage().getChatId());

        SendMessage response = new SendMessage();
        response.setChatId(userId);
        response.setDisableNotification(true);
        response.setDisableWebPagePreview(true);

        List<ChannelMessageIdPostCounts> channelMessageIdPostCounts = jobManagementServiceImpl
                .findAllByBotIdAndUserIdAndTypeWithChannelMessageIdPostCounts(String.valueOf(id),
                        userId, SpringyBotEnum.JOBPOSTING.getText());

        List<String> alertMessages_channel = channelMessageIdPostCounts.stream().map(cmpc -> {
            String markdown = "频道 [ " + cmpc.getChannelTitle() + " ] ";
            if (cmpc.getPostCount() <= 0) {
                return "";
            }
            return markdown + " 发布 " + cmpc.getPostCount() + " 则 [ 招聘人才 ] 信息 \n";
        }).filter(str -> !Objects.equals(str, "")).collect(Collectors.toList());

        List<GroupMessageIdPostCounts> groupMessageIdPostCounts = jobManagementServiceImpl
                .findAllByBotIdAndUserIdAndTypeWithGroupMessageIdPostCounts(String.valueOf(id),
                        userId, SpringyBotEnum.JOBPOSTING.getText());

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

            if (jobPosting != null) {

                response.setText(posting.generateJobPostingResponse(jobPosting, true));
                jobPostingDTO = posting.createJobPostingDTO(userId, String.valueOf(id));

                response.setReplyMarkup(new KeyboardButton().keyboard_jobPosting(jobPostingDTO, true));
                response.setDisableNotification(true);
                response.setDisableWebPagePreview(true);
                Integer messageId = common.sendResponseAsync(response);
                jobPosting.setLastMessageId(messageId);
                jobUser.getJobPosting().add(jobPosting);
                jobManagementServiceImpl.saveJobPosting(jobPosting);
            } else {
                response.setText(SpringyBotEnum.JOBPOSTING_EDITOR_DEFAULT_FORM.getText());
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
            response.setText(SpringyBotEnum.UNPUBLISHED_RECRUITMENT.getText());
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
                        userId, SpringyBotEnum.JOBSEEKER.getText());

        List<String> alertMessages_channel = channelMessageIdPostCounts.stream().map(cmpc -> {
            String markdown = "频道 [ " + cmpc.getChannelTitle() + " ] ";
            if (cmpc.getPostCount() <= 0) {
                return "";
            }
            return markdown + " 发布 " + cmpc.getPostCount() + " 则 [ 求职人员 ] 信息 \n";
        }).filter(str -> !Objects.equals(str, "")).collect(Collectors.toList());

        List<GroupMessageIdPostCounts> groupMessageIdPostCounts = jobManagementServiceImpl
                .findAllByBotIdAndUserIdAndTypeWithGroupMessageIdPostCounts(String.valueOf(id),
                        userId, SpringyBotEnum.JOBSEEKER.getText());

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
            common.sendResponseAsync(response);

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

            if (jobSeeker != null) {

                response.setText(seeker.generateJobSeekerResponse(jobSeeker, true));

                jobSeekerDTO = seeker.createJobSeekerDTO(userId, String.valueOf(id));

                response.setReplyMarkup(new KeyboardButton().keyboard_jobSeeker(jobSeekerDTO, true));
                response.setDisableNotification(true);
                response.setDisableWebPagePreview(true);
                Integer messageId = common.sendResponseAsync(response);
                jobSeeker.setLastMessageId(messageId);
                jobUser.getJobSeeker().add(jobSeeker);
                jobManagementServiceImpl.saveJobSeeker(jobSeeker);

            } else {
                response.setText(SpringyBotEnum.JOBSEEKE_REDITOR_DEFAULT_FORM.getText());
                response.setReplyMarkup(new KeyboardButton().keyboard_jobSeeker(jobSeekerDTO, true));

                response.setDisableNotification(true);
                response.setDisableWebPagePreview(true);
                JobSeeker js = new JobSeeker(userId, String.valueOf(id),
                        common.sendResponseAsync(response));
                jobUser.getJobSeeker().add(js);
                jobManagementServiceImpl.saveJobSeeker(js);
                springyBotServiceImpl.save(springyBot);
            }

        } else {
            response.setText(SpringyBotEnum.UNPUBLISHED_JOBSEARCH.getText());
            response.setDisableNotification(true);
            response.setDisableWebPagePreview(true);
            common.sendResponseAsync(response);
        }
    }

}
