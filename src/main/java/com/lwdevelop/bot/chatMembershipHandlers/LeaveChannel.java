package com.lwdevelop.bot.chatMembershipHandlers;

import com.lwdevelop.bot.bots.utils.Common;
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
        if (this.isBot == null) {
            return;
        }
        if (this.isBot) {
            this.robotChannelManagement.stream()
                    .filter(rgm -> hasTarget(rgm))
                    .findAny()
                    .ifPresent(c -> {
                        c.setStatus(false);
                    });
            this.springyBot.setRobotChannelManagement(this.robotChannelManagement);
            this.redisUtils.set("RobotChannelManagement_" + this.common.getSpringyBotId(), this.robotChannelManagement);
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
