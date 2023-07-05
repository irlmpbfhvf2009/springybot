package com.lwdevelop.botfactory.event;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.ChatMemberUpdated;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;

import com.lwdevelop.botfactory.Common;
import com.lwdevelop.entity.InvitationThreshold;
import com.lwdevelop.entity.RecordChannelUsers;
import com.lwdevelop.entity.RobotChannelManagement;
import com.lwdevelop.entity.SpringyBot;
import com.lwdevelop.service.impl.SpringyBotServiceImpl;
import com.lwdevelop.utils.RedisUtils;
import com.lwdevelop.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JoinChannel {

    @Autowired
    private SpringyBotServiceImpl springyBotServiceImpl = SpringUtils.getApplicationContext()
            .getBean(SpringyBotServiceImpl.class);

    @Autowired
    private RedisUtils redisUtils = SpringUtils.getApplicationContext()
            .getBean(RedisUtils.class);

    private Common common;
    private Long botId;
    private Long inviteId;
    private String inviteFirstname;
    private String inviteUsername;
    private String inviteLastname;
    private Long invitedId;
    private String invitedFirstname;
    private String invitedUsername;
    private String invitedLastname;
    private Long chatId;
    private String chatTitle;
    private SpringyBot springyBot;
    private User user;
    private User from;
    private Boolean isBot;
    private List<RobotChannelManagement> robotChannelManagement;
    private List<InvitationThreshold> invitationThreshold;
    private List<RecordChannelUsers> recordChannelUsers;

    public JoinChannel(Common common) {

        this.common = common;
        this.botId = common.getBotId();
        this.springyBot = springyBotServiceImpl.findById(common.getSpringyBotId()).get();
        this.robotChannelManagement = springyBotServiceImpl
                .findRobotChannelManagementBySpringyBotId(common.getSpringyBotId());
        this.invitationThreshold = springyBotServiceImpl
                .findInvitationThresholdBySpringyBotId(common.getSpringyBotId());
        this.recordChannelUsers = springyBotServiceImpl.findRecordChannelUsersBySpringyBotId(common.getSpringyBotId());

        Update update = common.getUpdate();
        ChatMemberUpdated chatMemberUpdated = null;
        ChatMember chatMember = null;

        if (update.hasMyChatMember()) {
            chatMemberUpdated = update.getMyChatMember();
            chatMember = chatMemberUpdated.getNewChatMember();
        } else if (update.hasChatMember()) {
            chatMemberUpdated = update.getChatMember();
            chatMember = chatMemberUpdated.getNewChatMember();
        }
        this.isBot = this.isBot(chatMember);

        if (chatMemberUpdated != null && chatMember != null) {
            this.user = chatMember.getUser();
            this.from = chatMemberUpdated.getFrom();
            this.chatId = chatMemberUpdated.getChat().getId();
            this.chatTitle = chatMemberUpdated.getChat().getTitle();
            this.inviteId = this.from.getId();
            this.inviteFirstname = (this.from.getFirstName() != null) ? this.from.getFirstName() : "";
            this.inviteUsername = (this.from.getUserName() != null) ? this.from.getUserName() : "";
            this.inviteLastname = (this.from.getLastName() != null) ? this.from.getLastName() : "";
            this.invitedId = this.user.getId();
            this.invitedFirstname = (this.user.getFirstName() != null) ? this.user.getFirstName() : "";
            this.invitedUsername = (this.user.getUserName() != null) ? this.user.getUserName() : "";
            this.invitedLastname = (this.user.getLastName() != null) ? this.user.getLastName() : "";
        }

    }

    public void handler() {
        if (this.isBot != null) {
            String formatChat = this.chatTitle + "[" + this.chatId + "]";
            String formatBot = common.formatBot();
            String formatUser = common.formatUser(this.user);

            if (this.isBot) {
                // bot join channel
                this.robotChannelManagement.stream()
                        .filter(rcm -> hasTarget(rcm))
                        .findAny()
                        .ifPresentOrElse(rcm -> {
                            rcm = this.getRobotChannelManagement(rcm);
                        }, () -> {
                            RobotChannelManagement robotChannelManagement = new RobotChannelManagement();
                            RobotChannelManagement rcm = this.getRobotChannelManagement(robotChannelManagement);
                            this.robotChannelManagement.add(rcm);
                        });
                this.springyBot.setRobotChannelManagement(this.robotChannelManagement);
            } else {
                if (!this.from.getIsBot()) {
                    // user join group
                    this.invitationThreshold.stream()
                            .filter(it -> hasTarget(it))
                            .findAny()
                            .ifPresentOrElse(it -> {
                                it = this.getInvitationThreshold(it);
                            }, () -> {
                                InvitationThreshold invitationThreshold = new InvitationThreshold();
                                InvitationThreshold it = this.getInvitationThreshold(invitationThreshold);
                                this.invitationThreshold.add(it);
                            });

                    this.recordChannelUsers.stream()
                            .filter(rcu -> hasTarget(rcu))
                            .findAny()
                            .ifPresentOrElse(rcu -> {
                                rcu = this.getRecordChannelUsers(rcu);
                            }, () -> {
                                RecordChannelUsers recordChannelUsers = new RecordChannelUsers();
                                RecordChannelUsers rcu = this.getRecordChannelUsers(recordChannelUsers);
                                this.recordChannelUsers.add(rcu);
                            });
                    this.springyBot.setInvitationThreshold(this.invitationThreshold);
                    this.springyBot.setRecordChannelUsers(this.recordChannelUsers);
                    this.redisUtils.set("InvitationThreshold_" + this.common.getSpringyBotId(), this.invitationThreshold);
                    this.redisUtils.set("RecordChannelUsers_" + this.common.getSpringyBotId(), this.recordChannelUsers);

                }
            }
            this.springyBotServiceImpl.save(this.springyBot);

            log.info("{} -> {} join {} channel", formatBot, formatUser, formatChat);

        }

        this.springyBotServiceImpl = null;
        this.redisUtils = null;
        this.springyBot = null;
    }

    private Boolean isBot(ChatMember chatMember) {
        if (chatMember == null) {
            return null;
        }
        Boolean isBot = chatMember.getUser().getIsBot();
        return isBot;
    }

    private Boolean hasTarget(RobotChannelManagement rcm) {
        return rcm.getChannelId().equals(this.chatId);
    }

    private Boolean hasTarget(InvitationThreshold it) {
        return it.getChatId().equals(this.chatId) && it.getType().equals("channel")
                && it.getInvitedId().equals(this.invitedId);
    }

    private Boolean hasTarget(RecordChannelUsers rcu) {
        return rcu.getUserId().equals(this.invitedId)
                && rcu.getChannelId().equals(this.chatId);
    }

    private RobotChannelManagement getRobotChannelManagement(RobotChannelManagement robotChannelManagement) {
        robotChannelManagement.setBotId(this.botId);
        robotChannelManagement.setInviteId(this.invitedId);
        robotChannelManagement.setInviteFirstname(this.inviteFirstname);
        robotChannelManagement.setInviteUsername(this.inviteUsername);
        robotChannelManagement.setInviteLastname(this.inviteLastname);
        robotChannelManagement.setChannelId(this.chatId);
        robotChannelManagement.setChannelTitle(this.chatTitle);
        robotChannelManagement.setLink(this.common.getInviteLink());
        robotChannelManagement.setStatus(true);
        return robotChannelManagement;
    }

    private InvitationThreshold getInvitationThreshold(InvitationThreshold invitationThreshold) {
        invitationThreshold.setBotId(this.botId);
        invitationThreshold.setChatId(this.chatId);
        invitationThreshold.setChatTitle(this.chatTitle);
        invitationThreshold.setInviteId(this.inviteId);
        invitationThreshold.setInviteFirstname(this.inviteFirstname);
        invitationThreshold.setInviteUsername(this.inviteUsername);
        invitationThreshold.setInviteLastname(this.inviteLastname);
        invitationThreshold.setInvitedId(this.invitedId);
        invitationThreshold.setInvitedFirstname(this.invitedFirstname);
        invitationThreshold.setInvitedUsername(this.invitedUsername);
        invitationThreshold.setInvitedLastname(this.invitedLastname);
        invitationThreshold.setInvitedStatus(true);
        invitationThreshold.setType("channel");
        invitationThreshold.setInvitedStatus(true);
        invitationThreshold.setInviteStatus(true);
        return invitationThreshold;
    }

    private RecordChannelUsers getRecordChannelUsers(RecordChannelUsers recordChannelUsers) {
        recordChannelUsers.setBotId(this.botId);
        recordChannelUsers.setChannelId(this.chatId);
        recordChannelUsers.setChannelTitle(this.chatTitle);
        recordChannelUsers.setFirstname(this.invitedFirstname);
        recordChannelUsers.setLastname(this.invitedLastname);
        recordChannelUsers.setStatus(true);
        recordChannelUsers.setUserId(this.invitedId);
        recordChannelUsers.setUsername(this.invitedUsername);
        return recordChannelUsers;
    }

}
