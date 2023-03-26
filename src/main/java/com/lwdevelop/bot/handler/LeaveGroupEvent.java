package com.lwdevelop.bot.handler;

import org.telegram.telegrambots.meta.api.objects.Message;

import com.lwdevelop.entity.RobotGroupManagement;
import com.lwdevelop.entity.SpringyBot;
import com.lwdevelop.service.impl.RobotGroupManagementServiceImpl;
import com.lwdevelop.service.impl.SpringyBotServiceImpl;

public class LeaveGroupEvent {
    
    private Long groupId;
    private Long botId;

    public void handler(Message message,String username, Long botId,SpringyBot springyBot,RobotGroupManagementServiceImpl robotGroupManagementServiceImpl, SpringyBotServiceImpl springyBotServiceImpl) {
        this.groupId = message.getChat().getId();
        this.botId = botId;

        if (isRobotBody(message,username)){
            springyBot.getRobotGroupManagement().removeIf(rgm -> hasTarget(rgm));
            springyBotServiceImpl.save(springyBot);
            RobotGroupManagement robotGroupManagement = robotGroupManagementServiceImpl.findByBotIdAndGroupId(botId, groupId);
            robotGroupManagementServiceImpl.deleteById(robotGroupManagement.getId());
        }
    }
    private Boolean isRobotBody(Message message,String username){
        return message.getLeftChatMember().getUserName().equals(username);
    }

    private Boolean hasTarget(RobotGroupManagement rgm){
        return rgm.getGroupId().equals(this.groupId) && rgm.getBotId().equals(this.botId);
    }
}

