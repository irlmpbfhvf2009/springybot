package com.lwdevelop.bot.chatMembershipHandlers;

import java.math.BigDecimal;
import com.lwdevelop.bot.bots.utils.Common;
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
        if (this.isBot == null) {
            return;
        }
        if (this.isBot) {
            handleBotLeaveGroup();
        } else {
            handleUserLeaveGroup();
            updateRedisAndSpringyBot();
        }

        this.springyBotServiceImpl.save(springyBot);

        log.info("{} -> {} leave {} group", formatBot, formatUser, formatChat);
    }

    private void handleBotLeaveGroup() {
        this.robotGroupManagement.stream()
                .filter(rgm -> hasTarget(rgm))
                .findAny()
                .ifPresent(g -> {
                    g.setStatus(false);
                });
        this.springyBot.setRobotGroupManagement(this.robotGroupManagement);
    }

    private void handleUserLeaveGroup() {
        handleInvitationThreshold();
        handleRecordGroupUsers();
        handleInvitationBonusUsers();
    }

    private void handleInvitationThreshold() {
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
    }

    private void handleRecordGroupUsers() {
        this.recordGroupUsers.stream()
                .filter(rgu -> hasTarget(rgu))
                .findAny()
                .ifPresent(rgu -> {
                    rgu.setStatus(false);
                });
    }

    private void handleInvitationBonusUsers() {
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
    }

    private void updateRedisAndSpringyBot() {
        this.redisUtils.set("RecordGroupUsers_" + this.common.getSpringyBotId(), this.recordGroupUsers);
        this.redisUtils.set("InvitationThreshold_" + this.common.getSpringyBotId(), this.invitationThreshold);
        this.springyBot.setRecordGroupUsers(this.recordGroupUsers);
        this.springyBot.setInvitationThreshold(this.invitationThreshold);
        this.springyBot.setInvitationBonusUser(this.invitationBonusUsers);
    }

}
