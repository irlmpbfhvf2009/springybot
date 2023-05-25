package com.lwdevelop.bot.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Message;

import com.lwdevelop.bot.Common;
import com.lwdevelop.entity.RobotGroupManagement;
import com.lwdevelop.entity.SpringyBot;
import com.lwdevelop.service.impl.SpringyBotServiceImpl;
import com.lwdevelop.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LeaveGroup {

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
        log.info("{} bot leave {} group",common.getBot().getBotUsername(), message.getChat().getTitle());

    }

    private Boolean hasTarget(RobotGroupManagement rgm) {
        return rgm.getBotId().equals(this.botId) && rgm.getGroupId().equals(this.groupId);
    }
}