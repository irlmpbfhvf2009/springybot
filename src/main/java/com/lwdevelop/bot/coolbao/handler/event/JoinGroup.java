package com.lwdevelop.bot.coolbao.handler.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import com.lwdevelop.bot.coolbao.utils.Common;
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

    public void isUserJoinGroup(Common common) {
        this.common = common;

    }

    public void isBotJoinGroup(Common common) {

        SpringyBot springyBot = springyBotServiceImpl.findById(common.getSpringyBotId()).get();
        this.common = common;
        this.message = common.getUpdate().getMessage();
        this.botId = common.getBotId();
        this.inviteId = message.getFrom().getId();
        this.inviteFirstname = message.getFrom().getFirstName();
        this.inviteUsername = message.getFrom().getUserName();
        this.inviteLastname = message.getFrom().getLastName();
        this.groupId = message.getChat().getId();
        this.groupTitle = message.getChat().getTitle();

        for (User member : this.message.getNewChatMembers()) {
            // bot join group
            if (isBot(member)) {
                springyBot.getRobotGroupManagement().stream()
                        .filter(rgm -> hasTarget(rgm))
                        .findFirst()
                        .ifPresentOrElse(rgm -> {
                            rgm.setStatus(true);
                        }, () -> {
                            springyBot.getRobotGroupManagement().add(this.getRobotGroupManagement());
                        });
                springyBotServiceImpl.save(springyBot);
                log.info("[{}] bot join {} group",common.getUsername(),this.groupTitle);
                // user invite other user
            } else if (isUserInviteEvent(member)) {
            }
        }
    }

    private Boolean isBot(User member) {
        return this.common.getUsername().equals(member.getUserName()) && member.getIsBot();
    }

    private Boolean isUserInviteEvent(User member) {
        return !this.common.getUsername().equals(member.getUserName()) && !member.getIsBot();
    }

    private Boolean hasTarget(RobotGroupManagement rgm) {
        return rgm.getGroupId().equals(this.groupId) && rgm.getBotId().equals(this.botId);
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

}
