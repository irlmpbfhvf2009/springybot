package com.lwdevelop.bot._factory.chatMembership;

import com.lwdevelop.bot.utils.Common;
import com.lwdevelop.entity.InvitationBonusUser;
import com.lwdevelop.entity.InvitationThreshold;
import com.lwdevelop.entity.RecordChannelUsers;
import com.lwdevelop.entity.RecordGroupUsers;
import com.lwdevelop.entity.RobotChannelManagement;
import com.lwdevelop.entity.RobotGroupManagement;

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
        if (this.isBot != null) {

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
                    this.redisUtils.set("InvitationThreshold_" + this.common.getSpringyBotId(),
                            this.invitationThreshold);
                    this.redisUtils.set("RecordChannelUsers_" + this.common.getSpringyBotId(), this.recordChannelUsers);

                }
            }
            this.springyBotServiceImpl.save(this.springyBot);

            log.info("{} -> {} join {} channel", formatBot, formatUser, formatChat);

        }

    }

    @Override
    protected Boolean hasTarget(RobotChannelManagement rcm) {
        return rcm.getChannelId().equals(this.chatId);
    }

    @Override
    protected Boolean hasTarget(RecordChannelUsers rcu) {
        return rcu.getUserId().equals(this.invitedId)
                && rcu.getChannelId().equals(this.chatId);
    }

    @Override
    protected Boolean hasTarget(InvitationThreshold it) {
        return it.getChatId().equals(this.chatId) && it.getType().equals("channel")
                && it.getInvitedId().equals(this.invitedId);
    }

    @Override
    protected RobotChannelManagement getRobotChannelManagement(RobotChannelManagement robotChannelManagement) {
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

    @Override
    protected InvitationThreshold getInvitationThreshold(InvitationThreshold invitationThreshold) {
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

    @Override
    protected RecordChannelUsers getRecordChannelUsers(RecordChannelUsers recordChannelUsers) {
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

    @Override
    protected Boolean hasTarget(RobotGroupManagement rgm) {
        return null;
    }

    @Override
    protected RecordGroupUsers getRecordGroupUsers(RecordGroupUsers recordGroupUsers) {
        return null;
    }

    @Override
    protected InvitationBonusUser getInvitationBonusUser(InvitationBonusUser invitationBonusUser) {
        return null;
    }

}
