package com.lwdevelop.bot.chatMembershipHandlers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import com.fasterxml.jackson.core.type.TypeReference;
import com.lwdevelop.bot.bots.utils.Common;
import com.lwdevelop.entity.Config;
import com.lwdevelop.entity.InvitationBonusUser;
import com.lwdevelop.entity.InvitationThreshold;
import com.lwdevelop.entity.RecordGroupUsers;
import com.lwdevelop.entity.RobotGroupManagement;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JoinGroup extends BaseHandler {

    public JoinGroup(Common common) {
        super(common);
        Config config = redisUtils.get("Config_" + springyBotId,
                new TypeReference<Config>() {
                });
        this.invitationBonusSet = config.getInvitationBonusSet();
        this.inviteMembers = config.getInviteMembers();
        this.inviteEarnedOutstand = config.getInviteEarnedOutstand();
        this.minimumPayout = config.getMinimumPayout();
        this.contactPerson = config.getContactPerson();
        this.robotGroupManagement = springyBotServiceImpl
                .findRobotGroupManagementBySpringyBotId(springyBotId);
        this.recordGroupUsers = springyBotServiceImpl.findRecordGroupUsersBySpringyBotId(springyBotId);
        this.invitationBonusUsers = springyBotServiceImpl.findInvitationBonusUserBySpringyBotId(springyBotId);
    }

    @Override
    public void handler() {
        if (this.isBot == null) {
            return;
        }
        if (this.isBot) {
            handleBotJoinGroup();
        } else {
            handleUserJoinGroup();
        }
        this.springyBotServiceImpl.save(this.springyBot);

        log.info("{} -> {} join {} group", formatBot, formatUser, formatChat);

    }

    private void handleBotJoinGroup() {
        this.robotGroupManagement.stream()
                .filter(rgm -> hasTarget(rgm))
                .findAny()
                .ifPresentOrElse(rgm -> {
                    rgm = this.initializeRobotGroupManagement(rgm);
                }, () -> {
                    RobotGroupManagement robotGroupManagement = new RobotGroupManagement();
                    RobotGroupManagement rgm = this.initializeRobotGroupManagement(robotGroupManagement);
                    this.robotGroupManagement.add(rgm);
                });
        this.springyBot.setRobotGroupManagement(this.robotGroupManagement);
    }

    private void handleUserJoinGroup() {
        if (!this.from.getIsBot()) {
            handleInvitationThreshold();
            handleRecordGroupUsers();
            handleInvitationBonusUsers();
            updateRedisAndSpringyBot();
        }
    }

    private void handleInvitationThreshold() {
        this.invitationThreshold.stream()
                .filter(it -> this.hasTarget(it, "group"))
                .findAny()
                .ifPresentOrElse(it -> {
                    it = this.initializeInvitationThreshold(it, "group");
                }, () -> {
                    InvitationThreshold invitationThreshold = new InvitationThreshold();
                    InvitationThreshold it = this.initializeInvitationThreshold(invitationThreshold, "group");
                    this.invitationThreshold.add(it);
                });
    }

    private void handleRecordGroupUsers() {
        this.recordGroupUsers.stream()
                .filter(rgu -> hasTarget(rgu))
                .findAny()
                .ifPresentOrElse(rgu -> {
                    rgu = this.initializeRecordGroupUsers(rgu);
                }, () -> {
                    RecordGroupUsers recordGroupUsers = new RecordGroupUsers();
                    RecordGroupUsers rgu = this.initializeRecordGroupUsers(recordGroupUsers);
                    this.recordGroupUsers.add(rgu);
                });
    }

    private void handleInvitationBonusUsers() {
        if (invitationBonusSet && !this.inviteId.equals(this.invitedId)) {
            this.invitationBonusUsers.stream()
                    .filter(ib -> hasTarget(ib))
                    .findAny()
                    .ifPresentOrElse(ib -> {
                        List<String> invitedIds = ib.getPendingInvitations();
                        invitedIds.removeIf(String::isEmpty);
                        boolean containsInvited = invitedIds.contains(this.invitedId.toString());
                        if (!containsInvited) {
                            invitedIds.add(this.invitedId.toString());
                            ib.setPendingInvitations(invitedIds);
                            int validInviteCount = invitedIds.size();

                            BigDecimal outstandingAmount = ib.getOutstandingAmount()
                                    .add(this.inviteEarnedOutstand);
                            ib.setOutstandingAmount(outstandingAmount);

                            SendMessage sendMessage = new SendMessage();
                            sendMessage.setChatId(String.valueOf(this.chatId));
                            BigDecimal settlementAmount = ib.getSettlementAmount();
                            sendMessage.setText(
                                    "您邀请" + validInviteCount + "位成员，赚取" + outstandingAmount
                                            + "元未结算，已经结算" + settlementAmount + "元，满"
                                            + this.minimumPayout + "元请联系 "
                                            + this.contactPerson + " 结算。");

                            Integer msgId = this.common.executeAsync(sendMessage);
                            DeleteMessage deleteMessage = new DeleteMessage(this.chatId.toString(),
                                    msgId);
                            this.common.deleteMessageTask(deleteMessage, 7);

                        }
                    }, () -> {
                        InvitationBonusUser invitationBonusUser = new InvitationBonusUser();
                        InvitationBonusUser ib = this.initializeInvitationBonusUser(invitationBonusUser);
                        this.invitationBonusUsers.add(ib);
                        SendMessage sendMessage = new SendMessage();
                        sendMessage.setChatId(String.valueOf(this.chatId));
                        sendMessage.setText(
                                "您邀请1位成员，赚取" + this.inviteEarnedOutstand + "元未结算，满" + this.minimumPayout
                                        + "元请联系 " + this.contactPerson + " 结算。");
                        Integer msgId = this.common.executeAsync(sendMessage);
                        DeleteMessage deleteMessage = new DeleteMessage(this.chatId.toString(),
                                msgId);
                        this.common.deleteMessageTask(deleteMessage, 1000);
                    });
        }
    }

    private void updateRedisAndSpringyBot() {
        this.redisUtils.set("RecordGroupUsers_" + this.springyBotId, this.recordGroupUsers);
        this.redisUtils.set("InvitationThreshold_" + this.springyBotId, this.invitationThreshold);
        this.springyBot.setInvitationThreshold(this.invitationThreshold);
        this.springyBot.setRecordGroupUsers(this.recordGroupUsers);
        this.springyBot.setInvitationBonusUser(this.invitationBonusUsers);
    }

    private RobotGroupManagement initializeRobotGroupManagement(RobotGroupManagement robotGroupManagement) {
        robotGroupManagement.setBotId(this.botId);
        robotGroupManagement.setInviteId(this.inviteId);
        robotGroupManagement.setInviteFirstname(this.inviteFirstname);
        robotGroupManagement.setInviteUsername(this.inviteUsername);
        robotGroupManagement.setInviteLastname(this.inviteLastname);
        robotGroupManagement.setGroupId(this.chatId);
        robotGroupManagement.setGroupTitle(this.chatTitle);
        // robotGroupManagement.setLink(this.common.getInviteLink());
        robotGroupManagement.setStatus(true);
        return robotGroupManagement;
    }

    private RecordGroupUsers initializeRecordGroupUsers(RecordGroupUsers recordGroupUsers) {
        recordGroupUsers.setBotId(this.botId);
        recordGroupUsers.setGroupId(this.chatId);
        recordGroupUsers.setGroupTitle(this.chatTitle);
        recordGroupUsers.setFirstname(this.invitedFirstname);
        recordGroupUsers.setLastname(this.invitedLastname);
        recordGroupUsers.setStatus(true);
        recordGroupUsers.setUserId(this.invitedId);
        recordGroupUsers.setUsername(this.invitedUsername);
        return recordGroupUsers;
    }

    private InvitationBonusUser initializeInvitationBonusUser(InvitationBonusUser invitationBonusUser) {
        invitationBonusUser.setBotId(this.botId);
        invitationBonusUser.setUserId(this.inviteId);
        invitationBonusUser.setFirstname(this.inviteFirstname);
        invitationBonusUser.setUsername(this.inviteUsername);
        invitationBonusUser.setLastname(this.inviteLastname);
        invitationBonusUser.setChatId(this.chatId);
        invitationBonusUser.setChatTitle(this.chatTitle);
        invitationBonusUser.setOutstandingAmount(this.inviteEarnedOutstand);
        invitationBonusUser.setSettlementAmount(BigDecimal.valueOf(0));
        List<String> invitedIds = new ArrayList<>();
        invitedIds.add(this.invitedId.toString());
        invitationBonusUser.setPendingInvitations(invitedIds);
        invitationBonusUser.setAccumulatedInvitations(new ArrayList<>());
        invitationBonusUser.setOutstandingAmount(inviteEarnedOutstand);
        return invitationBonusUser;
    }

}
