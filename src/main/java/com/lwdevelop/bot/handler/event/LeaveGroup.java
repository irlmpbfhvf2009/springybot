package com.lwdevelop.bot.handler.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Message;
import com.lwdevelop.bot.utils.Common;
import com.lwdevelop.entity.RobotGroupManagement;
import com.lwdevelop.entity.SpringyBot;
import com.lwdevelop.service.impl.SpringyBotServiceImpl;
import com.lwdevelop.utils.SpringUtils;

public class LeaveGroup {

    // @Autowired
    // private RobotGroupManagementServiceImpl robotGroupManagementServiceImpl = SpringUtils.getApplicationContext()
    //         .getBean(RobotGroupManagementServiceImpl.class);

    @Autowired
    private SpringyBotServiceImpl springyBotServiceImpl = SpringUtils.getApplicationContext()
            .getBean(SpringyBotServiceImpl.class);

    private Long groupId;
    private Long botId;

    public void isBotLeave(Common common) {
        Message message =  common.getUpdate().getMessage();
        this.groupId = message.getChat().getId();
        this.botId = common.getBotId();

        SpringyBot springyBot = springyBotServiceImpl.findById(common.getSpringyBotId()).get();
        springyBot.getRobotGroupManagement().stream()
            .filter(rgm -> hasTarget(rgm))
            .findFirst()
            .ifPresent(g -> {
                g.setStatus(false);
            });
        this.springyBotServiceImpl.save(springyBot);

        // springyBot.getRobotGroupManagement().removeIf(rgm -> hasTarget(rgm));
        // this.springyBotServiceImpl.save(springyBot);
        // RobotGroupManagement robotGroupManagement = this.robotGroupManagementServiceImpl
        //         .findByBotIdAndGroupId(this.botId, this.groupId);
        // this.robotGroupManagementServiceImpl.deleteById(robotGroupManagement.getId());

    }

    private Boolean hasTarget(RobotGroupManagement rgm) {
        return rgm.getBotId().equals(this.botId) && rgm.getGroupId().equals(this.groupId);
    }
    // private Boolean hasTarget(RobotGroupManagement rgm) {
    //     return rgm.getGroupId().equals(this.groupId) && rgm.getBotId().equals(this.botId);
    // }
}
