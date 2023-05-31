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
    private Long inviteId;
    private String inviteFirstname;
    private String inviteUsername;
    private String inviteLastname;
    private Long channelId;
    private String channelTitle;
    private SpringyBot springyBot;
    private Long userId;

    public JoinChannel(Common common) {
        this.common = common;
        this.botId = common.getBotId();
        // this.inviteId = common.getChatMemberUpdated().getFrom().getId();
        // this.inviteFirstname = common.getChatMemberUpdated().getFrom().getFirstName();
        // this.inviteUsername = common.getChatMemberUpdated().getFrom().getUserName();
        // this.inviteLastname = common.getChatMemberUpdated().getFrom().getLastName();
        this.channelId = common.getUpdate().getChatMember().getChat().getId();
        this.channelTitle = common.getUpdate().getChatMember().getChat().getTitle();
        this.springyBot = springyBotServiceImpl.findById(common.getSpringyBotId()).get();
        this.userId = common.getUpdate().getChatMember().getNewChatMember().getUser().getId();
        
    }

    public void isUserJoinChannel() {


        springyBot.getRestrictMember().stream()
                .filter(rm -> rm.getUserId().equals(this.userId)&& rm.getStatus())
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
                    RestrictChatMember restrictChatMember = new RestrictChatMember(rm.getChatId(), this.userId , chatPermissions,untilDate);
                    this.common.executeAsync(restrictChatMember);

                });

        springyBot.getInvitationThreshold().stream()
                .filter(it -> hasTarget(it))
                .findFirst()
                .ifPresentOrElse(it -> {
                    // it.setStatus(true);
                }, () -> {
                    springyBot.getInvitationThreshold().add(this.getInvitationThreshold());
                });
        springyBotServiceImpl.save(springyBot);
        // log.info("{} invite {} join channel {}", invite_name, invited_name,
        // this.channelTitle);
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
        return it.getChatId().equals(this.channelId) && it.getType().equals("channel");
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
        robotGroupManagement.setStatus(true);
        return robotGroupManagement;
    }

    private InvitationThreshold getInvitationThreshold() {
        InvitationThreshold invitationThreshold = new InvitationThreshold();
        return invitationThreshold;
    }

}
