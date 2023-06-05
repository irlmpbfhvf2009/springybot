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
public class JoinChannel {

    @Autowired
    private SpringyBotServiceImpl springyBotServiceImpl = SpringUtils.getApplicationContext()
            .getBean(SpringyBotServiceImpl.class);

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
    private User user;
    private User from;
    private Boolean isBot;

    public JoinChannel(Common common) {
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
            System.out.println(chatMemberUpdated);
            System.out.println(chatMemberUpdated.getFrom());
            this.from = chatMemberUpdated.getFrom();
            this.chatId = chatMemberUpdated.getChat().getId();
            this.chatTitle = chatMemberUpdated.getChat().getTitle();
            this.inviteId = this.from.getId();
            this.inviteFirstname = this.from.getFirstName();
            this.inviteUsername = this.from.getUserName();
            this.inviteLastname = this.from.getLastName();
            this.invitedId = this.user.getId();
            this.invitedFirstname = this.user.getFirstName();
            this.invitedUsername = this.user.getUserName();
            this.invitedLastname = this.user.getLastName();
        }

    }

    public void handler() {
        if (this.isBot != null) {
            String formatChat = this.chatTitle + "[" + this.chatId + "]";
            String formatBot = common.formatBot();
            String formatUser = common.formatUser(this.user);

            if (this.isBot) {
                // bot join channel
                springyBot.getRobotChannelManagement().stream()
                        .filter(rcm -> hasTarget(rcm))
                        .findAny()
                        .ifPresentOrElse(rcm -> {
                            rcm = this.getRobotChannelManagement(rcm);
                        }, () -> {
                            RobotChannelManagement robotChannelManagement = new RobotChannelManagement();
                            RobotChannelManagement rcm = this.getRobotChannelManagement(robotChannelManagement);
                            springyBot.getRobotChannelManagement().add(rcm);
                        });
            } else {
                // user join group
                this.springyBot.getInvitationThreshold().stream()
                        .filter(it -> hasTarget(it))
                        .findAny()
                        .ifPresentOrElse(it -> {
                            it = this.getInvitationThreshold(it);
                        }, () -> {
                            InvitationThreshold invitationThreshold = new InvitationThreshold();
                            InvitationThreshold it = this.getInvitationThreshold(invitationThreshold);
                            this.springyBot.getInvitationThreshold().add(it);
                        });

                this.springyBot.getRecordChannelUsers().stream()
                        .filter(rcu -> rcu.getUserId().equals(this.invitedId))
                        .findAny()
                        .ifPresentOrElse(rcu -> {
                            rcu = this.getRecordChannelUsers(rcu);
                        }, () -> {
                            RecordChannelUsers recordChannelUsers = new RecordChannelUsers();
                            RecordChannelUsers rcu = this.getRecordChannelUsers(recordChannelUsers);
                            this.springyBot.getRecordChannelUsers().add(rcu);
                        });
            }
            springyBotServiceImpl.save(springyBot);
            log.info("{} -> {} join {} channel", formatBot, formatUser, formatChat);

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
        return rcm.getChannelId().equals(this.chatId) && rcm.getBotId().equals(this.botId);
    }

    private Boolean hasTarget(InvitationThreshold it) {
        return it.getChatId().equals(this.chatId) && it.getType().equals("channel")
                && it.getInvitedId().equals(this.invitedId);
    }

    private RobotChannelManagement getRobotChannelManagement(RobotChannelManagement robotChannelManagement) {
        robotChannelManagement.setBotId(this.botId);
        robotChannelManagement.setInviteId(this.invitedId);
        robotChannelManagement.setInviteFirstname(this.inviteFirstname);
        robotChannelManagement.setInviteUsername(this.inviteUsername);
        robotChannelManagement.setInviteLastname(this.inviteLastname);
        robotChannelManagement.setChannelId(this.chatId);
        robotChannelManagement.setChannelTitle(this.chatTitle);
        robotChannelManagement.setLink(this.common.getInviteLink());
        robotChannelManagement.setStatus(true);
        return robotChannelManagement;
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
        invitationThreshold.setInvitedStatus(true);
        invitationThreshold.setType("channel");
        invitationThreshold.setInvitedStatus(true);
        invitationThreshold.setInviteStatus(true);
        return invitationThreshold;
    }

    private RecordChannelUsers getRecordChannelUsers(RecordChannelUsers recordChannelUsers) {
        recordChannelUsers.setChannelId(this.chatId);
        recordChannelUsers.setChannelTitle(this.chatTitle);
        recordChannelUsers.setFirstname(this.invitedFirstname);
        recordChannelUsers.setLastname(this.invitedLastname);
        recordChannelUsers.setStatus(true);
        recordChannelUsers.setUserId(this.invitedId);
        recordChannelUsers.setUsername(this.invitedUsername);
        return recordChannelUsers;
    }

}
