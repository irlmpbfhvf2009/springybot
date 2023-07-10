package com.lwdevelop.botfactory.event;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.ChatMemberUpdated;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;

import com.lwdevelop.botfactory.Common;
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
public class LeaveGroup {

    @Autowired
    private SpringyBotServiceImpl springyBotServiceImpl = SpringUtils.getApplicationContext()
            .getBean(SpringyBotServiceImpl.class);

    @Autowired
    private RedisUtils redisUtils = SpringUtils.getApplicationContext()
            .getBean(RedisUtils.class);

    private SpringyBot springyBot;
    private List<RobotGroupManagement> robotGroupManagement;
    private List<InvitationThreshold> invitationThreshold;
    private List<RecordGroupUsers> recordGroupUsers;
    private List<InvitationBonusUser> invitationBonusUsers;
    private Common common;
    private Long userId;
    private Long chatId;
    private String chatTitle;
    private User user;
    private Boolean isBot;

    public LeaveGroup(Common common) {
        this.common = common;
        this.springyBot = springyBotServiceImpl.findById(common.getSpringyBotId()).get();
        this.robotGroupManagement = springyBotServiceImpl
                .findRobotGroupManagementBySpringyBotId(common.getSpringyBotId());
        this.invitationThreshold = springyBotServiceImpl
                .findInvitationThresholdBySpringyBotId(common.getSpringyBotId());
        this.recordGroupUsers = springyBotServiceImpl.findRecordGroupUsersBySpringyBotId(common.getSpringyBotId());
        this.invitationBonusUsers = springyBotServiceImpl
                .findInvitationBonusUserBySpringyBotId(common.getSpringyBotId());

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
            this.chatId = chatMemberUpdated.getChat().getId();
            this.chatTitle = chatMemberUpdated.getChat().getTitle();
            this.user = chatMember.getUser();
            this.userId = chatMember.getUser().getId();
        }
    }

    public void handler() {
        if (this.isBot != null) {
            String formatChat = this.chatTitle + "[" + this.chatId + "]";
            String formatBot = common.formatBot();
            String formatUser = common.formatUser(this.user);

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

    private Boolean hasTarget(RecordGroupUsers recordGroupUsers) {
        return recordGroupUsers.getUserId().equals(this.userId);
    }

    private Boolean hasTarget_invite(InvitationThreshold it) {
        return it.getInviteId().equals(this.userId);
    }

    private Boolean hasTarget_invited(InvitationThreshold it) {
        return it.getInvitedId().equals(this.userId);
    }

}
