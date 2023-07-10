package com.lwdevelop.botfactory.event;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.ChatMemberUpdated;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import com.fasterxml.jackson.core.type.TypeReference;
import com.lwdevelop.botfactory.Common;
import com.lwdevelop.entity.Config;
import com.lwdevelop.entity.InvitationBonusUser;
import com.lwdevelop.entity.InvitationThreshold;
import com.lwdevelop.entity.RecordGroupUsers;
import com.lwdevelop.entity.RobotGroupManagement;
import com.lwdevelop.entity.SpringyBot;
import com.lwdevelop.service.impl.SpringyBotServiceImpl;
import com.lwdevelop.utils.RedisUtils;
import com.lwdevelop.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JoinGroup {

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
    private List<RobotGroupManagement> robotGroupManagement;
    private List<InvitationThreshold> invitationThreshold;
    private List<RecordGroupUsers> recordGroupUsers;
    private List<InvitationBonusUser> invitationBonusUsers;
    private User user;
    private User from;
    private Boolean isBot;
    private Boolean invitationBonusSet;
    private int inviteMembers;
    private BigDecimal inviteEarnedOutstand;
    private BigDecimal minimumPayout;
    private String contactPerson;

    public JoinGroup(Common common) {
        this.common = common;
        this.botId = common.getBotId();
        Long springyBotId = common.getSpringyBotId();
        SpringyBot springyBot = springyBotServiceImpl.findById(springyBotId).get();
        Config config = springyBot.getConfig();
        this.springyBot = springyBot;
        this.robotGroupManagement = springyBotServiceImpl
                .findRobotGroupManagementBySpringyBotId(springyBotId);
        this.invitationThreshold = springyBotServiceImpl
                .findInvitationThresholdBySpringyBotId(springyBotId);
        this.recordGroupUsers = springyBotServiceImpl.findRecordGroupUsersBySpringyBotId(springyBotId);
        this.invitationBonusUsers = springyBotServiceImpl.findInvitationBonusUserBySpringyBotId(springyBotId);
        this.invitationBonusSet = config.getInvitationBonusSet();
        this.inviteMembers = config.getInviteMembers();
        this.inviteEarnedOutstand = config.getInviteEarnedOutstand();
        this.minimumPayout = config.getMinimumPayout();
        this.contactPerson = config.getContactPerson();

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
        this.loadConfig();
    }

    public void handler() {
        if (this.isBot != null) {
            String formatChat = this.chatTitle + "[" + this.chatId + "]";
            String formatBot = common.formatBot();
            String formatUser = common.formatUser(this.user);

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
                    this.redisUtils.set("InvitationThreshold_" + this.common.getSpringyBotId(), this.invitationThreshold);
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
                                        Boolean isInvitationsDivisibleByMembers = (invitedIds.size()
                                                % this.inviteMembers) == 0;

                                        if (isInvitationsDivisibleByMembers) {
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
                                            this.common.deleteMessageTask(deleteMessage, 1000);

                                        }
                                    }
                                }, () -> {
                                    InvitationBonusUser invitationBonusUser = new InvitationBonusUser();
                                    InvitationBonusUser ib = this.getInvitationBonusUser(invitationBonusUser);
                                    this.invitationBonusUsers.add(ib);
                                    if (1 % this.inviteMembers == 0) {
                                        SendMessage sendMessage = new SendMessage();
                                        sendMessage.setChatId(String.valueOf(this.chatId));
                                        sendMessage.setText(
                                                "您邀请1位成员，赚取" + this.inviteEarnedOutstand + "元未结算，满" + this.minimumPayout
                                                        + "元请联系 " + this.contactPerson + " 结算。");
                                        Integer msgId = this.common.executeAsync(sendMessage);
                                        DeleteMessage deleteMessage = new DeleteMessage(this.chatId.toString(),
                                                msgId);
                                        this.common.deleteMessageTask(deleteMessage, 1000);
                                    }
                                });
                        this.springyBot.setInvitationBonusUser(this.invitationBonusUsers);
                    }
                }
            }
            this.springyBotServiceImpl.save(this.springyBot);

            log.info("{} -> {} join {} group", formatBot, formatUser, formatChat);
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

    private Boolean hasTarget(RobotGroupManagement rgm) {
        return rgm.getGroupId().equals(this.chatId);
    }

    private Boolean hasTarget(InvitationThreshold it) {
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
        robotGroupManagement.setLink(this.common.getInviteLink());
        robotGroupManagement.setStatus(true);
        return robotGroupManagement;
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
        invitationThreshold.setType("group");
        invitationThreshold.setInviteStatus(true);
        invitationThreshold.setInvitedStatus(true);
        return invitationThreshold;
    }

    private RecordGroupUsers getRecordGroupUsers(RecordGroupUsers recordGroupUsers) {
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

    private InvitationBonusUser getInvitationBonusUser(InvitationBonusUser invitationBonusUser) {
        invitationBonusUser.setBotId(this.botId);
        invitationBonusUser.setUserId(this.inviteId);
        invitationBonusUser.setFirstname(this.inviteFirstname);
        invitationBonusUser.setUsrname(this.inviteUsername);
        invitationBonusUser.setLastname(this.inviteLastname);
        invitationBonusUser.setChatId(this.chatId);
        invitationBonusUser.setChatTitle(this.chatTitle);
        if (1 % this.inviteMembers == 0) {
            invitationBonusUser.setOutstandingAmount(this.inviteEarnedOutstand);
        }
        invitationBonusUser.setSettlementAmount(BigDecimal.valueOf(0));
        List<String> invitedIds = new ArrayList<>();
        invitedIds.add(this.invitedId.toString());
        invitationBonusUser.setPendingInvitations(invitedIds);
        invitationBonusUser.setAccumulatedInvitations(new ArrayList<>());
        return invitationBonusUser;
    }

    private void loadConfig() {
        Config config = redisUtils.get("Config_" + this.common.getSpringyBotId(),
                new TypeReference<Config>() {
                });
        this.invitationBonusSet = config.getInvitationBonusSet();
        this.inviteMembers = config.getInviteMembers();
        this.inviteEarnedOutstand = config.getInviteEarnedOutstand();
        this.minimumPayout = config.getMinimumPayout();
        this.contactPerson = config.getContactPerson();

    }

}
