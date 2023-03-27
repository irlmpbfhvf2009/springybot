package com.lwdevelop.bot.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import com.lwdevelop.bot.utils.Common;
import com.lwdevelop.entity.RobotGroupManagement;
import com.lwdevelop.entity.SpringyBot;
import com.lwdevelop.service.impl.SpringyBotServiceImpl;
import com.lwdevelop.utils.SpringUtils;

public class JoinGroupEvent {

    @Autowired
    private SpringyBotServiceImpl springyBotServiceImpl = SpringUtils.getApplicationContext()
            .getBean(SpringyBotServiceImpl.class);

    private Long inviteId;
    private Long groupId;
    private Long botId;
    private String inviteFirstname;
    private String inviteUsername;
    private String groupTitle;
    private Common common;

    public void isUserJoinGroup(Common common) {
        Message message = common.getMessage();
        this.common = common;

        // invite user
        this.inviteId = message.getFrom().getId();
        this.inviteFirstname = message.getFrom().getFirstName();
        this.inviteUsername = message.getFrom().getUserName();

        // group info
        this.groupId = message.getChat().getId();
        this.groupTitle = message.getChat().getTitle();

        this.botId = common.getBotId();

    }

    public void isBotJoinGroup(Common common) {
        // init
        Message message = common.getMessage();
        SpringyBot springyBot = springyBotServiceImpl.findById(common.getSpringyBotId()).get();
        this.common = common;

        // invite user
        this.inviteId = message.getFrom().getId();
        this.inviteFirstname = message.getFrom().getFirstName();
        this.inviteUsername = message.getFrom().getUserName();

        // group info
        this.groupId = message.getChat().getId();
        this.groupTitle = message.getChat().getTitle();

        this.botId = common.getBotId();

        for (User member : message.getNewChatMembers()) {
            // bot join group
            if (isBot(member)) {
                springyBot.getRobotGroupManagement().stream()
                        .filter(rgm -> hasTarget(rgm))
                        .findFirst()
                        .ifPresentOrElse(null, () -> {
                            springyBot.getRobotGroupManagement().add(getRobotGroupManagement());
                            springyBotServiceImpl.save(springyBot);
                        });
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
        robotGroupManagement.setGroupId(this.groupId);
        robotGroupManagement.setGroupTitle(this.groupTitle);
        robotGroupManagement.setLink(this.common.getInviteLink());
        return robotGroupManagement;
    }

}
