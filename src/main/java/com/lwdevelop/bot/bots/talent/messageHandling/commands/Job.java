package com.lwdevelop.bot.bots.talent.messageHandling.commands;

import java.util.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import com.lwdevelop.bot.bots.utils.Common;
import com.lwdevelop.bot.bots.utils.enum_.TalentEnum;
import com.lwdevelop.bot.bots.utils.keyboardButton.TalentButton;
import com.lwdevelop.bot.chatMessageHandlers.BasePrivateMessage;
import com.lwdevelop.dto.JobPostingDTO;
import com.lwdevelop.dto.JobSeekerDTO;
import com.lwdevelop.entity.ChannelMessageIdPostCounts;
import com.lwdevelop.entity.GroupMessageIdPostCounts;
import com.lwdevelop.entity.JobPosting;
import com.lwdevelop.entity.JobSeeker;
import com.lwdevelop.entity.TgUser;
import com.lwdevelop.entity.RobotChannelManagement;
import com.lwdevelop.entity.RobotGroupManagement;
import com.lwdevelop.entity.SpringyBot;
import com.lwdevelop.service.impl.JobManagementServiceImpl;
import com.lwdevelop.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

@Slf4j
public class Job extends BasePrivateMessage {

    @Autowired
    private JobManagementServiceImpl jobManagementServiceImpl = SpringUtils.getApplicationContext()
            .getBean(JobManagementServiceImpl.class);

    private JobPostingDTO jobPostingDTO;
    private JobSeekerDTO jobSeekerDTO;

    public Job(Common common) {
        super(common);
        this.jobPostingDTO = new JobPostingDTO(common);
        this.jobSeekerDTO = new JobSeekerDTO(common);
        this.saveTgUser();
    }

    private void saveTgUser() {
        String firstname = Optional.ofNullable(common.getUpdate().getMessage().getChat().getFirstName()).orElse("");
        String username = Optional.ofNullable(common.getUpdate().getMessage().getChat().getUserName()).orElse("");
        String lastname = Optional.ofNullable(common.getUpdate().getMessage().getChat().getLastName()).orElse("");
        SpringyBot springyBot = springyBotServiceImpl.findById(springyBotId).orElseThrow();
        List<TgUser> tgUsers = springyBotServiceImpl.findTgUserBySpringyBotId(springyBotId);

        tgUsers.stream().filter(t -> t.getUserId().equals(chatId_str)).findAny()
                .ifPresentOrElse(tu -> {
                    tu.setFirstname(firstname);
                    tu.setLastname(lastname);
                    tu.setUsername(username);
                }, () -> {
                    TgUser tgUser = new TgUser();
                    tgUser.setUserId(chatId_str);
                    tgUser.setFirstname(firstname);
                    tgUser.setLastname(lastname);
                    tgUser.setUsername(username);
                    tgUsers.add(tgUser);
                });
        springyBot.setTgUser(tgUsers);
        springyBotServiceImpl.save(springyBot);
    }

    public void setResponse_jobPosting_management() {

        List<GroupMessageIdPostCounts> gmpcs = jobManagementServiceImpl
                .findAllByBotIdAndUserIdAndTypeWithGroupMessageIdPostCounts(
                        springyBotId.toString(), chatId_str, "jobPosting");
        List<ChannelMessageIdPostCounts> cmpcs = jobManagementServiceImpl
                .findAllByBotIdAndUserIdAndTypeWithChannelMessageIdPostCounts(
                        springyBotId.toString(), chatId_str, "jobPosting");

        boolean isPostToGroup = gmpcs.stream().anyMatch(gmpc -> gmpc.getPostCount() >= 1);
        boolean isPostToChannel = cmpcs.stream().anyMatch(cmpc -> cmpc.getPostCount() >= 1);

        if (isPostToGroup || isPostToChannel) {
            String text = "您已经发布过[招聘人才]信息，请点选[招聘和求职信息管理]进行编辑或删除信息后重新发布。";
            SendMessage response = new SendMessage(chatId_str, text);
            common.executeAsync(response);
        } else {
            SendMessage response = new SendMessage();
            response.setChatId(chatId_str);
            JobPosting jobPosting = jobManagementServiceImpl.findByUserIdAndBotIdWithJobPosting(chatId_str,
                    springyBotId.toString());
            if (jobPosting == null) {
                response.setText(TalentEnum.JOBPOSTING_DEFAULT_FORM.getText());
                log.info("No job posting found for user {}, bot id {}", chatId_str, springyBotId);
            } else {
                response.setText(jobPostingDTO.generateJobPostingResponse(jobPosting, false));
                log.info("Job posting found for user {}, bot id {}", chatId_str, springyBotId);
            }
            common.executeAsync(response);
            response.setText(TalentEnum.REMIND_EDITOR_.getText());
            common.executeAsync(response);
        }
    }

