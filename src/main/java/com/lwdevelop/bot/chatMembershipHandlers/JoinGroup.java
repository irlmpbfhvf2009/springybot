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
import com.lwdevelop.entity.RecordChannelUsers;
import com.lwdevelop.entity.RecordGroupUsers;
import com.lwdevelop.entity.RobotChannelManagement;
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
        if (this.isBot != null) {

            if (this.isBot) {
                // bot join group
                this.robotGroupManagement.stream()
                        .filter(rgm -> hasTarget(rgm))
                        .findAny()
                        .ifPresentOrElse(rgm -> {
                            rgm = this.getRobotGroupManagement(rgm);
                        }, () -> {
                            RobotGroupManagement robotGroupManagement = new RobotGroupManagement();
                            RobotGroupManagement rgm = this.getRobotGroupManagement(robotGroupManagement);
                            this.robotGroupManagement.add(rgm);
                        });
                this.springyBot.setRobotGroupManagement(this.robotGroupManagement);
            } else {
                if (!this.from.getIsBot()) {
                    // user join group
                    this.invitationThreshold.stream()
                            .filter(it -> this.hasTarget(it))
                            .findAny()
                            .ifPresentOrElse(it -> {
                                it = this.getInvitationThreshold(it);
                            }, () -> {
                                InvitationThreshold invitationThreshold = new InvitationThreshold();
                                InvitationThreshold it = this.getInvitationThreshold(invitationThreshold);
                                this.invitationThreshold.add(it);
                            });
                    this.recordGroupUsers.stream()
                            .filter(rgu -> hasTarget(rgu))
                            .findAny()
                            .ifPresentOrElse(rgu -> {
                                rgu = this.getRecordGroupUsers(rgu);
                            }, () -> {
                                RecordGroupUsers recordGroupUsers = new RecordGroupUsers();
                                RecordGroupUsers rgu = this.getRecordGroupUsers(recordGroupUsers);
                                this.recordGroupUsers.add(rgu);
                            });
                    this.redisUtils.set("RecordGroupUsers_" + this.common.getSpringyBotId(), this.recordGroupUsers);
                    this.redisUtils.set("InvitationThreshold_" + this.common.getSpringyBotId(),
                            this.invitationThreshold);
                    this.springyBot.setInvitationThreshold(this.invitationThreshold);
                    this.springyBot.setRecordGroupUsers(this.recordGroupUsers);

                    Boolean isSelf = this.inviteId.equals(this.invitedId);

                    if (invitationBonusSet && !isSelf) {
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
                                    InvitationBonusUser ib = this.getInvitationBonusUser(invitationBonusUser);
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
                        this.springyBot.setInvitationBonusUser(this.invitationBonusUsers);
                    }
                }
            }
            this.springyBotServiceImpl.save(this.springyBot);

            log.info("{} -> {} join {} group", formatBot, formatUser, formatChat);
        }

    }

    @Override
    protected Boolean hasTarget(RobotGroupManagement rgm) {
        return rgm.getGroupId().equals(this.chatId);
    }

    @Override
    protected Boolean hasTarget(InvitationThreshold it) {
        return it.getChatId().equals(this.chatId) && it.getType().equals("group")
                && it.getInvitedId().equals(this.invitedId);
    }

    private Boolean hasTarget(RecordGroupUsers rgu) {
        return rgu.getUserId().equals(this.invitedId)
                && rgu.getGroupId().equals(this.chatId);
    }

    private Boolean hasTarget(InvitationBonusUser ib) {
        return ib.getUserId().equals(this.inviteId) && ib.getChatId().equals(this.chatId);
    }

    private RobotGroupManagement getRobotGroupManagement(RobotGroupManagement robotGroupManagement) {
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
        invitationThreshold.setType("group");
        invitationThreshold.setInviteStatus(true);
        invitationThreshold.setInvitedStatus(true);
        return invitationThreshold;
    }

    @Override
    protected RecordGroupUsers getRecordGroupUsers(RecordGroupUsers recordGroupUsers) {
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

    @Override
    protected InvitationBonusUser getInvitationBonusUser(InvitationBonusUser invitationBonusUser) {
        invitationBonusUser.setBotId(this.botId);
        invitationBonusUser.setUserId(this.inviteId);
        invitationBonusUser.setFirstname(this.inviteFirstname);
        invitationBonusUser.setUsrname(this.inviteUsername);
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

    @Override
    protected Boolean hasTarget(RobotChannelManagement rcm) {
        return null;
    }

    @Override
    protected Boolean hasTarget(RecordChannelUsers rcu) {
        return null;
    }

    @Override
    protected RobotChannelManagement getRobotChannelManagement(RobotChannelManagement robotChannelManagement) {
        return null;
    }

    @Override
    protected RecordChannelUsers getRecordChannelUsers(RecordChannelUsers recordChannelUsers) {
        return null;
    }

}
