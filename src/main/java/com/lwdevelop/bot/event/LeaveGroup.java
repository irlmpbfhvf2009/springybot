package com.lwdevelop.bot.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Message;

import com.lwdevelop.bot.Common;
import com.lwdevelop.entity.InvitationThreshold;
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
    private Common common;

    public LeaveGroup(Common common) {
        this.common = common;
        this.groupId = common.getUpdate().getMessage().getChat().getId();
        this.botId = common.getBotId();
    }

    public void isBotLeave() {
        Message message = this.common.getUpdate().getMessage();

        SpringyBot springyBot = springyBotServiceImpl.findById(common.getSpringyBotId()).get();
        springyBot.getRobotGroupManagement().stream()
                .filter(rgm -> hasTarget(rgm))
                .findFirst()
                .ifPresent(g -> {
                    g.setStatus(false);
                });
        this.springyBotServiceImpl.save(springyBot);
        log.info("{} bot leave {} group", common.getBot().getBotUsername(), message.getChat().getTitle());

    }

    public void isUserLeave() {
        // SpringyBot springyBot = springyBotServiceImpl.findById(common.getSpringyBotId()).get();
        // springyBot.getInvitationThreshold().stream()
        //         .filter(it -> invite_hasTarget(it))
        //         .findFirst()
        //         .ifPresent(g -> {
        //             g.setInviteStatus(false);
        //         });
        // springyBot.getInvitationThreshold().stream()
        //         .filter(it -> invite_hasTarget(it))
        //         .findFirst()
        //         .ifPresent(g -> {
        //             g.setInvitedStatus(false);
        //         });
        // this.springyBotServiceImpl.save(springyBot);
        // log.info("{} user leave {} group");

    }

    private Boolean hasTarget(RobotGroupManagement rgm) {
        return rgm.getBotId().equals(this.botId) && rgm.getGroupId().equals(this.groupId);
    }

    private Boolean invite_hasTarget(InvitationThreshold it) {
        return it.getInviteId().equals(common.getUpdate().getMessage().getFrom().getId());
    }
    // private Boolean invited_hasTarget(InvitationThreshold it) {
    //     return it.getInvitedId().equals(common.getUpdate().getMessage().get);
    // }
}
