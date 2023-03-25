package com.lwdevelop.bot.handler;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import com.lwdevelop.entity.RobotGroupManagement;
import com.lwdevelop.entity.SpringyBot;
import com.lwdevelop.service.impl.SpringyBotServiceImpl;

public class JoinGroupEvent {

    Long inviteId;
    String inviteFirstname;
    String inviteUsername;
    Long groupId;
    String groupTitle;
    Long botId;
    String link;

    public void handler(Message message, String username, Long botId,SpringyBot springyBot,String link, SpringyBotServiceImpl springyBotServiceImpl) {

        // 邀請人
        this.inviteId = message.getFrom().getId();
        this.inviteFirstname = message.getFrom().getFirstName();
        this.inviteUsername = message.getFrom().getUserName();
        this.groupId = message.getChat().getId();
        this.groupTitle = message.getChat().getTitle();
        this.botId = botId;
        this.link = link;

        for (User member : message.getNewChatMembers()) {
            // bot join group
            if (isBotJoinGroup(member,username)) {
                springyBot.getRobotGroupManagement().stream()
                        .filter(rgm -> hasTarget(rgm))
                        .findFirst()
                        .ifPresentOrElse(null, () -> {
                            RobotGroupManagement robotGroupManagement = getRobotGroupManagement();
                            springyBot.getRobotGroupManagement().add(robotGroupManagement);
                            springyBotServiceImpl.save(springyBot);
                        });
            // user invite other user
            }else if(isUserInviteEvent(member,username)){

            }
        }
    }
    private Boolean isBotJoinGroup(User member,String username){
        return username.equals(member.getUserName()) && member.getIsBot();
    }

    private Boolean isUserInviteEvent(User member,String username){
        return !username.equals(member.getUserName()) && !member.getIsBot();
    }

    private Boolean hasTarget(RobotGroupManagement rgm){
        return rgm.getGroupId().equals(this.groupId) && rgm.getBotId().equals(this.botId);
    }

    private RobotGroupManagement getRobotGroupManagement(){
        RobotGroupManagement robotGroupManagement = new RobotGroupManagement();
        robotGroupManagement.setBotId(this.botId);
        robotGroupManagement.setInviteId(this.inviteId);
        robotGroupManagement.setInviteFirstname(this.inviteFirstname);
        robotGroupManagement.setInviteUsername(this.inviteUsername);
        robotGroupManagement.setGroupId(this.groupId);
        robotGroupManagement.setGroupTitle(this.groupTitle);
        robotGroupManagement.setLink(this.link);
        return robotGroupManagement;
    }

}
