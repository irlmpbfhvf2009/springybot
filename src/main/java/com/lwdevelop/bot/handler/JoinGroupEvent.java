package com.lwdevelop.bot.handler;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import com.lwdevelop.entity.RobotGroupManagement;
import com.lwdevelop.entity.SpringyBot;
import com.lwdevelop.service.impl.SpringyBotServiceImpl;

public class JoinGroupEvent {
    

    public void handler(Message message, String username, String token,Long botId, String link,SpringyBotServiceImpl springyBotServiceImpl) {

        // 邀請人
        Long inviteId = message.getFrom().getId();
        String inviteFirstname = message.getFrom().getFirstName();
        String inviteUsername = message.getFrom().getUserName();
        Long groupId = message.getChat().getId();
        String groupTitle = message.getChat().getTitle();

        // 被邀請對象
        for (User member : message.getNewChatMembers()) {
            if (username.equals(member.getUserName()) && member.getIsBot()) {
                SpringyBot springyBot = springyBotServiceImpl.findByToken(token);
                springyBot.getRobotGroupManagement().stream().filter(rgm -> rgm.getGroupId().equals(groupId))
                        .findFirst()
                        .ifPresentOrElse(null, () -> {
                            RobotGroupManagement robotGroupManagement = new RobotGroupManagement();
                            robotGroupManagement.setFromBotId(botId);
                            robotGroupManagement.setInviteId(inviteId);
                            robotGroupManagement.setInviteFirstname(inviteFirstname);
                            robotGroupManagement.setInviteUsername(inviteUsername);
                            robotGroupManagement.setGroupId(groupId);
                            robotGroupManagement.setGroupTitle(groupTitle);
                            robotGroupManagement.setLink(link);
                            springyBot.getRobotGroupManagement().add(robotGroupManagement);
                            springyBotServiceImpl.save(springyBot);
                        });
            }
        }
    }
}