    public void setResponse_jobSeeker_management() {
        List<GroupMessageIdPostCounts> gmpcs = jobManagementServiceImpl
                .findAllByBotIdAndUserIdAndTypeWithGroupMessageIdPostCounts(
                        springyBotId.toString(), chatId_str, "jobSeeker");
        List<ChannelMessageIdPostCounts> cmpcs = jobManagementServiceImpl
                .findAllByBotIdAndUserIdAndTypeWithChannelMessageIdPostCounts(
                        springyBotId.toString(), chatId_str, "jobSeeker");

        boolean isPostToGroup = gmpcs.stream().anyMatch(gmpc -> gmpc.getPostCount() >= 1);
        boolean isPostToChannel = cmpcs.stream().anyMatch(cmpc -> cmpc.getPostCount() >= 1);

        if (isPostToGroup || isPostToChannel) {
            String text = "您已经发布过[求职人员]信息，请点选[招聘和求职信息管理]进行编辑或删除信息后重新发布。";
            SendMessage response = new SendMessage(chatId_str, text);
            common.executeAsync(response);
        } else {
            SendMessage response = new SendMessage();
            response.setChatId(chatId_str);
            JobSeeker jobSeeker = jobManagementServiceImpl.findByUserIdAndBotIdWithJobSeeker(chatId_str,
                    springyBotId.toString());
            if (jobSeeker == null) {
                response.setText(TalentEnum.JOBSEEKER_DEFAULT_FORM.getText());
                log.info("No job seeker found for user {}, bot id {}", chatId_str, springyBotId);
            } else {
                response.setText(jobSeekerDTO.generateJobSeekerResponse(jobSeeker, false));
                log.info("Job seeker found for user {}, bot id {}", chatId_str, springyBotId);
            }
            common.executeAsync(response);
            response.setText(TalentEnum.REMIND_EDITOR_.getText());
            common.executeAsync(response);
        }
    }

