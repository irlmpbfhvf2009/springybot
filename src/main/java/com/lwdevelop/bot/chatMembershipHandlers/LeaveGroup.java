package com.lwdevelop.bot.chatMembershipHandlers;

import java.math.BigDecimal;

import com.lwdevelop.bot.bots.utils.Common;
import com.lwdevelop.entity.InvitationBonusUser;
import com.lwdevelop.entity.InvitationThreshold;
import com.lwdevelop.entity.RecordChannelUsers;
import com.lwdevelop.entity.RecordGroupUsers;
import com.lwdevelop.entity.RobotChannelManagement;
import com.lwdevelop.entity.RobotGroupManagement;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LeaveGroup extends BaseHandler {

    public LeaveGroup(Common common) {
        super(common);
        this.robotGroupManagement = springyBotServiceImpl
                .findRobotGroupManagementBySpringyBotId(common.getSpringyBotId());
        this.recordGroupUsers = springyBotServiceImpl.findRecordGroupUsersBySpringyBotId(common.getSpringyBotId());
        this.invitationBonusUsers = springyBotServiceImpl
                .findInvitationBonusUserBySpringyBotId(common.getSpringyBotId());

    }

    @Override
    public void handler() {
        if (this.isBot != null) {
            if (this.isBot) {
                this.robotGroupManagement.stream()
                        .filter(rgm -> hasTarget(rgm))
                        .findAny()
                        .ifPresent(g -> {
                            g.setStatus(false);
                        });
                this.springyBot.setRobotGroupManagement(this.robotGroupManagement);
            } else {
                this.recordGroupUsers.stream()
                        .filter(rgu -> hasTarget(rgu))
                        .findAny()
                        .ifPresent(rgu -> {
                            rgu.setStatus(false);
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

                this.invitationBonusUsers.stream()
                        .filter(ibu -> ibu.getChatId().equals(this.chatId))
                        .forEach(ibu -> {
                            boolean removed = ibu.getPendingInvitations()
                                    .removeIf(invitedId -> invitedId.equals(this.userId.toString()));
                            if (removed) {
                                BigDecimal outstandingAmount = ibu.getOutstandingAmount();
                                BigDecimal original = this.springyBot.getConfig().getInviteEarnedOutstand();
                                outstandingAmount = outstandingAmount.subtract(original);
                                ibu.setOutstandingAmount(outstandingAmount);
                            }
                        });

                this.springyBot.setRecordGroupUsers(this.recordGroupUsers);
                this.springyBot.setInvitationThreshold(this.invitationThreshold);
                this.springyBot.setInvitationBonusUser(this.invitationBonusUsers);
            }
            this.redisUtils.set("RecordGroupUsers_" + this.common.getSpringyBotId(), this.recordGroupUsers);
            this.redisUtils.set("InvitationThreshold_" + this.common.getSpringyBotId(), this.invitationThreshold);
            this.springyBotServiceImpl.save(springyBot);

            log.info("{} -> {} leave {} group", formatBot, formatUser, formatChat);
        }

    }

    @Override
    protected Boolean hasTarget(RobotGroupManagement rgm) {
        return rgm.getGroupId().equals(this.chatId);
    }

    private Boolean hasTarget(RecordGroupUsers recordGroupUsers) {
        return recordGroupUsers.getUserId().equals(this.userId);
    }

    private Boolean hasTarget_invite(InvitationThreshold it) {
        return it.getInviteId().equals(this.userId);
    }

    private Boolean hasTarget_invited(InvitationThreshold it) {
        return it.getInvitedId().equals(this.userId);
    }

    @Override
    protected Boolean hasTarget(RobotChannelManagement rcm) {
        return null;
    }

    @Override
    protected Boolean hasTarget(RecordChannelUsers rcu) {
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
