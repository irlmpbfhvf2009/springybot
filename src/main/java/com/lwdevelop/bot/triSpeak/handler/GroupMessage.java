package com.lwdevelop.bot.triSpeak.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import com.lwdevelop.bot.Common;
import com.lwdevelop.bot.triSpeak.utils.SpringyBotEnum;
import com.lwdevelop.bot.triSpeak.utils.SystemMessageTask;
import com.lwdevelop.dto.ConfigDTO;
import com.lwdevelop.entity.SpringyBot;
import com.lwdevelop.service.impl.SpringyBotServiceImpl;
import com.lwdevelop.utils.SpringUtils;

public class GroupMessage {

    private Common common;
    private Long chatId_long;
    private String chatId;
    private Long userId;
    private Integer messageId;
    private Boolean followChannelSet;
    private int deleteSeconds;
    private Boolean inviteFriendsSet;
    private int inviteFriendsQuantity;
    private int inviteFriendsAutoClearTime;
    private Long channel_id;
    private String channel_title;
    private String username;
    private String firstname;
    private String lastname;
    private static List<SystemMessageTask> systemMessageTask;
    private static HashMap<Long, HashMap<Long, HashMap<Long, String>>> warnMessageMap;
    @Autowired
    private SpringyBotServiceImpl springyBotServiceImpl = SpringUtils.getApplicationContext()
            .getBean(SpringyBotServiceImpl.class);

    public GroupMessage(Common common) {
        this.common = common;
        this.chatId = String.valueOf(common.getUpdate().getMessage().getChatId());
        this.chatId_long = common.getUpdate().getMessage().getChatId();
        this.userId = common.getUpdate().getMessage().getFrom().getId();
        this.messageId = common.getUpdate().getMessage().getMessageId();
        this.username = common.getUpdate().getMessage().getFrom().getUserName();
        this.firstname = common.getUpdate().getMessage().getFrom().getFirstName();
        this.lastname = common.getUpdate().getMessage().getFrom().getLastName();
        this.loadConfig();
    }

    // 關注頻道、邀請限制發言開關, 累積獎金開關
    public void handler() {

        // 關注頻道系統
        if (this.followChannelSet) {
            if (isSubscribeChannel()) {
                return;
            }
            // 删除消息
            this.deleteMessage();

            // 发送系统消息
            this.executeOperation();
        }

        // 邀請好友限制發言系統
        // if (this.inviteFriendsSet && this.inviteFriendsQuantity > 0) {
        // // 删除消息
        // this.deleteMessage();
        // SendMessage response = new SendMessage();
        // response.setChatId(chatId);
        // response.setText(channel_title);

        // this.executeOperation();

        // }

    }

    // HashMap<Botid,HashMap<chatId, warn_text>>
    private void executeOperation() {

        String first = this.firstname == null ? "" : this.firstname;
        String last = this.lastname == null ? "" : this.lastname;
        String name = this.username == null ? "@" + first + last : "@" + this.username;
        String warn_text = name;
        Long botId = this.common.getBotId();
        String subscription_warn_text = SpringyBotEnum.warning_text(channel_title);

        if (systemMessageTask == null || systemMessageTask.size() <= 0) {
            GroupMessage.systemMessageTask = new ArrayList<>();
            SystemMessageTask  smt = new SystemMessageTask();
            smt.setCommon(common);
            smt.setWarnMessageMap(GroupMessage.warnMessageMap);
            smt.setDeleteSeconds(deleteSeconds);
            smt.setSubscriptionWarnText(subscription_warn_text);
            GroupMessage.systemMessageTask.add(smt);
        }

        if (GroupMessage.warnMessageMap == null) {
            GroupMessage.warnMessageMap = new HashMap<>();
            GroupMessage.warnMessageMap.put(botId, new HashMap<>());
        }
        if (GroupMessage.warnMessageMap.containsKey(botId)) {
            HashMap<Long, HashMap<Long, String>> message = GroupMessage.warnMessageMap.get(botId);
            if (message.containsKey(this.chatId_long)) {
                systemMessageTask.setCommon(common);
                systemMessageTask.setWarnMessageMap(message);
                systemMessageTask.setDeleteSeconds(deleteSeconds);
                systemMessageTask.setSubscriptionWarnText(subscription_warn_text);
            } else {
                message.put(this.chatId_long, null);
            }

        } else {

        }
        // if (GroupMessage.warnMessageMap == null) {
        // GroupMessage.warnMessageMap = new HashMap<>();
        // GroupMessage.warnMessageMap.put(this.chatId_long, new HashMap<>());
        // }

        // HashMap<Long, String> message = GroupMessage.warnMessageMap.getOrDefault(,
        // new HashMap<>());
        // message.put(this.userId, warn_text);

        if (systemMessageTask == null || !systemMessageTask.getCommon().getBotId().equals(this.common.getBotId())) {
            systemMessageTask = new SystemMessageTask();
            Timer timer = new Timer();
            timer.schedule(systemMessageTask, 0, 10000);
        }
        systemMessageTask.setCommon(common);
        systemMessageTask.setWarnMessageMap(GroupMessage.warnMessageMap);
        systemMessageTask.setDeleteSeconds(deleteSeconds);
        systemMessageTask.setSubscriptionWarnText(subscription_warn_text);

    }

