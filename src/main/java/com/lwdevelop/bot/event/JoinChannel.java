package com.lwdevelop.bot.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.groupadministration.RestrictChatMember;
import org.telegram.telegrambots.meta.api.objects.ChatPermissions;
import com.lwdevelop.bot.Common;
import com.lwdevelop.entity.InvitationThreshold;
import com.lwdevelop.entity.RobotChannelManagement;
import com.lwdevelop.entity.SpringyBot;
import com.lwdevelop.service.impl.SpringyBotServiceImpl;
import com.lwdevelop.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JoinChannel {

    @Autowired
    private SpringyBotServiceImpl springyBotServiceImpl = SpringUtils.getApplicationContext()
            .getBean(SpringyBotServiceImpl.class);

    private Common common;
    private Long botId;
    private Long invitedId;
    private String invitedFirstname;
    private String invitedUsername;
    private String invitedLastname;
    private Long channelId;
    private String channelTitle;
    private SpringyBot springyBot;
    private Long userId;

    public JoinChannel(Common common) {
        this.common = common;
        this.botId = common.getBotId();
        this.channelId = common.getUpdate().getChatMember().getChat().getId();
        this.channelTitle = common.getUpdate().getChatMember().getChat().getTitle();
        this.springyBot = springyBotServiceImpl.findById(common.getSpringyBotId()).get();
        this.invitedId = common.getUpdate().getChatMember().getFrom().getId();
        this.invitedFirstname = common.getUpdate().getChatMember().getFrom().getFirstName();
        this.invitedUsername = common.getUpdate().getChatMember().getFrom().getUserName();
        this.invitedLastname = common.getUpdate().getChatMember().getFrom().getLastName();
    }

    public void isUserJoinChannel() {
        System.out.println(common.getUpdate().getChatMember().getFrom());
        if (this.invitedId != null) {
            this.springyBot.getRestrictMember().stream()
                    .filter(rm -> rm.getUserId().equals(this.userId) && rm.getStatus())
                    .findAny()
                    .ifPresent(rm -> {
                        rm.setStatus(false);
                        ChatPermissions chatPermissions = new ChatPermissions();
                        chatPermissions.setCanSendMessages(true);
                        chatPermissions.setCanChangeInfo(true);
                        chatPermissions.setCanInviteUsers(true);
                        chatPermissions.setCanPinMessages(true);
                        chatPermissions.setCanSendMediaMessages(true);
                        chatPermissions.setCanAddWebPagePreviews(true);
                        chatPermissions.setCanSendOtherMessages(true);
                        chatPermissions.setCanSendPolls(true);
                        int untilDate = (int) (System.currentTimeMillis() / 1000) + 1;
                        RestrictChatMember restrictChatMember = new RestrictChatMember(rm.getChatId(), this.userId,
                                chatPermissions, untilDate);
                        this.common.executeAsync(restrictChatMember);

                    });

            this.springyBot.getInvitationThreshold().stream()
                    .filter(it -> hasTarget(it))
                    .findFirst()
                    .ifPresentOrElse(it -> {
                        it.setInvitedStatus(true);
                    }, () -> {
                        this.springyBot.getInvitationThreshold().add(this.getInvitationThreshold());
                    });
            springyBotServiceImpl.save(this.springyBot);
            log.info("user [{}] join channel {}", this.invitedId, this.channelTitle);

        }
    }

    public void isBotJoinChannel() {

        springyBot.getRobotChannelManagement().stream()
                .filter(rcm -> hasTarget(rcm))
                .findFirst()
                .ifPresentOrElse(rcm -> {
                    rcm.setStatus(true);
                }, () -> {
                    springyBot.getRobotChannelManagement().add(getRobotChannelManagement());
                });
        springyBotServiceImpl.save(springyBot);
        log.info("{} bot join {} channel", common.getBot().getBotUsername(), this.channelTitle);
    }

    private Boolean hasTarget(RobotChannelManagement rcm) {
        return rcm.getChannelId().equals(this.channelId) && rcm.getBotId().equals(this.botId);
    }

    private Boolean hasTarget(InvitationThreshold it) {
        return it.getChatId().equals(this.channelId) && it.getType().equals("channel") && it.getInvitedId().equals(this.invitedId);
    }

    private RobotChannelManagement getRobotChannelManagement() {
        RobotChannelManagement robotGroupManagement = new RobotChannelManagement();
        robotGroupManagement.setBotId(this.botId);
        robotGroupManagement.setInviteId(this.invitedId);
        robotGroupManagement.setInviteFirstname(this.invitedFirstname);
        robotGroupManagement.setInviteUsername(this.invitedUsername);
        robotGroupManagement.setInviteLastname(this.invitedLastname);
        robotGroupManagement.setChannelId(this.channelId);
        robotGroupManagement.setChannelTitle(this.channelTitle);
        robotGroupManagement.setLink(this.common.getInviteLink());
        robotGroupManagement.setStatus(true);
        return robotGroupManagement;
    }

    private InvitationThreshold getInvitationThreshold() {
        InvitationThreshold invitationThreshold = new InvitationThreshold();
        invitationThreshold.setBotId(this.botId);
        invitationThreshold.setChatId(this.channelId);
        invitationThreshold.setChatTitle(this.channelTitle);
        invitationThreshold.setInvitedId(this.invitedId);
        invitationThreshold.setInvitedFirstname(this.invitedFirstname);
        invitationThreshold.setInvitedUsername(this.invitedUsername);
        invitationThreshold.setInvitedLastname(this.invitedLastname);
        invitationThreshold.setInvitedStatus(true);
        invitationThreshold.setType("channel");
        return invitationThreshold;
    }

}
