package com.lwdevelop.bot.handler;

import org.telegram.telegrambots.meta.api.objects.Message;

import com.lwdevelop.entity.RobotGroupManagement;
import com.lwdevelop.entity.SpringyBot;
import com.lwdevelop.service.impl.RobotGroupManagementServiceImpl;
import com.lwdevelop.service.impl.SpringyBotServiceImpl;

public class LeaveGroupEvent {
    public void handler(Message message, SpringyBotServiceImpl springyBotServiceImpl,
            RobotGroupManagementServiceImpl robotGroupManagementServiceImpl, String token,Long botId) {
                
        Long groupId = message.getChat().getId();
        SpringyBot springyBot = springyBotServiceImpl.findByToken(token);
        RobotGroupManagement robotGroupManagement = robotGroupManagementServiceImpl.findByBotIdAndGroupId(botId, groupId);

        springyBot.getRobotGroupManagement().removeIf(rgm -> rgm.getGroupId().equals(groupId) && rgm.getBotId().equals(botId) );
        springyBotServiceImpl.save(springyBot);
        robotGroupManagementServiceImpl.deleteById(robotGroupManagement.getId());
        
    }
}
