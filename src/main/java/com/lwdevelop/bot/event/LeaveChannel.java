package com.lwdevelop.bot.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.ChatMemberUpdated;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;

import com.lwdevelop.bot.Common;
import com.lwdevelop.entity.InvitationThreshold;
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
    private String channelTitle;
    private Long botId;
    private Common common;
    private String chatMemberUpdatedChatTitle;
    private SpringyBot springyBot;
    private Long userId;
    private Boolean isBot;

    public LeaveChannel(Common common) {
        this.common = common;
        this.botId = common.getBotId();
        this.springyBot = springyBotServiceImpl.findById(common.getSpringyBotId()).get();
        ChatMemberUpdated chatMemberUpdated = common.getUpdate().getMyChatMember();
        ChatMember chatMember = chatMemberUpdated.getNewChatMember();
        this.isBot = chatMember.getUser().getIsBot() && chatMember.getUser().getId().equals(common.getBotId());
        this.channelTitle = common.getUpdate().getMyChatMember().getChat().getTitle();
        this.channelId = common.getUpdate().getChatMember().getChat().getId();
        this.userId = common.getUpdate().getChatMember().getNewChatMember().getUser().getId();
    }

    public void handler(){
        if(this.isBot){
            this.springyBot.getRobotChannelManagement().stream()
                    .filter(rgm -> hasTarget(rgm))
                    .findFirst()
                    .ifPresent(c -> {
                        c.setStatus(false);
                    });
    
            this.springyBotServiceImpl.save(springyBot);
            log.info("{} bot leave {} channel", this.common.getBot().getBotUsername(), this.chatMemberUpdatedChatTitle);
        }else{
                this.springyBot.getInvitationThreshold().stream()
                        .filter(it -> hasTarget(it))
                        .findFirst()
                        .ifPresent(it -> {
                            it.setInvitedStatus(false);
                        });
    
                this.springyBot.getRecordChannelUsers().stream()
                        .filter(rcu -> rcu.getUserId() == this.userId)
                        .findAny()
                        .ifPresent(rcu -> {
                            rcu.setStatus(false);
                        });
                        
                springyBotServiceImpl.save(this.springyBot);
                log.info("user [{}] leave channel {}", this.userId, this.channelTitle);
            
        }
    }

    private Boolean hasTarget(RobotChannelManagement rcm) {
        return rcm.getBotId().equals(this.botId) && rcm.getChannelId().equals(this.channelId);
    }

    private Boolean hasTarget(InvitationThreshold it) {
        return it.getChatId().equals(this.channelId) && it.getType().equals("channel")
                && it.getInvitedId().equals(this.userId);
    }

}
