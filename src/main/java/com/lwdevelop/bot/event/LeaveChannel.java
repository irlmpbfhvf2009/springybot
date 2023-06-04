package com.lwdevelop.bot.event;

import org.springframework.beans.factory.annotation.Autowired;
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
    private Boolean isLeft;
    private Long userId;

    public LeaveChannel(Common common) {
        this.common = common;
        this.botId = common.getBotId();
        this.springyBot = springyBotServiceImpl.findById(common.getSpringyBotId()).get();

    }

    public void handler(Boolean isBot){
        if(isBot){
            this.channelTitle = common.getUpdate().getMyChatMember().getChat().getTitle();
            this.springyBot.getRobotChannelManagement().stream()
                    .filter(rgm -> hasTarget(rgm))
                    .findFirst()
                    .ifPresent(c -> {
                        c.setStatus(false);
                    });
    
            this.springyBotServiceImpl.save(springyBot);
            log.info("{} bot leave {} channel", this.common.getBot().getBotUsername(), this.chatMemberUpdatedChatTitle)
        }else{
            this.channelId = common.getUpdate().getChatMember().getChat().getId();
            this.channelTitle = common.getUpdate().getChatMember().getChat().getTitle();
            this.userId = common.getUpdate().getChatMember().getNewChatMember().getUser().getId();
    
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

    public void isBotLeaveChannel() {
        this.channelTitle = common.getUpdate().getMyChatMember().getChat().getTitle();

        this.springyBot.getRobotChannelManagement().stream()
                .filter(rgm -> hasTarget(rgm))
                .findFirst()
                .ifPresent(c -> {
                    c.setStatus(false);
                });

        this.springyBotServiceImpl.save(springyBot);
        log.info("{} bot leave {} channel", this.common.getBot().getBotUsername(), this.chatMemberUpdatedChatTitle);

    }

    public void isUserLeaveChannel() {
        this.channelId = common.getUpdate().getChatMember().getChat().getId();
        this.channelTitle = common.getUpdate().getChatMember().getChat().getTitle();
        this.isLeft = common.getUpdate().getChatMember().getNewChatMember().getStatus().equals("left") ? true : false;
        this.userId = common.getUpdate().getChatMember().getNewChatMember().getUser().getId();

        if (isLeft) {
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
