package com.lwdevelop.bot.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.ChatMemberUpdated;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;

import com.lwdevelop.bot.Common;
import com.lwdevelop.entity.InvitationThreshold;
import com.lwdevelop.entity.RecordChannelUsers;
import com.lwdevelop.entity.RobotChannelManagement;
import com.lwdevelop.entity.SpringyBot;
import com.lwdevelop.service.impl.SpringyBotServiceImpl;
import com.lwdevelop.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LeaveChannel {

    @Autowired
    private SpringyBotServiceImpl springyBotServiceImpl = SpringUtils.getApplicationContext()
            .getBean(SpringyBotServiceImpl.class);

    private Long chatId;
    private String chatTitle;
    private Long botId;
    private Common common;
    private SpringyBot springyBot;
    private Long userId;
    private User user;
    private Boolean isBot;

    public LeaveChannel(Common common) {
        this.common = common;
        this.botId = common.getBotId();
        this.springyBot = springyBotServiceImpl.findById(common.getSpringyBotId()).get();

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
                this.springyBot.getRobotChannelManagement().stream()
                        .filter(rgm -> hasTarget(rgm))
                        .findAny()
                        .ifPresent(c -> {
                            c.setStatus(false);
                        });
            } else {
                this.springyBot.getRecordChannelUsers().stream()
                        .filter(rcu -> hasTarget(rcu))
                        .findAny()
                        .ifPresent(rcu -> {
                            rcu.setStatus(false);
                        });

                this.springyBot.getInvitationThreshold().stream()
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
            springyBotServiceImpl.save(this.springyBot);
            log.info("{} -> {} leave {} channel", formatBot, formatUser, formatChat);
        }
    }

    private Boolean isBot(ChatMember chatMember) {
        if (chatMember == null) {
            return null;
        }
        Boolean isBot = chatMember.getUser().getIsBot() && chatMember.getUser().getId().equals(common.getBotId());
        Boolean existBot = springyBotServiceImpl.findAll().stream()
                .anyMatch(springyBot -> springyBot.getBotId().equals(this.botId));
        return isBot && existBot;
    }

    private Boolean hasTarget(RobotChannelManagement rcm) {
        return rcm.getBotId().equals(this.botId) && rcm.getChannelId().equals(this.chatId);
    }

    private Boolean hasTarget(RecordChannelUsers recordChannelUsers) {
        return recordChannelUsers.getUserId() == this.userId;
    }

    private Boolean hasTarget_invite(InvitationThreshold it) {
        return it.getInviteId().equals(this.userId);
    }

    private Boolean hasTarget_invited(InvitationThreshold it) {
        return it.getInvitedId().equals(this.userId);
    }
}