    public void generateTextJobPosting(Boolean isEdit) {

        String[] lines = text.split("\\r?\\n");

        TgUser tgUser = springyBotServiceImpl.findTgUserBySpringyBotIdAndUserId(springyBotId, chatId_str);

        String username = "@" + common.getUpdate().getMessage().getFrom().getUserName();

        List<JobPosting> jobPostings = jobManagementServiceImpl.findAllByUserIdAndBotIdWithJobPosting(chatId_str,
                springyBotId.toString());

        JobPosting jobPosting = jobPostings.stream()
                .filter(jp -> jp.getUserId().equals(chatId_str) && jp.getBotId().equals(springyBotId.toString()))
                .findFirst()
                .orElse(new JobPosting());

        String isSuccess = jobPostingDTO.fillJobPostingInfo(jobPosting, lines);

        if (!StringUtils.hasText(isSuccess)) {
            jobPosting.setPublisher(username);
            jobPosting.setBotId(springyBotId.toString());
            jobPosting.setUserId(chatId_str);
            jobPosting.setLastMessageId(message.getMessageId());

            String result = jobPostingDTO.generateJobPostingDetails(jobPosting);

            List<RobotChannelManagement> robotChannelManagements = springyBotServiceImpl
                    .findRobotChannelManagementBySpringyBotId(springyBotId);
            List<RobotGroupManagement> robotGroupManagements = springyBotServiceImpl
                    .findRobotGroupManagementBySpringyBotId(springyBotId);

            Iterator<RobotChannelManagement> iterator_channel = robotChannelManagements.iterator();
            Iterator<RobotGroupManagement> iterator_group = robotGroupManagements.iterator();

            while (iterator_group.hasNext()) {
                RobotGroupManagement robotGroupManagement = iterator_group.next();
                if (!result.isEmpty()) {
                    SendMessage response = new SendMessage();
                    Long groupId = robotGroupManagement.getGroupId();
                    String groupTitle = robotGroupManagement.getGroupTitle();
                    response.setChatId(groupId.toString());
                    response.setText(TalentEnum.send_recruitment_text(result));
                    response.setReplyMarkup(new TalentButton().keyboardJobMarkup());
                    response.setDisableWebPagePreview(true);

                    List<GroupMessageIdPostCounts> gmpcs = jobManagementServiceImpl
                            .findAllByBotIdAndUserIdAndTypeWithGroupMessageIdPostCounts(springyBotId.toString(),
                                    chatId_str, "jobPosting");

                    GroupMessageIdPostCounts gmpc = gmpcs.stream()
                            .filter(g -> g.getGroupId().equals(groupId)
                                    && g.getUserId().equals(chatId_str) && g.getType().equals("jobPosting"))
                            .findFirst().orElse(null);

                    if (isEdit) {
                        EditMessageText editMessageText = new EditMessageText();
                        editMessageText.setChatId(groupId.toString());
                        editMessageText.setText(TalentEnum.send_recruitment_text(result));
                        editMessageText.setMessageId(gmpc.getMessageId());
                        editMessageText.setDisableWebPagePreview(true);
                        editMessageText.setReplyMarkup(new TalentButton().keyboardJobMarkup());
                        common.executeAsync(editMessageText);
                        response.setChatId(chatId_str);
                        response.setText("[ " + groupTitle + " ]编辑成功");
                        common.executeAsync(response);

                    } else {
                        if (gmpc == null) {
                            final Integer groupMessageId = common.executeAsync(response);
                            response.setChatId(chatId_str);
                            response.setText("[ " + groupTitle + " ]发送 [招聘人才] 信息成功");
                            common.executeAsync(response);

                            gmpc = new GroupMessageIdPostCounts();
                            gmpc.setBotId(springyBotId.toString());
                            gmpc.setUserId(chatId_str);
                            gmpc.setGroupId(groupId);
                            gmpc.setGroupTitle(groupTitle);
                            gmpc.setMessageId(groupMessageId);
                            gmpc.setPostCount(1);
                            gmpc.setType("jobPosting");
                        } else {
                            if (gmpc.getPostCount() <= 0) {
                                final Integer groupMessageId = common.executeAsync(response);
                                response.setChatId(chatId_str);
                                response.setText("[ " + groupTitle + " ]发送 [招聘人才] 成功");
                                common.executeAsync(response);
                                gmpc.setMessageId(groupMessageId);
                                gmpc.setPostCount(gmpc.getPostCount() + 1);
                            } else {
                                response.setChatId(chatId_str);
                                response.setText("您已在[ " + groupTitle + " ]發送一條 [招聘人才] 信息");
                                common.executeAsync(response);
                            }
                        }
                    }
                    final GroupMessageIdPostCounts finalGmpc = gmpc;
                    gmpcs.stream()
                            .filter(g -> g.getGroupId().equals(groupId)
                                    && g.getUserId().equals(chatId_str) && g.getType().equals("jobPosting"))
                            .findFirst()
                            .ifPresentOrElse(
                                    g -> gmpcs.set(gmpcs.indexOf(g), finalGmpc),
                                    () -> gmpcs.add(finalGmpc));
                    jobPosting.setGroupMessageIdPostCounts(gmpcs);
                }
            }
            while (iterator_channel.hasNext()) {
                RobotChannelManagement robotChannelManagement = iterator_channel.next();
                if (!result.isEmpty()) {
                    SendMessage response = new SendMessage();
                    Long channelId = robotChannelManagement.getChannelId();
                    String channelTitle = robotChannelManagement.getChannelTitle();
                    response.setChatId(channelId.toString());
                    response.setText(TalentEnum.send_recruitment_text(result));
                    response.setReplyMarkup(new TalentButton().keyboardJobMarkup());
                    response.setDisableWebPagePreview(true);

                    List<ChannelMessageIdPostCounts> cmpcs = jobManagementServiceImpl
                            .findAllByBotIdAndUserIdAndTypeWithChannelMessageIdPostCounts(springyBotId.toString(),
                                    chatId_str,
                                    "jobPosting");
                    ChannelMessageIdPostCounts cmpc = cmpcs.stream().filter(c -> c.getChannelId().equals(channelId)
                            && c.getUserId().equals(chatId_str) && c.getType().equals("jobPosting"))
                            .findFirst().orElse(null);

                    if (isEdit) {
                        EditMessageText editMessageText = new EditMessageText();
                        editMessageText.setChatId(channelId.toString());
                        editMessageText.setText(TalentEnum.send_recruitment_text(result));
                        editMessageText.setMessageId(cmpc.getMessageId());
                        editMessageText.setDisableWebPagePreview(true);
                        editMessageText.setReplyMarkup(new TalentButton().keyboardJobMarkup());
                        common.executeAsync(editMessageText);
                        response.setChatId(chatId_str);
                        response.setText("[ " + channelTitle + " ]编辑成功");
                        common.executeAsync(response);
                    } else {
                        if (cmpc == null) {
                            final Integer channelMessageId = common.executeAsync(response);
                            response.setChatId(chatId_str);
                            response.setText("[ " + channelTitle + " ] 发送 [招聘人才] 信息成功");
                            common.executeAsync(response);
                            cmpc = new ChannelMessageIdPostCounts();
                            cmpc.setBotId(springyBotId.toString());
                            cmpc.setUserId(chatId_str);
                            cmpc.setChannelId(channelId);
                            cmpc.setChannelTitle(channelTitle);
                            cmpc.setMessageId(channelMessageId);
                            cmpc.setPostCount(1);
                            cmpc.setType("jobPosting");
                        } else {
                            if (cmpc.getPostCount() <= 0) {
                                final Integer channelMessageId = common.executeAsync(response);
                                response.setChatId(chatId_str);
                                response.setText("[ " + channelTitle + " ]发送 [招聘人才] 信息成功");
                                common.executeAsync(response);
                                cmpc.setMessageId(channelMessageId);
                                cmpc.setPostCount(cmpc.getPostCount() + 1);
                            } else {
                                response.setChatId(chatId_str);
                                response.setText("您已在[ " + channelTitle + " ]發送一條 [招聘人才] 信息");
                                common.executeAsync(response);
                            }
                        }
                    }
                    final ChannelMessageIdPostCounts finalCmpc = cmpc;
                    cmpcs.stream()
                            .filter(c -> c.getChannelId().equals(channelId)
                                    && c.getUserId().equals(chatId_str) && c.getType().equals("jobPosting"))
                            .findFirst()
                            .ifPresentOrElse(
                                    c -> cmpcs.set(cmpcs.indexOf(c), finalCmpc),
                                    () -> cmpcs.add(finalCmpc));
                    jobPosting.setChannelMessageIdPostCounts(cmpcs);
                }
            }
        } else {
            SendMessage response = new SendMessage();
            response.setChatId(chatId_str);
            response.setText(isSuccess);
            response.setDisableWebPagePreview(true);
            common.executeAsync(response);
        }
        final JobPosting finalJobPosting = jobPosting;
        jobPostings.stream()
                .filter(jp -> jp.getUserId().equals(chatId_str) && jp.getBotId().equals(springyBotId.toString()))
                .findFirst()
                .ifPresentOrElse(
                        jp -> jobPostings.set(jobPostings.indexOf(jp), finalJobPosting),
                        () -> jobPostings.add(finalJobPosting));
        tgUser.setJobPosting(jobPostings);
        jobManagementServiceImpl.saveTgUser(tgUser);
    }

