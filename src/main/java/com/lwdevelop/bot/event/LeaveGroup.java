package com.lwdevelop.bot.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.ChatMemberUpdated;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;

import com.lwdevelop.bot.Common;
import com.lwdevelop.entity.InvitationThreshold;
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

    public LeaveGroup(Common common) {
        this.common = common;
        this.botId = common.getBotId();
        ChatMemberUpdated chatMemberUpdated = common.getUpdate().getMyChatMember();
        ChatMember chatMember = common.getUpdate().getMyChatMember().getNewChatMember();
        this.chatId = chatMemberUpdated.getChat().getId();
        this.springyBot = springyBotServiceImpl.findById(common.getSpringyBotId()).get();
        this.chatTitle = chatMemberUpdated.getChat().getTitle();
        this.userId = chatMember.getUser().getId();
        this.user = chatMember.getUser();
    }

    public void handler(Boolean isBot) {
        String formatChat = "[" + this.chatId + "] " + this.chatTitle;
        String formatBot = common.formatBot();
        String formatUser  = common.formatUser(this.user);


        if (isBot) {
            this.springyBot.getRobotGroupManagement().stream()
                    .filter(rgm -> hasTarget(rgm))
                    .findFirst()
                    .ifPresent(g -> {
                        g.setStatus(false);
                    });
            this.springyBotServiceImpl.save(springyBot);
            log.info("{} -> {} user leave {} group", formatBot, formatUser, formatChat);


        } else {
            this.springyBot.getInvitationThreshold().stream()
                    .filter(it -> invite_hasTarget(it) || invited_hasTarget(it))
                    .findFirst()
                    .ifPresent(g -> {
                        if (invite_hasTarget(g)) {
                            g.setInviteStatus(false);
                        }
                        if (invited_hasTarget(g)) {
                            g.setInvitedStatus(false);
                        }
                    });

            this.springyBotServiceImpl.save(springyBot);
            log.info("{} -> {} user leave {} group", formatBot, formatUser formatChat);
        }

    }

    private Boolean hasTarget(RobotGroupManagement rgm) {
        return rgm.getBotId().equals(this.botId) && rgm.getGroupId().equals(this.chatId);
    }

    private Boolean invite_hasTarget(InvitationThreshold it) {
        return it.getInviteId().equals(this.userId);
    }

    private Boolean invited_hasTarget(InvitationThreshold it) {
        return it.getInvitedId().equals(this.userId);
    }
}
