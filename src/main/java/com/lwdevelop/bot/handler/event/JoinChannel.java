package com.lwdevelop.bot.handler.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.ChatMemberUpdated;

import com.lwdevelop.bot.utils.Common;
import com.lwdevelop.entity.RobotChannelManagement;
import com.lwdevelop.entity.SpringyBot;
import com.lwdevelop.service.impl.SpringyBotServiceImpl;
import com.lwdevelop.utils.SpringUtils;

public class JoinChannel {

    @Autowired
    private SpringyBotServiceImpl springyBotServiceImpl = SpringUtils.getApplicationContext()
            .getBean(SpringyBotServiceImpl.class);

    private Common common;
    private ChatMemberUpdated chatMemberUpdated;
    private Long botId;
    private Long inviteId;
    private String inviteFirstname;
    private String inviteUsername;
    private String inviteLastname;
    private Long channelId;
    private String channelTitle;

    public void isBotJoin(Common common) {

        System.out.println("入頻道事件");

        ChatMemberUpdated chatMemberUpdated = common.getChatMemberUpdated();

        SpringyBot springyBot = springyBotServiceImpl.findById(common.getSpringyBotId()).get();
        springyBot.getRobotChannelManagement().stream()
            .filter(rcm -> hasTarget(rcm))
            .findFirst()
            .ifPresentOrElse(null, () ->
                springyBot.getRobotChannelManagement().add(getRobotChannelManagement());
                springyBotServiceImpl.save(springyBot);
            );
        
    }

    private Boolean hasTarget(RobotChannelManagement rcm) {
        return rcm.getChannelId().equals(this.channelId) && rcm.getBotId().equals(this.botId);
    }

    private RobotChannelManagement getRobotChannelManagement() {
        RobotChannelManagement robotGroupManagement = new RobotChannelManagement();
        robotGroupManagement.setBotId(this.botId);
        robotGroupManagement.setInviteId(this.inviteId);
        robotGroupManagement.setInviteFirstname(this.inviteFirstname);
        robotGroupManagement.setInviteUsername(this.inviteUsername);
        robotGroupManagement.setInviteLastname(this.inviteLastname);
        robotGroupManagement.setChannelId(this.channelId);
        robotGroupManagement.setChannelTitle(this.channelTitle);
        robotGroupManagement.setLink(this.common.getInviteLink());
        return robotGroupManagement;
    }

}
