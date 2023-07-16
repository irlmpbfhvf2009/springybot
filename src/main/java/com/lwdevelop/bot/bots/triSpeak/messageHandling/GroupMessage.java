package com.lwdevelop.bot.bots.triSpeak.messageHandling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import com.fasterxml.jackson.core.type.TypeReference;
import com.lwdevelop.bot.bots.utils.Common;
import com.lwdevelop.bot.bots.utils.enum_.TriSpeakEnum;
import com.lwdevelop.bot.bots.utils.keyboardButton.TriSpeakButton;
import com.lwdevelop.entity.Config;
import com.lwdevelop.entity.InvitationThreshold;
import com.lwdevelop.entity.RecordChannelUsers;
import com.lwdevelop.utils.RedisUtils;
import com.lwdevelop.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GroupMessage {

    private Common common;
    private Long chatId_long;
    private String chatId;
    // private Long botId;
    private Long userId;
    private Integer messageId;
    private Long channel_id;
    private String channel_title;
    private String username;
    private String firstname;
    private String lastname;
    private Boolean followChannelSet;
    private int deleteSeconds;
    private Boolean inviteFriendsSet;
    private int inviteFriendsQuantity;
    // private int inviteFriendsAutoClearTime;

    private static ArrayList<Long> recordChatIds = new ArrayList<>();
    private HashMap<Long, List<String>> groupMessageMap;
    private static Timer timer;

    private static ArrayList<Long> recordChatIds2 = new ArrayList<>();
    private HashMap<Long, List<String>> groupMessageMap2;
    private static Timer timer2;

    @Autowired
    private RedisUtils redisUtils = SpringUtils.getApplicationContext()
            .getBean(RedisUtils.class);

    public GroupMessage(Common common) {
        this.common = common;
        this.chatId = String.valueOf(common.getUpdate().getMessage().getChatId());
        this.chatId_long = common.getUpdate().getMessage().getChatId();
        this.messageId = common.getUpdate().getMessage().getMessageId();
        this.userId = common.getUpdate().getMessage().getFrom().getId();
        this.username = common.getUpdate().getMessage().getFrom().getUserName() == null ? ""
                : common.getUpdate().getMessage().getFrom().getUserName();
        this.firstname = common.getUpdate().getMessage().getFrom().getFirstName() == null ? ""
                : common.getUpdate().getMessage().getFrom().getFirstName();
        this.lastname = common.getUpdate().getMessage().getFrom().getLastName() == null ? ""
                : common.getUpdate().getMessage().getFrom().getLastName();
        this.groupMessageMap = common.getGroupMessageMap();
        this.groupMessageMap2 = common.getGroupMessageMap2();
        this.loadConfig();
    }

    // 關注頻道、邀請限制發言開關, 累積獎金開關
    public void handler() {
        // 關注頻道系統
        if (this.followChannelSet) {

            boolean isSubscribeChannel = isSubscribeChannel();

            if (!isSubscribeChannel) {

                // 删除消息
                DeleteMessage deleteMessage = new DeleteMessage(chatId, messageId);
                this.common.executeAsync(deleteMessage);

                // 發送系统消息
                this.executeOperation();
                // return;
            }
        }

        if (this.inviteFriendsSet) {
            List<InvitationThreshold> invitationThresholdList = redisUtils.get(
                    "InvitationThreshold_" + this.common.getSpringyBotId(),
                    new TypeReference<List<InvitationThreshold>>() {
                    });
            int userInviteFriendsQuantity = invitationThresholdList.stream()
                    .filter(i -> this.userId.equals(i.getInviteId()) && i.getInvitedStatus()).mapToInt(i -> 1).sum();
            if (userInviteFriendsQuantity < this.inviteFriendsQuantity) {
                // 删除消息
                DeleteMessage deleteMessage = new DeleteMessage(chatId, messageId);
                this.common.executeAsync(deleteMessage);

                // 发送系统消息
                this.executeOperation2();
            }
            return;
        }

    }

    private void loadConfig() {

        Config config = redisUtils.get("Config_" + this.common.getSpringyBotId(),
                new TypeReference<Config>() {
                });
        // 關注頻道限制發言相關參數
        this.followChannelSet = config.getFollowChannelSet();
        this.deleteSeconds = config.getDeleteSeconds();
        this.channel_id = config.getFollowChannelSet_chatId();
        this.channel_title = config.getFollowChannelSet_chatTitle();

        // 邀請好友限制發言相關參數
        this.inviteFriendsSet = config.getInviteFriendsSet();
        this.inviteFriendsQuantity = config.getInviteFriendsQuantity();
        // this.inviteFriendsAutoClearTime = config.getInviteFriendsAutoClearTime();

    }

    private void executeOperation() {

        Long chatId = this.chatId_long;
        String name = generate_username();
        String warn_text = generate_warning_text();

        List<String> message = this.groupMessageMap.getOrDefault(chatId, new ArrayList<>());

        if (!message.contains(name)) {
            message.add(name);
        }

        this.groupMessageMap.put(chatId, message);
        this.common.setGroupMessageMap(this.groupMessageMap);

        if (!recordChatIds.contains(chatId)) {
            recordChatIds.add(chatId);

            TimerTask systemMessageTask = new TimerTask() {
                public void run() {
                    int messageSize = message.size();
                    if (messageSize != 0) {
                        StringBuilder textBuilder = new StringBuilder();
                        for (String msg : message) {
                            textBuilder.append(msg).append("\n");
                        }
                        textBuilder.append(warn_text);
                        message.clear();
                        SendMessage response = new SendMessage(String.valueOf(chatId), textBuilder.toString());
                        if (common.getBotId().equals(5822751184L) || common.getBotId().equals(5901295307L)) {
                            InlineKeyboardMarkup inlineKeyboardButton = new TriSpeakButton(common).advertise();
                            response.setReplyMarkup(inlineKeyboardButton);
                        }
                        Integer msgId = common.executeAsync(response);
                        DeleteMessage deleteMessage = new DeleteMessage(String.valueOf(chatId), msgId);
                        common.deleteMessageTask(deleteMessage, deleteSeconds);
                    }
                }
            };
            timer = new Timer();
            timer.scheduleAtFixedRate(systemMessageTask, 0, 4500); // 每n秒执行一次

        }

    }

    private void executeOperation2() {

        Long chatId = this.chatId_long;
        String name = generate_username();
        String warn_text = generate_warning_text2();

        List<String> message = this.groupMessageMap2.getOrDefault(chatId, new ArrayList<>());

        if (!message.contains(name)) {
            message.add(name);
        }

        this.groupMessageMap2.put(chatId, message);
        this.common.setGroupMessageMap2(this.groupMessageMap2);

        if (!recordChatIds2.contains(chatId)) {
            recordChatIds2.add(chatId);

            TimerTask systemMessageTask = new TimerTask() {
                public void run() {
                    int messageSize = message.size();
                    if (messageSize != 0) {
                        StringBuilder textBuilder = new StringBuilder();
                        for (String msg : message) {
                            textBuilder.append(msg).append("\n");
                        }
                        textBuilder.append(warn_text);
                        message.clear();
                        SendMessage response = new SendMessage(String.valueOf(chatId), textBuilder.toString());
                        if (common.getBotId().equals(5822751184L) || common.getBotId().equals(5901295307L)) {
                            InlineKeyboardMarkup inlineKeyboardButton = new TriSpeakButton(common).advertise();
                            response.setReplyMarkup(inlineKeyboardButton);
                        }
                        Integer msgId = common.executeAsync(response);
                        DeleteMessage deleteMessage = new DeleteMessage(String.valueOf(chatId), msgId);
                        common.deleteMessageTask(deleteMessage, deleteSeconds);
                    }
                }
            };
            timer2 = new Timer();
            timer2.scheduleAtFixedRate(systemMessageTask, 0, 4500); // 每n秒执行一次

        }

    }

    @Async
    private boolean isSubscribeChannel() {
        List<RecordChannelUsers> recordChannelUsersList = redisUtils.get(
                "RecordChannelUsers_" + this.common.getSpringyBotId(),
                new TypeReference<List<RecordChannelUsers>>() {
                });

        if (recordChannelUsersList != null) {
            boolean isSubscribed = recordChannelUsersList.stream()
                    .anyMatch(r -> r.getChannelId().equals(this.channel_id)
                            && r.getUserId().equals(this.userId)
                            && r.getStatus());

            return isSubscribed;
        } else {
            log.info("No RecordChannelUsers key found in Redis.");
            return false;
        }
    }

    private String generate_warning_text() {
        return TriSpeakEnum.warning_text(this.channel_title);
    }

    private String generate_warning_text2() {
        return TriSpeakEnum.warning_text2(this.inviteFriendsQuantity);
    }

    private String generate_username() {
        if (this.username == null || this.username.length() < 2) {
            this.firstname = this.firstname == null ? "" : this.firstname;
            this.lastname = this.lastname == null ? "" : this.lastname;
            return "@" + this.firstname + this.lastname;
        } else {
            return "@" + this.username;
        }
    }

}