    @Async
    private boolean isSubscribeChannel() {
        try {
            String parseId = String.valueOf(this.channel_id);
            GetChatMember getChatMember = new GetChatMember(parseId, this.userId);
            String status = this.common.executeAsync(getChatMember);
            if (status.equals("administrator")) {
                return true;
            }
            return status.equals("left") ? false : true;
        } catch (NullPointerException e) {
            System.out.println(e.toString());
        }
        return true;
    }

    private void deleteMessage() {
        DeleteMessage deleteMessage = new DeleteMessage(this.chatId, this.messageId);
        this.common.executeAsync(deleteMessage);
    }

    private void loadConfig() {
        HashMap<Long, ConfigDTO> configDTO_map = this.common.getConfigDTO_map();
        Long springyBotId = this.common.getSpringyBotId();

        if (configDTO_map.get(springyBotId) == null) {
            SpringyBot springyBot = springyBotServiceImpl.findById(springyBotId).get();
            this.followChannelSet = springyBot.getConfig().getFollowChannelSet();
            this.channel_id = springyBot.getConfig().getFollowChannelSet_chatId();
            this.channel_title = springyBot.getConfig().getFollowChannelSet_chatTitle();
            this.deleteSeconds = springyBot.getConfig().getDeleteSeconds();
            this.inviteFriendsSet = springyBot.getConfig().getInviteFriendsSet();
            this.inviteFriendsQuantity = springyBot.getConfig().getInviteFriendsQuantity();
            this.inviteFriendsAutoClearTime = springyBot.getConfig().getInviteFriendsAutoClearTime();

            ConfigDTO configDTO = new ConfigDTO();
            // 關注頻道限制發言相關參數
            configDTO.setFollowChannelSet(this.followChannelSet);
            configDTO.setFollowChannelSet_chatTitle(this.channel_title);
            configDTO.setFollowChannelSet_chatId(this.channel_id);
            configDTO.setDeleteSeconds(this.deleteSeconds);

            // 邀請好友限制發言相關參數
            configDTO.setInviteFriendsSet(this.inviteFriendsSet);
            configDTO.setInviteFriendsQuantity(this.inviteFriendsQuantity);
            configDTO.setInviteFriendsAutoClearTime(this.inviteFriendsAutoClearTime);

            configDTO_map.put(springyBotId, configDTO);
        } else {
            // 關注頻道限制發言相關參數
            this.followChannelSet = configDTO_map.get(springyBotId).getFollowChannelSet();
            this.deleteSeconds = configDTO_map.get(springyBotId).getDeleteSeconds();
            this.channel_id = configDTO_map.get(springyBotId).getFollowChannelSet_chatId();
            this.channel_title = configDTO_map.get(springyBotId).getFollowChannelSet_chatTitle();

            // 邀請好友限制發言相關參數
            this.inviteFriendsSet = configDTO_map.get(springyBotId).getInviteFriendsSet();
            this.inviteFriendsQuantity = configDTO_map.get(springyBotId).getInviteFriendsQuantity();
            this.inviteFriendsAutoClearTime = configDTO_map.get(springyBotId).getInviteFriendsAutoClearTime();
        }

    }
}
