package com.lwdevelop.bot.chatMembershipHandlers;

import com.lwdevelop.bot.bots.utils.Common;
import com.lwdevelop.entity.InvitationThreshold;
import com.lwdevelop.entity.RecordChannelUsers;
import com.lwdevelop.entity.RobotChannelManagement;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JoinChannel extends BaseHandler {

    public JoinChannel(Common common) {
        super(common);

        this.robotChannelManagement = springyBotServiceImpl
                .findRobotChannelManagementBySpringyBotId(springyBotId);
        this.recordChannelUsers = springyBotServiceImpl.findRecordChannelUsersBySpringyBotId(springyBotId);

    }

    @Override
    public void handler() {
        if (this.isBot == null) {
            return;
        }
        if (this.isBot) {
            handleBotJoinChannel();
        } else {
            handleUserJoinGroup();
            updateRedisAndSpringyBot();
        }
        this.springyBotServiceImpl.save(this.springyBot);
        log.info("{} -> {} join {} channel", formatBot, formatUser, formatChat);
    }

    private void handleBotJoinChannel() {
        this.robotChannelManagement.stream()
                .filter(rcm -> hasTarget(rcm))
                .findAny()
                .ifPresentOrElse(rcm -> {
                    rcm = this.initializeRobotChannelManagement(rcm);
                }, () -> {
                    RobotChannelManagement robotChannelManagement = new RobotChannelManagement();
                    RobotChannelManagement rcm = this.initializeRobotChannelManagement(robotChannelManagement);
                    this.robotChannelManagement.add(rcm);
                });
        this.springyBot.setRobotChannelManagement(this.robotChannelManagement);
        this.redisUtils.set("RobotChannelManagement_" + this.springyBotId, this.robotChannelManagement);
    }

    private void handleUserJoinGroup() {
        if (!this.from.getIsBot()) {
            // user join group
            this.invitationThreshold.stream()
                    .filter(it -> hasTarget(it, "channel"))
                    .findAny()
                    .ifPresentOrElse(it -> {
                        it = this.initializeInvitationThreshold(it, "channel");
                    }, () -> {
                        InvitationThreshold invitationThreshold = new InvitationThreshold();
                        InvitationThreshold it = this.initializeInvitationThreshold(invitationThreshold, "channel");
                        this.invitationThreshold.add(it);
                    });

            this.recordChannelUsers.stream()
                    .filter(rcu -> hasTarget(rcu))
                    .findAny()
                    .ifPresentOrElse(rcu -> {
                        rcu = this.initializeRecordChannelUsers(rcu);
                    }, () -> {
                        RecordChannelUsers recordChannelUsers = new RecordChannelUsers();
                        RecordChannelUsers rcu = this.initializeRecordChannelUsers(recordChannelUsers);
                        this.recordChannelUsers.add(rcu);
                    });
        }
    }

    private void updateRedisAndSpringyBot() {
        this.redisUtils.set("InvitationThreshold_" + this.springyBotId, this.invitationThreshold);
        this.redisUtils.set("RecordChannelUsers_" + this.springyBotId, this.recordChannelUsers);
        this.springyBot.setInvitationThreshold(this.invitationThreshold);
        this.springyBot.setRecordChannelUsers(this.recordChannelUsers);
    }

    private RobotChannelManagement initializeRobotChannelManagement(RobotChannelManagement robotChannelManagement) {
        robotChannelManagement.setBotId(this.botId);
        robotChannelManagement.setInviteId(this.invitedId);
        robotChannelManagement.setInviteFirstname(this.inviteFirstname);
        robotChannelManagement.setInviteUsername(this.inviteUsername);
        robotChannelManagement.setInviteLastname(this.inviteLastname);
        robotChannelManagement.setChannelId(this.chatId);
        robotChannelManagement.setChannelTitle(this.chatTitle);
        robotChannelManagement.setStatus(true);
        robotChannelManagement.setType(this.chatType);
        return robotChannelManagement;
    }

    private RecordChannelUsers initializeRecordChannelUsers(RecordChannelUsers recordChannelUsers) {
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
