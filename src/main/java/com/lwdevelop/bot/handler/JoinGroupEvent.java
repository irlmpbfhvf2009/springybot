package com.lwdevelop.bot.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import com.lwdevelop.entity.RobotGroupManagement;
import com.lwdevelop.entity.SpringyBot;
import com.lwdevelop.service.impl.SpringyBotServiceImpl;
import com.lwdevelop.utils.SpringUtils;

public class JoinGroupEvent {

    @Autowired
    private SpringyBotServiceImpl springyBotServiceImpl = SpringUtils.getApplicationContext().getBean(SpringyBotServiceImpl.class);

    public void handler(Message message, String username,String token) {

        System.out.println(message);
        // 邀請人
        message.getFrom().getId();
        message.getFrom().getFirstName();
        message.getFrom().getUserName();

        Long groupId = message.getChat().getId();
        String groupTitle = message.getChat().getTitle();
        String asd = message.getChat().getInviteLink();
        System.out.println(asd);
        // 被邀請對象
        for (User member : message.getNewChatMembers()) {
            if (username.equals(member.getUserName()) && member.getIsBot()) {

                SpringyBot springyBot = springyBotServiceImpl.findByToken(token);
                RobotGroupManagement robotGroupManagement = new RobotGroupManagement();
                robotGroupManagement.setGroupId(groupId);
                robotGroupManagement.setGroupTitle(groupTitle);
                robotGroupManagement.setLink(asd);
                springyBot.getRobotGroupManagement().add(robotGroupManagement);
                springyBotServiceImpl.save(springyBot);
            }
        }
    }
}