    public void generateTextJobSeeker(Boolean isEdit) {

        String[] lines = text.split("\\r?\\n");

        TgUser tgUser = springyBotServiceImpl.findTgUserBySpringyBotIdAndUserId(springyBotId, chatId_str);

        String username = "@" + common.getUpdate().getMessage().getFrom().getUserName();

        List<JobSeeker> jobSeekers = jobManagementServiceImpl.findAllByUserIdAndBotIdWithJobSeeker(chatId_str,
                springyBotId.toString());

        JobSeeker jobSeeker = jobSeekers.stream()
                .filter(js -> js.getUserId().equals(chatId_str) && js.getBotId().equals(springyBotId.toString()))
                .findFirst()
                .orElse(new JobSeeker());

        String isSuccess = jobSeekerDTO.fillJobSeekerInfo(jobSeeker, lines);

        if (!StringUtils.hasText(isSuccess)) {
            jobSeeker.setPublisher(username);
            jobSeeker.setBotId(springyBotId.toString());
            jobSeeker.setUserId(chatId_str);
            jobSeeker.setLastMessageId(message.getMessageId());

            String result = jobSeekerDTO.generateJobSeekerDetails(jobSeeker);

            List<RobotGroupManagement> robotGroupManagements = springyBotServiceImpl
                    .findRobotGroupManagementBySpringyBotId(springyBotId);
            List<RobotChannelManagement> robotChannelManagements = springyBotServiceImpl
                    .findRobotChannelManagementBySpringyBotId(springyBotId);
            Iterator<RobotGroupManagement> iterator_group = robotGroupManagements.iterator();
            Iterator<RobotChannelManagement> iterator_channel = robotChannelManagements.iterator();

            while (iterator_group.hasNext()) {
                RobotGroupManagement robotGroupManagement = iterator_group.next();
                if (!result.isEmpty()) {
                    SendMessage response = new SendMessage();
                    Long groupId = robotGroupManagement.getGroupId();
                    String groupTitle = robotGroupManagement.getGroupTitle();
                    response.setChatId(groupId.toString());
                    response.setText(TalentEnum.send_jobsearch_text(result));
                    response.setReplyMarkup(new TalentButton().keyboardJobMarkup());
                    response.setDisableWebPagePreview(true);

                    List<GroupMessageIdPostCounts> gmpcs = jobManagementServiceImpl
                            .findAllByBotIdAndUserIdAndTypeWithGroupMessageIdPostCounts(springyBotId.toString(),
                                    chatId_str, "jobSeeker");

                    GroupMessageIdPostCounts gmpc = gmpcs.stream()
                            .filter(g -> g.getGroupId().equals(groupId) && g.getUserId().equals(chatId_str)
                                    && g.getType().equals("jobSeeker"))
                            .findFirst().orElse(null);

                    if (isEdit) {
                        EditMessageText editMessageText = new EditMessageText();
                        editMessageText.setChatId(groupId.toString());
                        editMessageText.setText(TalentEnum.send_jobsearch_text(result));
                        editMessageText.setMessageId(gmpc.getMessageId());
                        editMessageText.setReplyMarkup(new TalentButton().keyboardJobMarkup());
                        editMessageText.setDisableWebPagePreview(true);
                        common.executeAsync(editMessageText);
                        response.setChatId(chatId_str);
                        response.setText("[ " + groupTitle + " ]编辑成功");
                        common.executeAsync(response);
                    } else {
                        if (gmpc == null) {
                            final Integer groupMessageId = common.executeAsync(response);
                            response.setChatId(chatId_str);
                            response.setText("[ " + groupTitle + " ]发送 [求职人员] 成功");
                            common.executeAsync(response);
                            gmpc = new GroupMessageIdPostCounts();
                            gmpc.setBotId(jobSeeker.getBotId());
                            gmpc.setUserId(chatId_str);
                            gmpc.setGroupId(groupId);
                            gmpc.setGroupTitle(groupTitle);
                            gmpc.setMessageId(groupMessageId);
                            gmpc.setPostCount(1);
                            gmpc.setType("jobSeeker");
                        } else {
                            if (gmpc.getPostCount() <= 0) {
                                final Integer groupMessageId = common.executeAsync(response);
                                response.setChatId(chatId_str);
                                response.setText("[ " + groupTitle + " ]发送 [求职人员] 成功");
                                common.executeAsync(response);
                                gmpc.setMessageId(groupMessageId);
                                gmpc.setPostCount(gmpc.getPostCount() + 1);
                            } else {
                                response.setChatId(chatId_str);
                                response.setText("您已在[ " + groupTitle + " ]發送一條 [求职人员] 信息");
                                common.executeAsync(response);
                            }
                        }
                    }
                    final GroupMessageIdPostCounts finalGmpc = gmpc;
                    gmpcs.stream()
                            .filter(g -> g.getGroupId().equals(groupId) && g.getUserId().equals(chatId_str)
                                    && g.getType().equals("jobSeeker"))
                            .findFirst()
                            .ifPresentOrElse(g -> gmpcs.set(gmpcs.indexOf(g), finalGmpc), () -> gmpcs.add(finalGmpc));
                    jobSeeker.setGroupMessageIdPostCounts(gmpcs);
                }
            }
            while (iterator_channel.hasNext()) {
                RobotChannelManagement robotChannelManagement = iterator_channel.next();
                if (!result.isEmpty()) {
                    SendMessage response = new SendMessage();
                    Long channelId = robotChannelManagement.getChannelId();
                    String channelTitle = robotChannelManagement.getChannelTitle();
                    response.setChatId(channelId.toString());
                    response.setText(TalentEnum.send_jobsearch_text(result));
                    response.setReplyMarkup(new TalentButton().keyboardJobMarkup());
                    response.setDisableWebPagePreview(true);

                    List<ChannelMessageIdPostCounts> cmpcs = jobManagementServiceImpl
                            .findAllByBotIdAndUserIdAndTypeWithChannelMessageIdPostCounts(springyBotId.toString(),
                                    chatId_str, "jobSeeker");

                    ChannelMessageIdPostCounts cmpc = cmpcs.stream().filter(c -> c.getChannelId().equals(channelId)
                            && c.getUserId().equals(chatId_str) && c.getType().equals("jobSeeker"))
                            .findFirst().orElse(null);

                    if (isEdit) {
                        EditMessageText editMessageText = new EditMessageText();
                        editMessageText.setChatId(String.valueOf(channelId));
                        editMessageText.setText(TalentEnum.send_recruitment_text(result));
                        editMessageText.setMessageId(cmpc.getMessageId());
                        editMessageText.setReplyMarkup(new TalentButton().keyboardJobMarkup());
                        editMessageText.setDisableWebPagePreview(true);
                        common.executeAsync(editMessageText);
                        response.setChatId(chatId_str);
                        response.setText("[ " + channelTitle + " ]编辑成功");
                        common.executeAsync(response);
                    } else {
                        if (cmpc == null) {
                            final Integer channelMessageId = common.executeAsync(response);
                            response.setChatId(chatId_str);
                            response.setText("[ " + channelTitle + " ] 发送 [求职人员] 信息成功");
                            common.executeAsync(response);
                            cmpc = new ChannelMessageIdPostCounts();
                            cmpc.setBotId(springyBotId.toString());
                            cmpc.setUserId(chatId_str);
                            cmpc.setChannelId(channelId);
                            cmpc.setChannelTitle(channelTitle);
                            cmpc.setMessageId(channelMessageId);
                            cmpc.setPostCount(1);
                            cmpc.setType("jobSeeker");
                        } else {
                            if (cmpc.getPostCount() <= 0) {
                                final Integer channelMessageId = common.executeAsync(response);
                                response.setChatId(chatId_str);
                                response.setText("[ " + channelTitle + " ]发送 [求职人员] 信息成功");
                                common.executeAsync(response);
                                cmpc.setMessageId(channelMessageId);
                                cmpc.setPostCount(cmpc.getPostCount() + 1);
                            } else {
                                response.setChatId(chatId_str);
                                response.setText("您已在[ " + channelTitle + " ]發送一條 [求职人员] 信息");
                                common.executeAsync(response);
                            }
                        }
                    }
                    final ChannelMessageIdPostCounts finalCmpc = cmpc;
                    cmpcs.stream()
                            .filter(c -> c.getChannelId().equals(channelId)
                                    && c.getUserId().equals(chatId_str) && c.getType().equals("jobSeeker"))
                            .findFirst()
                            .ifPresentOrElse(
                                    c -> cmpcs.set(cmpcs.indexOf(c), finalCmpc),
                                    () -> cmpcs.add(finalCmpc));
                    jobSeeker.setChannelMessageIdPostCounts(cmpcs);
                }
            }
        } else {
            SendMessage response = new SendMessage();
            response.setChatId(chatId_str);
            response.setText(isSuccess);
            response.setDisableWebPagePreview(true);
            common.executeAsync(response);
        }
        final JobSeeker finalJobSeeker = jobSeeker;
        jobSeekers.stream()
                .filter(js -> js.getUserId().equals(chatId_str) && js.getBotId().equals(springyBotId.toString()))
                .findFirst()
                .ifPresentOrElse(
                        js -> jobSeekers.set(jobSeekers.indexOf(js), finalJobSeeker),
                        () -> jobSeekers.add(finalJobSeeker));
        tgUser.setJobSeeker(jobSeekers);
        jobManagementServiceImpl.saveTgUser(tgUser);
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

            List<TgUser> tgUsers = springyBotServiceImpl.findTgUserBySpringyBotId(springyBotId);

            TgUser tgUser = tgUsers.stream().filter(tu -> tu.getUserId().equals(chatId_str)).findAny().get();

            List<JobPosting> jobPostings = jobManagementServiceImpl.findAllByUserIdAndBotIdWithJobPosting(chatId_str,
                    springyBotId.toString());

            jobPostings.stream()
                    .filter(jp -> jp.getUserId().equals(chatId_str) && jp.getBotId().equals(springyBotId.toString()))
                    .findFirst().ifPresentOrElse(jobPosting -> {
                        JobPostingDTO jobPostingDTO = new JobPostingDTO(chatId_str, springyBotId.toString());
                        response.setText(jobPostingDTO.generateJobPostingResponse(jobPosting, true));
                        jobPostingDTO = jobPostingDTO.createJobPostingDTO(chatId_str, springyBotId.toString());

                        response.setReplyMarkup(new TalentButton().keyboard_jobPosting(jobPostingDTO, true));
                        response.setDisableWebPagePreview(true);
                        Integer messageId = common.executeAsync(response);
                        jobPosting.setLastMessageId(messageId);
                    }, () -> {
                        JobPostingDTO jobPostingDTO = new JobPostingDTO(chatId_str, springyBotId.toString());
                        response.setText(TalentEnum.JOBPOSTING_EDITOR_DEFAULT_FORM.getText());
                        response.setReplyMarkup(new TalentButton().keyboard_jobPosting(jobPostingDTO, false));
                        response.setDisableWebPagePreview(true);

                        JobPosting jobPosting = new JobPosting(chatId_str, springyBotId.toString(),
                                common.executeAsync(response));
                        jobPostings.add(jobPosting);
                    });

            tgUser.setJobPosting(jobPostings);
            jobManagementServiceImpl.saveTgUser(tgUser);

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

            List<TgUser> tgUsers = springyBotServiceImpl.findTgUserBySpringyBotId(springyBotId);

            TgUser tgUser = tgUsers.stream().filter(j -> j.getUserId().equals(chatId_str)).findAny().get();

            List<JobSeeker> jobSeekers = jobManagementServiceImpl.findAllByUserIdAndBotIdWithJobSeeker(chatId_str,
                    springyBotId.toString());

            jobSeekers.stream()
                    .filter(js -> js.getUserId().equals(chatId_str) && js.getBotId().equals(springyBotId.toString()))
                    .findFirst().ifPresentOrElse(jobSeeker -> {
                        JobSeekerDTO jobSeekerDTO = new JobSeekerDTO(chatId_str, springyBotId.toString());
                        response.setText(jobSeekerDTO.generateJobSeekerResponse(jobSeeker, true));
                        jobSeekerDTO = jobSeekerDTO.createJobSeekerDTO(chatId_str, springyBotId.toString());
                        response.setReplyMarkup(new TalentButton().keyboard_jobSeeker(jobSeekerDTO, true));
                        response.setDisableWebPagePreview(true);
                        Integer messageId = common.executeAsync(response);
                        jobSeeker.setLastMessageId(messageId);
                    }, () -> {
                        JobSeekerDTO jobSeekerDTO = new JobSeekerDTO(chatId_str, springyBotId.toString());
                        response.setText(TalentEnum.JOBSEEKE_REDITOR_DEFAULT_FORM.getText());
                        response.setReplyMarkup(new TalentButton().keyboard_jobSeeker(jobSeekerDTO, true));
                        response.setDisableWebPagePreview(true);
                        JobSeeker js = new JobSeeker(chatId_str, springyBotId.toString(),
                                common.executeAsync(response));
                        jobSeekers.add(js);
                    });
            tgUser.setJobSeeker(jobSeekers);
            jobManagementServiceImpl.saveTgUser(tgUser);

        } else {
            response.setText("未发布求职");
            response.setDisableWebPagePreview(true);
            common.executeAsync(response);
        }
    }

    @Override
    public void handler() {
        throw new UnsupportedOperationException("Unimplemented method 'handler'");
    }

}
