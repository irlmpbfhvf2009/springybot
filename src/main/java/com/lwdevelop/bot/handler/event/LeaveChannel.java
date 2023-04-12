package com.lwdevelop.bot.handler.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.ChatMemberUpdated;

import com.lwdevelop.bot.utils.Common;
import com.lwdevelop.entity.RobotChannelManagement;
import com.lwdevelop.entity.SpringyBot;
import com.lwdevelop.service.impl.RobotChannelManagementServiceImpl;
import com.lwdevelop.service.impl.SpringyBotServiceImpl;
import com.lwdevelop.utils.SpringUtils;

public class LeaveChannel {
    @Autowired
    private RobotChannelManagementServiceImpl robotChannelManagementServiceImpl = SpringUtils.getApplicationContext()
            .getBean(RobotChannelManagementServiceImpl.class);

    @Autowired
    private SpringyBotServiceImpl springyBotServiceImpl = SpringUtils.getApplicationContext()
            .getBean(SpringyBotServiceImpl.class);

    private Long channelId;
    private Long botId;

    public void isBotLeave(Common common) {

        System.out.println("離開頻道事件");

        ChatMemberUpdated chatMemberUpdated = common.getChatMemberUpdated();
        this.channelId = chatMemberUpdated.getChat().getId();
        this.botId = common.getBotId();

        SpringyBot springyBot = springyBotServiceImpl.findById(common.getSpringyBotId()).get();
        springyBot.getRobotChannelManagement().removeIf(rcm -> hasTarget(rcm));
        RobotChannelManagement robotChannelManagement = this.robotChannelManagementServiceImpl
            .findByBotIdAndChannelId(this.botId, this.channelId);
        this.robotChannelManagementServiceImpl.deleteById(robotChannelManagement.getId());

    }

    private Boolean hasTarget(RobotChannelManagement rcm) {
        return rcm.getChannelId().equals(this.channelId) && rcm.getBotId().equals(this.botId);
    }
}
