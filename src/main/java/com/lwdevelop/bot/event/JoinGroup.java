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
public class JoinGroup {

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

    public JoinGroup(Common common) {
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
            this.from = chatMemberUpdated.getFrom();
            this.chatId = chatMemberUpdated.getChat().getId();
            this.chatTitle = chatMemberUpdated.getChat().getTitle().replaceAll("[^\\p{script=Han}a-zA-Z0-9]", "");;
            this.inviteId = this.from.getId();
            this.inviteFirstname = this.from.getFirstName().replaceAll("[^\\p{script=Han}a-zA-Z0-9]", "");;
            this.inviteUsername = this.from.getUserName().replaceAll("[^\\p{script=Han}a-zA-Z0-9]", "");;
            this.inviteLastname = this.from.getLastName().replaceAll("[^\\p{script=Han}a-zA-Z0-9]", "");;
            this.invitedId = this.user.getId();
            this.invitedFirstname = this.user.getFirstName().replaceAll("[^\\p{script=Han}a-zA-Z0-9]", "");;
            this.invitedUsername = this.user.getUserName().replaceAll("[^\\p{script=Han}a-zA-Z0-9]", "");;
            this.invitedLastname = this.user.getLastName().replaceAll("[^\\p{script=Han}a-zA-Z0-9]", "");;
        }
    }

    public void handler() {
        if (this.isBot != null) {
            String formatChat = this.chatTitle + "[" + this.chatId + "]";
            String formatBot = common.formatBot();
            String formatUser = common.formatUser(this.user);

            if (this.isBot) {
                // bot join group
                this.springyBot.getRobotGroupManagement().stream()
                        .filter(rgm -> hasTarget(rgm))
                        .findAny()
                        .ifPresentOrElse(rgm -> {
                            rgm = this.getRobotGroupManagement(rgm);
                        }, () -> {
                            RobotGroupManagement robotGroupManagement = new RobotGroupManagement();
                            RobotGroupManagement rgm = this.getRobotGroupManagement(robotGroupManagement);
                            springyBot.getRobotGroupManagement().add(rgm);
                        });
            } else {
                // user join group
                this.springyBot.getInvitationThreshold().stream()
                        .filter(it -> this.hasTarget(it))
                        .findAny()
                        .ifPresentOrElse(it -> {
                            it = this.getInvitationThreshold(it);
                        }, () -> {
                            InvitationThreshold invitationThreshold = new InvitationThreshold();
                            InvitationThreshold it = this.getInvitationThreshold(invitationThreshold);
                            this.springyBot.getInvitationThreshold().add(it);
                        });

                this.springyBot.getRecordGroupUsers().stream()
                        .filter(rgu -> rgu.getUserId().equals(this.invitedId))
                        .findAny()
                        .ifPresentOrElse(rgu -> {
                            rgu = this.getRecordGroupUsers(rgu);
                        }, () -> {
                            RecordGroupUsers recordGroupUsers = new RecordGroupUsers();
                            RecordGroupUsers rgu = this.getRecordGroupUsers(recordGroupUsers);
                            this.springyBot.getRecordGroupUsers().add(rgu);                        });
            }
            springyBotServiceImpl.save(springyBot);
            log.info("{} -> {} join {} group", formatBot, formatUser, formatChat);
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
        return rgm.getGroupId().equals(this.chatId) && rgm.getBotId().equals(this.botId);
    }

    private Boolean hasTarget(InvitationThreshold it) {
        return it.getChatId().equals(this.chatId) && it.getType().equals("group")
                && it.getInvitedId().equals(this.invitedId);

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
        recordGroupUsers.setGroupId(this.chatId);
        recordGroupUsers.setGroupTitle(this.chatTitle);
        recordGroupUsers.setFirstname(this.invitedFirstname);
        recordGroupUsers.setLastname(this.invitedLastname);
        recordGroupUsers.setStatus(true);
        recordGroupUsers.setUserId(this.invitedId);
        recordGroupUsers.setUsername(this.invitedUsername);
        return recordGroupUsers;
    }

}
