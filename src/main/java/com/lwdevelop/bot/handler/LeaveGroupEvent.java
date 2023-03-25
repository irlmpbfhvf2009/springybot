package com.lwdevelop.bot.handler;

import org.telegram.telegrambots.meta.api.objects.Message;

import com.lwdevelop.entity.SpringyBot;
import com.lwdevelop.service.impl.RobotGroupManagementServiceImpl;
import com.lwdevelop.service.impl.SpringyBotServiceImpl;

public class LeaveGroupEvent {
    public void handler(Message message, SpringyBotServiceImpl springyBotServiceImpl,
            RobotGroupManagementServiceImpl robotGroupManagementServiceImpl, String token,Long botId) {
        Long groupId = message.getChat().getId();

        System.out.println("test LeaveGroupEvent");
        System.out.println("------------------");

        SpringyBot springyBot = springyBotServiceImpl.findByToken(token);
        springyBot.getRobotGroupManagement().removeIf(rgm -> rgm.getGroupId().equals(groupId));
        springyBotServiceImpl.save(springyBot);
        robotGroupManagementServiceImpl.deleteByGroupIdAndBotId(groupId,botId);
    }
}
