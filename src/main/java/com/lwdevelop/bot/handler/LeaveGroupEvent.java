package com.lwdevelop.bot.handler;

import org.telegram.telegrambots.meta.api.objects.Message;

import com.lwdevelop.entity.RobotGroupManagement;
import com.lwdevelop.entity.SpringyBot;
import com.lwdevelop.service.impl.RobotGroupManagementServiceImpl;
import com.lwdevelop.service.impl.SpringyBotServiceImpl;

public class LeaveGroupEvent {
    public void handler(Message message, Long botId,SpringyBot springyBot,RobotGroupManagementServiceImpl robotGroupManagementServiceImpl, SpringyBotServiceImpl springyBotServiceImpl) {
        Long groupId = message.getChat().getId();
        springyBot.getRobotGroupManagement().removeIf(rgm -> rgm.getGroupId().equals(groupId) && rgm.getBotId().equals(botId));
        springyBotServiceImpl.save(springyBot);
        RobotGroupManagement robotGroupManagement = robotGroupManagementServiceImpl.findByBotIdAndGroupId(botId, groupId);
        robotGroupManagementServiceImpl.deleteById(robotGroupManagement.getId());
    }
}
