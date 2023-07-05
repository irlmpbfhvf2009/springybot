package com.lwdevelop.botfactory.event;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.ChatMemberUpdated;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;

import com.lwdevelop.botfactory.Common;
import com.lwdevelop.entity.InvitationThreshold;
import com.lwdevelop.entity.RecordChannelUsers;
import com.lwdevelop.entity.RobotChannelManagement;
import com.lwdevelop.entity.SpringyBot;
import com.lwdevelop.service.impl.SpringyBotServiceImpl;
import com.lwdevelop.utils.RedisUtils;
import com.lwdevelop.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LeaveChannel {

    @Autowired
    private SpringyBotServiceImpl springyBotServiceImpl = SpringUtils.getApplicationContext()
            .getBean(SpringyBotServiceImpl.class);

    @Autowired
    private RedisUtils redisUtils = SpringUtils.getApplicationContext()
            .getBean(RedisUtils.class);

    private Long chatId;
    private String chatTitle;
    private Common common;
    private SpringyBot springyBot;
    private List<RobotChannelManagement> robotChannelManagement;
    private List<InvitationThreshold> invitationThreshold;
    private List<RecordChannelUsers> recordChannelUsers;
    private Long userId;
    private User user;
    private Boolean isBot;

    public LeaveChannel(Common common) {
        this.common = common;
        this.springyBot = springyBotServiceImpl.findById(common.getSpringyBotId()).get();
        this.robotChannelManagement = springyBotServiceImpl
                .findRobotChannelManagementBySpringyBotId(common.getSpringyBotId());
        this.invitationThreshold = springyBotServiceImpl
                .findInvitationThresholdBySpringyBotId(common.getSpringyBotId());
        this.recordChannelUsers = springyBotServiceImpl.findRecordChannelUsersBySpringyBotId(common.getSpringyBotId());

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
            this.chatId = chatMemberUpdated.getChat().getId();
            this.chatTitle = chatMemberUpdated.getChat().getTitle();
            this.userId = this.user.getId();
        }

    }

    public void handler() {
        if (this.isBot != null) {
            String formatChat = this.chatTitle + "[" + this.chatId + "]";
            String formatBot = common.formatBot();
            String formatUser = common.formatUser(this.user);

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

    private Boolean hasTarget(RobotChannelManagement rcm) {
        return rcm.getChannelId().equals(this.chatId);
    }

    private Boolean hasTarget(RecordChannelUsers rcu) {
        return rcu.getUserId().equals(this.userId)
                && rcu.getChannelId().equals(this.chatId);
    }

    private Boolean hasTarget_invite(InvitationThreshold it) {
        return it.getInviteId().equals(this.userId);
    }

    private Boolean hasTarget_invited(InvitationThreshold it) {
        return it.getInvitedId().equals(this.userId);
    }

}
