package com.lwdevelop.bot.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import com.lwdevelop.bot.Common;
import com.lwdevelop.entity.InvitationThreshold;
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
    private Message message;
    private Long botId;
    private Long inviteId;
    private String inviteFirstname;
    private String inviteUsername;
    private String inviteLastname;
    private Long groupId;
    private String groupTitle;
    private Long invitedId;
    private String invitedFirstname;
    private String invitedUsername;
    private String invitedLastname;
    private SpringyBot springyBot;

    public JoinGroup(Common common) {
        this.common = common;
        this.message = common.getUpdate().getMessage();
        this.botId = common.getBotId();
        this.inviteId = message.getFrom().getId();
        this.inviteFirstname = message.getFrom().getFirstName();
        this.inviteUsername = message.getFrom().getUserName();
        this.inviteLastname = message.getFrom().getLastName();
        this.groupId = message.getChat().getId();
        this.groupTitle = message.getChat().getTitle();
        this.springyBot = springyBotServiceImpl.findById(common.getSpringyBotId()).get();
    }

    public void isUserJoinGroup() {

        String invite_name = "[" + String.valueOf(this.inviteId) + "]" + this.inviteFirstname + this.inviteUsername
                + this.inviteLastname;

        for (User member : this.message.getNewChatMembers()) {
            this.invitedId = member.getId();
            this.invitedFirstname = member.getFirstName();
            this.invitedUsername = member.getUserName();
            this.invitedLastname = member.getLastName();
            String invited_name = generatedInvitedName();

            if (isUserInviteEvent(member)) {
                this.springyBot.getInvitationThreshold().stream()
                        .filter(it -> this.hasTarget(it))
                        .findFirst()
                        .ifPresentOrElse(it -> {
                            it.setInviteStatus(true);
                            it.setInvitedStatus(true);
                        }, () -> {
                            this.springyBot.getInvitationThreshold().add(this.getInvitationThreshold());
                        });
                log.info("{} invite {} join group {}", invite_name, invited_name, this.groupTitle);
                springyBotServiceImpl.save(springyBot);
            }
        }
    }

    public void isBotJoinGroup() {

        for (User member : this.message.getNewChatMembers()) {
            // bot join group
            if (isBotInviteEvent(member)) {
                this.springyBot.getRobotGroupManagement().stream()
                        .filter(rgm -> hasTarget(rgm))
                        .findFirst()
                        .ifPresentOrElse(rgm -> {
                            rgm.setStatus(true);
                        }, () -> {
                            springyBot.getRobotGroupManagement().add(this.getRobotGroupManagement());
                        });
                springyBotServiceImpl.save(springyBot);
                log.info("{} bot join {} group", member.getUserName(), this.groupTitle);
            }
        }
    }

    private Boolean isBotInviteEvent(User member) {
        return this.common.getUsername().equals(member.getUserName()) && member.getIsBot();
    }

    private Boolean isUserInviteEvent(User member) {
        return !member.getIsBot();
    }

    private Boolean hasTarget(RobotGroupManagement rgm) {
        return rgm.getGroupId().equals(this.groupId) && rgm.getBotId().equals(this.botId);
    }

    private Boolean hasTarget(InvitationThreshold it) {
        return it.getChatId().equals(this.groupId) && it.getType().equals("group");
    }

    private RobotGroupManagement getRobotGroupManagement() {
        RobotGroupManagement robotGroupManagement = new RobotGroupManagement();
        robotGroupManagement.setBotId(this.botId);
        robotGroupManagement.setInviteId(this.inviteId);
        robotGroupManagement.setInviteFirstname(this.inviteFirstname);
        robotGroupManagement.setInviteUsername(this.inviteUsername);
        robotGroupManagement.setInviteLastname(this.inviteLastname);
        robotGroupManagement.setGroupId(this.groupId);
        robotGroupManagement.setGroupTitle(this.groupTitle);
        robotGroupManagement.setLink(this.common.getInviteLink());
        robotGroupManagement.setStatus(true);
        return robotGroupManagement;
    }

    private InvitationThreshold getInvitationThreshold() {
        InvitationThreshold invitationThreshold = new InvitationThreshold();
        invitationThreshold.setBotId(this.botId);
        invitationThreshold.setChatId(this.groupId);
        invitationThreshold.setChatTitle(this.groupTitle);
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

    private String generatedInvitedName() {
        return "[" + String.valueOf(this.invitedId) + "]" + this.invitedFirstname
                + this.invitedUsername
                + this.invitedLastname;
    }

    // private String generatedInviteName() {
    // return "[" + String.valueOf(this.inviteId) + "]" + this.inviteFirstname +
    // this.inviteUsername
    // + this.inviteLastname;
    // }
}
