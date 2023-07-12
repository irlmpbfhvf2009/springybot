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
public class LeaveChannel extends BaseHandler {

    public LeaveChannel(Common common) {
        super(common);
        this.robotChannelManagement = springyBotServiceImpl
                .findRobotChannelManagementBySpringyBotId(springyBotId);
        this.recordChannelUsers = springyBotServiceImpl.findRecordChannelUsersBySpringyBotId(springyBotId);

    }

    @Override
    public void handler() {
        if (this.isBot != null) {

            if (this.isBot) {
                this.robotChannelManagement.stream()
                        .filter(rgm -> hasTarget(rgm))
                        .findAny()
                        .ifPresent(c -> {
                            c.setStatus(false);
                        });
                this.springyBot.setRobotChannelManagement(this.robotChannelManagement);
            } else {
                this.recordChannelUsers.stream()
                        .filter(rcu -> hasTarget(rcu))
                        .findAny()
                        .ifPresent(rcu -> {
                            rcu.setStatus(false);
                        });

                this.invitationThreshold.stream()
                        .filter(it -> hasTarget_invite(it) || hasTarget_invited(it))
                        .findAny()
                        .ifPresent(g -> {
                            if (hasTarget_invite(g)) {
                                g.setInviteStatus(false);
                            }
                            if (hasTarget_invited(g)) {
                                g.setInvitedStatus(false);
                            }
                        });

                this.springyBot.setRecordChannelUsers(this.recordChannelUsers);
                this.springyBot.setInvitationThreshold(this.invitationThreshold);

                this.redisUtils.set("RecordChannelUsers_" + this.common.getSpringyBotId(), this.recordChannelUsers);
                this.redisUtils.set("InvitationThreshold_" + this.common.getSpringyBotId(), this.invitationThreshold);
            }
            this.springyBotServiceImpl.save(this.springyBot);

            log.info("{} -> {} leave {} channel", formatBot, formatUser, formatChat);
        }

    }

    @Override
    protected Boolean hasTarget(RobotChannelManagement rcm) {
        return rcm.getChannelId().equals(this.chatId);
    }

    @Override
    protected Boolean hasTarget(RecordChannelUsers rcu) {
        return rcu.getUserId().equals(this.userId)
                && rcu.getChannelId().equals(this.chatId);
    }

    private Boolean hasTarget_invite(InvitationThreshold it) {
        return it.getInviteId().equals(this.userId);
    }

    private Boolean hasTarget_invited(InvitationThreshold it) {
        return it.getInvitedId().equals(this.userId);
    }

    @Override
    protected Boolean hasTarget(RobotGroupManagement rgm) {
        return null;
    }

    @Override
    protected Boolean hasTarget(InvitationThreshold it) {
        return null;
    }

    @Override
    protected RobotChannelManagement getRobotChannelManagement(RobotChannelManagement robotChannelManagement) {
        return null;
    }

    @Override
    protected InvitationThreshold getInvitationThreshold(InvitationThreshold invitationThreshold) {
        return null;
    }

    @Override
    protected RecordChannelUsers getRecordChannelUsers(RecordChannelUsers recordChannelUsers) {
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
