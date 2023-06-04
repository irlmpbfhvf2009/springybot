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
    private SpringyBot springyBot;
    private Message message;
    // private Long left_id;

    public LeaveGroup(Common common) {
        this.common = common;
        this.groupId = common.getUpdate().getMessage().getChat().getId();
        this.botId = common.getBotId();
        this.springyBot = springyBotServiceImpl.findById(common.getSpringyBotId()).get();
        this.message = this.common.getUpdate().getMessage();
    }

    public void isBotLeave() {
        this.springyBot.getRobotGroupManagement().stream()
                .filter(rgm -> hasTarget(rgm))
                .findFirst()
                .ifPresent(g -> {
                    g.setStatus(false);
                });
        this.springyBotServiceImpl.save(springyBot);
        log.info("{} bot leave {} group", common.getBot().getBotUsername(), this.message.getChat().getTitle());

    }

    public void isUserLeave() {
        Long userId = common.getUpdate().getMessage().getLeftChatMember().getId();
        this.springyBot.getInvitationThreshold().stream()
            .filter(it->invite_hasTarget(it,userId) || invited_hasTarget(it, userId))
            .findFirst()
            .ifPresent(g -> {
                if (invite_hasTarget(g, userId)) {
                    g.setInviteStatus(false);
                }
                if (invited_hasTarget(g, userId)) {
                    g.setInvitedStatus(false);
                }
            });
    
            // System.out.println(this.message);
        // this.springyBot.getRecordGroupUsers().stream()
        // .filter(rgu->rgu.getUserId().equals(this.user))

        this.springyBotServiceImpl.save(springyBot);
        log.info("{} user leave {} group");

    }

    private Boolean hasTarget(RobotGroupManagement rgm) {
        return rgm.getBotId().equals(this.botId) && rgm.getGroupId().equals(this.groupId);
    }

    private Boolean invite_hasTarget(InvitationThreshold it,Long userId) {
        return it.getInviteId().equals(userId);
    }
    private Boolean invited_hasTarget(InvitationThreshold it,Long userId) {
        return it.getInvitedId().equals(userId);
    }
}
