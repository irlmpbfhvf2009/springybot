package com.lwdevelop.bot.chatMembershipHandlers;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.ChatMemberUpdated;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;

import com.lwdevelop.bot.bots.utils.Common;
import com.lwdevelop.entity.InvitationBonusUser;
import com.lwdevelop.entity.InvitationThreshold;
import com.lwdevelop.entity.RecordChannelUsers;
import com.lwdevelop.entity.RecordGroupUsers;
import com.lwdevelop.entity.RobotChannelManagement;
import com.lwdevelop.entity.RobotGroupManagement;
import com.lwdevelop.entity.SpringyBot;
import com.lwdevelop.service.impl.SpringyBotServiceImpl;
import com.lwdevelop.utils.RedisUtils;
import com.lwdevelop.utils.SpringUtils;

public abstract class BaseHandler {

    @Autowired
    protected SpringyBotServiceImpl springyBotServiceImpl = SpringUtils.getApplicationContext()
            .getBean(SpringyBotServiceImpl.class);

    @Autowired
    protected RedisUtils redisUtils = SpringUtils.getApplicationContext()
            .getBean(RedisUtils.class);

    protected Common common;
    protected Long botId;
    protected Long springyBotId;
    protected Long inviteId;
    protected String inviteFirstname;
    protected String inviteUsername;
    protected String inviteLastname;
    protected Long invitedId;
    protected String invitedFirstname;
    protected String invitedUsername;
    protected String invitedLastname;
    protected Long chatId;
    protected String chatTitle;
    protected SpringyBot springyBot;
    protected User user;
    protected User from;
    protected Boolean isBot;
    protected List<RobotChannelManagement> robotChannelManagement;
    protected List<InvitationThreshold> invitationThreshold;
    protected List<RecordChannelUsers> recordChannelUsers;
    protected Update update;
    protected ChatMemberUpdated chatMemberUpdated = null;
    protected ChatMember chatMember = null;
    protected String formatChat;
    protected String formatBot;
    protected String formatUser;

    protected List<RobotGroupManagement> robotGroupManagement;
    protected List<RecordGroupUsers> recordGroupUsers;
    protected List<InvitationBonusUser> invitationBonusUsers;
    protected Boolean invitationBonusSet;
    protected int inviteMembers;
    protected BigDecimal inviteEarnedOutstand;
    protected BigDecimal minimumPayout;
    protected String contactPerson;
    protected Long userId;

    public BaseHandler(Common common) {
        this.common = common;
        this.botId = common.getBotId();
        this.springyBot = springyBotServiceImpl.findById(common.getSpringyBotId()).get();
        this.update = common.getUpdate();
        this.chatMemberUpdated = null;
        this.chatMember = null;
        this.invitationThreshold = springyBotServiceImpl
                .findInvitationThresholdBySpringyBotId(common.getSpringyBotId());
        this.springyBotId = common.getSpringyBotId();
        this.update = common.getUpdate();
        if (update.hasMyChatMember()) {
            chatMemberUpdated = update.getMyChatMember();
            chatMember = chatMemberUpdated.getNewChatMember();
        } else if (update.hasChatMember()) {
            chatMemberUpdated = update.getChatMember();
            chatMember = chatMemberUpdated.getNewChatMember();
        }

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
            this.userId = this.user.getId();
        }
        this.isBot = this.isBot(chatMember);
        this.formatChat = this.chatTitle + "[" + this.chatId + "]";
        this.formatBot = common.formatBot();
        this.formatUser = common.formatUser(this.user);
    }

    public abstract void handler();

    protected abstract Boolean hasTarget(RobotGroupManagement rgm);

    protected abstract Boolean hasTarget(RobotChannelManagement rcm);

    protected abstract Boolean hasTarget(RecordChannelUsers rcu);

    protected abstract Boolean hasTarget(InvitationThreshold it);

    protected abstract InvitationThreshold getInvitationThreshold(InvitationThreshold invitationThreshold);

    protected abstract RobotChannelManagement getRobotChannelManagement(RobotChannelManagement robotChannelManagement);

    protected abstract RecordChannelUsers getRecordChannelUsers(RecordChannelUsers recordChannelUsers);

    protected abstract RecordGroupUsers getRecordGroupUsers(RecordGroupUsers recordGroupUsers);

    protected abstract InvitationBonusUser getInvitationBonusUser(InvitationBonusUser invitationBonusUser);

    private Boolean isBot(ChatMember chatMember) {
        if (chatMember == null) {
            return null;
        }
        Boolean isBot = chatMember.getUser().getIsBot();
        return isBot;
    }

}
