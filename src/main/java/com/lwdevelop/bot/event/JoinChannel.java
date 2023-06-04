package com.lwdevelop.bot.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.ChatMemberUpdated;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import com.lwdevelop.bot.Common;
import com.lwdevelop.entity.InvitationThreshold;
import com.lwdevelop.entity.RecordChannelUsers;
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
    private Long invitedId;
    private String invitedFirstname;
    private String invitedUsername;
    private String invitedLastname;
    private Long chatId;
    private String chatTitle;
    private SpringyBot springyBot;
    private Long userId;
    private Boolean isBot;
    private User user;
    private User from;

    public JoinChannel(Common common) {
        this.common = common;
        this.botId = common.getBotId();
        this.springyBot = springyBotServiceImpl.findById(common.getSpringyBotId()).get();
        ChatMemberUpdated chatMemberUpdated = common.getUpdate().getMyChatMember();
        ChatMember chatMember = chatMemberUpdated.getNewChatMember();
        this.user = chatMember.getUser();
        this.from = chatMemberUpdated.getFrom();
        this.isBot = chatMember.getUser().getIsBot() && chatMember.getUser().getId().equals(common.getBotId());
        this.chatId = chatMemberUpdated.getChat().getId();
        this.chatTitle = chatMemberUpdated.getChat().getTitle();
        this.inviteId = this.from.getId();
        this.inviteFirstname = this.from.getFirstName();
        this.inviteUsername = this.from.getUserName();
        this.inviteLastname = this.from.getLastName();
        this.invitedId = this.user.getId();
        this.invitedFirstname = this.user.getFirstName();
        this.invitedUsername = this.user.getUserName();
        this.invitedLastname = this.user.getLastName();

    }

    public void handler() {
        String formatChat = this.chatTitle +  "[" + this.chatId + "]";
        String formatBot = common.formatBot();
        String formatUser  = common.formatUser(this.user);
        if (this.isBot) {
            // 監測訂閱頻道後解除限制
            // this.springyBot.getRestrictMember().stream()
            //         .filter(rm -> rm.getUserId().equals(this.userId) && rm.getStatus())
            //         .findAny()
            //         .ifPresent(rm -> {
            //             rm.setStatus(false);
            //             ChatPermissions chatPermissions = new ChatPermissions();
            //             chatPermissions.setCanSendMessages(true);
            //             chatPermissions.setCanChangeInfo(true);
            //             chatPermissions.setCanInviteUsers(true);
            //             chatPermissions.setCanPinMessages(true);
            //             chatPermissions.setCanSendMediaMessages(true);
            //             chatPermissions.setCanAddWebPagePreviews(true);
            //             chatPermissions.setCanSendOtherMessages(true);
            //             chatPermissions.setCanSendPolls(true);
            //             Calendar calendar = Calendar.getInstance();
            //             calendar.add(Calendar.SECOND, 1);
            //             int untilDate = (int) (calendar.getTimeInMillis() / 1000);
            //             RestrictChatMember restrictChatMember = new RestrictChatMember(rm.getChatId(), this.userId,
            //                     chatPermissions, untilDate);
            //             this.common.executeAsync(restrictChatMember);

            //         });

            // 邀請紀錄
            this.springyBot.getInvitationThreshold().stream()
                    .filter(it -> hasTarget(it))
                    .findFirst()
                    .ifPresentOrElse(it -> {
                        it.setInvitedStatus(true);
                    }, () -> {
                        this.springyBot.getInvitationThreshold().add(this.getInvitationThreshold());
                    });

            // 入群紀錄
            this.springyBot.getRecordChannelUsers().stream()
                    .filter(rcu -> rcu.getUserId() == this.invitedId)
                    .findAny()
                    .ifPresentOrElse(rcu -> {
                        rcu.setStatus(true);
                    }, () -> {
                        this.springyBot.getRecordChannelUsers().add(this.getRecordChannelUsers());
                    });

            springyBotServiceImpl.save(this.springyBot);
        } else {
            this.chatTitle = common.getUpdate().getMyChatMember().getChat().getTitle();
            this.chatId = common.getUpdate().getMyChatMember().getChat().getId();
            this.invitedId = common.getUpdate().getMyChatMember().getFrom().getId();
            this.invitedFirstname = common.getUpdate().getMyChatMember().getFrom().getFirstName();
            this.invitedUsername = common.getUpdate().getMyChatMember().getFrom().getUserName();
            this.invitedLastname = common.getUpdate().getMyChatMember().getFrom().getLastName();
            springyBot.getRobotChannelManagement().stream()
                    .filter(rcm -> hasTarget(rcm))
                    .findFirst()
                    .ifPresentOrElse(rcm -> {
                        rcm.setStatus(true);
                    }, () -> {
                        springyBot.getRobotChannelManagement().add(getRobotChannelManagement());
                    });
            springyBotServiceImpl.save(springyBot);
        }

        log.info("{} -> {} join {} channel", formatBot, formatUser ,formatChat);

    }

    private Boolean hasTarget(RobotChannelManagement rcm) {
        return rcm.getChannelId().equals(this.chatId) && rcm.getBotId().equals(this.botId);
    }

    private Boolean hasTarget(InvitationThreshold it) {
        return it.getChatId().equals(this.chatId) && it.getType().equals("channel")
                && it.getInvitedId().equals(this.invitedId);
    }

    private RobotChannelManagement getRobotChannelManagement() {
        RobotChannelManagement robotGroupManagement = new RobotChannelManagement();
        robotGroupManagement.setBotId(this.botId);
        robotGroupManagement.setInviteId(this.invitedId);
        robotGroupManagement.setInviteFirstname(this.invitedFirstname);
        robotGroupManagement.setInviteUsername(this.invitedUsername);
        robotGroupManagement.setInviteLastname(this.invitedLastname);
        robotGroupManagement.setChannelId(this.chatId);
        robotGroupManagement.setChannelTitle(this.chatTitle);
        robotGroupManagement.setLink(this.common.getInviteLink());
        robotGroupManagement.setStatus(true);
        return robotGroupManagement;
    }

    private InvitationThreshold getInvitationThreshold() {
        InvitationThreshold invitationThreshold = new InvitationThreshold();
        invitationThreshold.setBotId(this.botId);
        invitationThreshold.setChatId(this.chatId);
        invitationThreshold.setChatTitle(this.chatTitle);
        invitationThreshold.setInvitedId(this.inviteId);
        invitationThreshold.setInvitedFirstname(this.inviteFirstname);
        invitationThreshold.setInvitedUsername(this.inviteUsername);
        invitationThreshold.setInvitedLastname(this.inviteLastname);
        invitationThreshold.setInvitedId(this.invitedId);
        invitationThreshold.setInvitedFirstname(this.invitedFirstname);
        invitationThreshold.setInvitedUsername(this.invitedUsername);
        invitationThreshold.setInvitedLastname(this.invitedLastname);
        invitationThreshold.setInvitedStatus(true);
        invitationThreshold.setType("channel");
        return invitationThreshold;
    }

    private RecordChannelUsers getRecordChannelUsers() {
        RecordChannelUsers recordChannelUsers = new RecordChannelUsers();
        recordChannelUsers.setChannelId(this.chatId);
        recordChannelUsers.setChannelTitle(this.chatTitle);
        recordChannelUsers.setFirstname(this.invitedFirstname);
        recordChannelUsers.setLastname(this.invitedLastname);
        recordChannelUsers.setStatus(true);
        recordChannelUsers.setUserId(this.userId);
        recordChannelUsers.setUsername(this.invitedUsername);
        return recordChannelUsers;
    }

}
