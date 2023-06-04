package com.lwdevelop.bot.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.ChatMemberUpdated;
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
    private Long chatId;
    private String chatTitle;
    private Long invitedId;
    private String invitedFirstname;
    private String invitedUsername;
    private String invitedLastname;
    private SpringyBot springyBot;
    private User user;
    private Boolean isBot;

    public JoinGroup(Common common) {
        this.common = common;
        this.botId = common.getBotId();
        ChatMemberUpdated chatMemberUpdated = common.getUpdate().getMyChatMember();
        ChatMember chatMember = chatMemberUpdated.getNewChatMember();
        this.inviteId = chatMemberUpdated.getFrom().getId();
        this.inviteFirstname = chatMemberUpdated.getFrom().getFirstName();
        this.inviteUsername = chatMemberUpdated.getFrom().getUserName();
        this.inviteLastname = chatMemberUpdated.getFrom().getLastName();
        this.chatId = chatMemberUpdated.getChat().getId();
        this.chatTitle = chatMemberUpdated.getChat().getTitle();
        this.springyBot = springyBotServiceImpl.findById(common.getSpringyBotId()).get();
        this.invitedId = chatMember.getUser().getId();
        this.invitedFirstname = chatMember.getUser().getFirstName();
        this.invitedUsername = chatMember.getUser().getUserName();
        this.invitedLastname = chatMember.getUser().getLastName();
        this.user = chatMember.getUser();
        this.isBot = chatMember.getUser().getIsBot() && chatMember.getUser().getId().equals(common.getBotId());
    }

    public void handler() {
        String formatChat = this.chatTitle +  "[" + this.chatId + "]";
        String formatBot = common.formatBot();
        String formatUser  = common.formatUser(this.user);

        if (this.isBot) {
            // bot join group
            this.springyBot.getRobotGroupManagement().stream()
                    .filter(rgm -> hasTarget(rgm))
                    .findFirst()
                    .ifPresentOrElse(rgm -> {
                        rgm.setStatus(true);
                    }, () -> {
                        springyBot.getRobotGroupManagement().add(this.getRobotGroupManagement());
                    });
            springyBotServiceImpl.save(springyBot);
        } else {
                this.springyBot.getInvitationThreshold().stream()
                        .filter(it -> this.hasTarget(it))
                        .findFirst()
                        .ifPresentOrElse(it -> {
                            it.setInviteStatus(true);
                            it.setInvitedStatus(true);
                        }, () -> {
                            this.springyBot.getInvitationThreshold().add(this.getInvitationThreshold());
                        });

                this.springyBot.getRecordGroupUsers().stream()
                        .filter(rgu -> rgu.getUserId().equals(this.invitedId))
                        .findAny()
                        .ifPresentOrElse(rgu -> {
                            rgu.setStatus(true);
                        }, () -> {
                            this.springyBot.getRecordGroupUsers().add(this.getRecordGroupUsers());
                        });

                springyBotServiceImpl.save(springyBot);
            
        }
        log.info("{} -> {} join {} group", formatBot, formatUser ,formatChat);

    }

    private Boolean hasTarget(RobotGroupManagement rgm) {
        return rgm.getGroupId().equals(this.chatId) && rgm.getBotId().equals(this.botId);
    }

    private Boolean hasTarget(InvitationThreshold it) {
        return it.getChatId().equals(this.chatId) && it.getType().equals("group");
    }

    private RobotGroupManagement getRobotGroupManagement() {
        RobotGroupManagement robotGroupManagement = new RobotGroupManagement();
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

    private InvitationThreshold getInvitationThreshold() {
        InvitationThreshold invitationThreshold = new InvitationThreshold();
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

    private RecordGroupUsers getRecordGroupUsers() {
        RecordGroupUsers recordGroupUsers = new RecordGroupUsers();
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
