package com.lwdevelop.bot.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.ChatMemberUpdated;

import com.lwdevelop.bot.Common;
import com.lwdevelop.entity.RobotChannelManagement;
import com.lwdevelop.entity.SpringyBot;
import com.lwdevelop.service.impl.SpringyBotServiceImpl;
import com.lwdevelop.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LeaveChannel {

    @Autowired
    private SpringyBotServiceImpl springyBotServiceImpl = SpringUtils.getApplicationContext()
            .getBean(SpringyBotServiceImpl.class);

    private Long channelId;
    private Long botId;
    private Common common;
    private String chatMemberUpdatedChatTitle;
    
    public LeaveChannel(Common common){
        this.common = common;
        ChatMemberUpdated chatMemberUpdated = common.getChatMemberUpdated();
        this.channelId = chatMemberUpdated.getChat().getId();
        this.botId = common.getBotId();
        this.chatMemberUpdatedChatTitle = chatMemberUpdated.getChat().getTitle();
    }

    public void isBotLeave() {

        SpringyBot springyBot = springyBotServiceImpl.findById(common.getSpringyBotId()).get();
        springyBot.getRobotChannelManagement().stream()
            .filter(rgm -> hasTarget(rgm))
            .findFirst()
            .ifPresent(c -> {
                c.setStatus(false);
            });

        this.springyBotServiceImpl.save(springyBot);
        log.info("{} bot leave {} channel",this.common.getBot().getBotUsername(),this.chatMemberUpdatedChatTitle);


    }

    private Boolean hasTarget(RobotChannelManagement rcm) {
        return rcm.getBotId().equals(this.botId) && rcm.getChannelId().equals(this.channelId);
    }
}
