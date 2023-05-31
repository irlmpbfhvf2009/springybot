package com.lwdevelop.bot.event;

import org.springframework.beans.factory.annotation.Autowired;
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
    private SpringyBot springyBot;
    
    public LeaveChannel(Common common){
        this.common = common;
        this.channelId = common.getUpdate().getChatMember().getChat().getId();
        this.botId = common.getBotId();
        this.springyBot = springyBotServiceImpl.findById(common.getSpringyBotId()).get();
    }

    public void isBotLeaveChannel() {

        this.springyBot.getRobotChannelManagement().stream()
            .filter(rgm -> hasTarget(rgm))
            .findFirst()
            .ifPresent(c -> {
                c.setStatus(false);
            });

        this.springyBotServiceImpl.save(springyBot);
        log.info("{} bot leave {} channel",this.common.getBot().getBotUsername(),this.chatMemberUpdatedChatTitle);


    }
    public void isUserLeaveChannel(){

    }


    private Boolean hasTarget(RobotChannelManagement rcm) {
        return rcm.getBotId().equals(this.botId) && rcm.getChannelId().equals(this.channelId);
    }
}
