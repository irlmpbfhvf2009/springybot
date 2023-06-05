package com.lwdevelop.bot.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.ChatMemberUpdated;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import com.lwdevelop.bot.Common;
import com.lwdevelop.entity.InvitationThreshold;
import com.lwdevelop.entity.RecordGroupUsers;
import com.lwdevelop.entity.RobotGroupManagement;
import com.lwdevelop.entity.SpringyBot;
import com.lwdevelop.service.impl.SpringyBotServiceImpl;
import com.lwdevelop.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LeaveGroup {

    @Autowired
    private SpringyBotServiceImpl springyBotServiceImpl = SpringUtils.getApplicationContext()
            .getBean(SpringyBotServiceImpl.class);

    private SpringyBot springyBot;
    private Common common;
    private Long botId;
    private Long userId;
    private Long chatId;
    private String chatTitle;
    private User user;
    private Boolean isBot;

    public LeaveGroup(Common common) {
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
                this.springyBot.getRobotGroupManagement().stream()
                        .filter(rgm -> hasTarget(rgm))
                        .findAny()
                        .ifPresent(g -> {
                            g.setStatus(false);
                        });
            } else {
                this.springyBot.getRecordGroupUsers().stream()
                        .filter(rgu -> hasTarget(rgu))
                        .findAny()
                        .ifPresent(rgu -> {
                            rgu.setStatus(false);
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
            this.springyBotServiceImpl.save(springyBot);
            log.info("{} -> {} leave {} group", formatBot, formatUser, formatChat);
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

    private Boolean hasTarget(RobotGroupManagement rgm) {
        return rgm.getBotId().equals(this.botId) && rgm.getGroupId().equals(this.chatId);
    }

    private Boolean hasTarget(RecordGroupUsers recordGroupUsers) {
        return recordGroupUsers.getUserId() == this.userId;
    }

    private Boolean hasTarget_invite(InvitationThreshold it) {
        return it.getInviteId().equals(this.userId);
    }

    private Boolean hasTarget_invited(InvitationThreshold it) {
        return it.getInvitedId().equals(this.userId);
    }
}
