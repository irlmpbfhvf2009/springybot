package com.lwdevelop.bot.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Message;

import com.lwdevelop.bot.utils.Common;
import com.lwdevelop.entity.RobotGroupManagement;
import com.lwdevelop.entity.SpringyBot;
import com.lwdevelop.service.impl.RobotGroupManagementServiceImpl;
import com.lwdevelop.service.impl.SpringyBotServiceImpl;
import com.lwdevelop.utils.SpringUtils;

public class LeaveGroupEvent {

    @Autowired
    private RobotGroupManagementServiceImpl robotGroupManagementServiceImpl = SpringUtils.getApplicationContext()
            .getBean(RobotGroupManagementServiceImpl.class);

    @Autowired
    private SpringyBotServiceImpl springyBotServiceImpl = SpringUtils.getApplicationContext()
            .getBean(SpringyBotServiceImpl.class);

    private Long groupId;
    private Long botId;

    public void isBotLeave(Common common) {

        Message message = common.getMessage();
        SpringyBot springyBot = springyBotServiceImpl.findById(common.getSpringyBotId()).get();
        this.groupId = message.getChat().getId();
        this.botId = common.getBotId();
        springyBot.getRobotGroupManagement().removeIf(rgm -> hasTarget(rgm));
        this.springyBotServiceImpl.save(springyBot);
        RobotGroupManagement robotGroupManagement = this.robotGroupManagementServiceImpl
                .findByBotIdAndGroupId(this.botId, this.groupId);
        this.robotGroupManagementServiceImpl.deleteById(robotGroupManagement.getId());

    }

    private Boolean hasTarget(RobotGroupManagement rgm) {
        return rgm.getGroupId().equals(this.groupId) && rgm.getBotId().equals(this.botId);
    }
}
