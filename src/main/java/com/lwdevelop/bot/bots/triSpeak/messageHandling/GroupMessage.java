package com.lwdevelop.bot.bots.triSpeak.messageHandling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.springframework.scheduling.annotation.Async;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import com.fasterxml.jackson.core.type.TypeReference;
import com.lwdevelop.bot.bots.utils.Common;
import com.lwdevelop.bot.bots.utils.enum_.TriSpeakEnum;
import com.lwdevelop.bot.bots.utils.keyboardButton.TriSpeakButton;
import com.lwdevelop.bot.chatMessageHandlers.BaseGroupMessage;
import com.lwdevelop.entity.Config;
import com.lwdevelop.entity.InvitationThreshold;
import com.lwdevelop.entity.RecordChannelUsers;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GroupMessage extends BaseGroupMessage {

    private Long configChannelId;
    private String configChannelTitle;
    private Boolean configFollowChannelSet;
    private int configDeleteSeconds;
    private Boolean configInviteFriendsSet;
    private int configInviteFriendsQuantity;
    // private int inviteFriendsAutoClearTime;

    private static ArrayList<Long> recordChatIds = new ArrayList<>();
    private HashMap<Long, List<String>> groupMessageMap;
    private static Timer timer;

    private static ArrayList<Long> recordChatIds2 = new ArrayList<>();
    private HashMap<Long, List<String>> groupMessageMap2;
    private static Timer timer2;


    public GroupMessage(Common common) {
        super(common);
        this.groupMessageMap = common.getGroupMessageMap();
        this.groupMessageMap2 = common.getGroupMessageMap2();
        this.loadConfig();
    }

    // 關注頻道、邀請限制發言開關, 累積獎金開關
    @Override
    public void handler() {
        // 關注頻道系統
        if (this.configFollowChannelSet) {

            boolean isSubscribeChannel = isSubscribeChannel();

            if (!isSubscribeChannel) {

                // 删除消息
                DeleteMessage deleteMessage = new DeleteMessage(chatId_str, messageId);
                this.common.executeAsync(deleteMessage);

                // 發送系统消息
                this.executeOperation();
                // return;
            }
        }

        if (this.configInviteFriendsSet) {
            List<InvitationThreshold> invitationThresholdList = redisUtils.get(
                    "InvitationThreshold_" + springyBotId,
                    new TypeReference<List<InvitationThreshold>>() {
                    });
            int userInviteFriendsQuantity = invitationThresholdList.stream()
                    .filter(i -> this.userId.equals(i.getInviteId()) && i.getInvitedStatus()).mapToInt(i -> 1).sum();
            if (userInviteFriendsQuantity < this.configInviteFriendsQuantity) {
                // 删除消息
                DeleteMessage deleteMessage = new DeleteMessage(chatId_str, messageId);
                this.common.executeAsync(deleteMessage);

                // 发送系统消息
                this.executeOperation2();
            }
            return;
        }

    }

    private void loadConfig() {

        Config config = redisUtils.get("Config_" + springyBotId,
                new TypeReference<Config>() {
                });
        // 關注頻道限制發言相關參數
        this.configFollowChannelSet = config.getFollowChannelSet();
        this.configDeleteSeconds = config.getDeleteSeconds();
        this.configChannelId = config.getFollowChannelSet_chatId();
        this.configChannelTitle = config.getFollowChannelSet_chatTitle();

        // 邀請好友限制發言相關參數
        this.configInviteFriendsSet = config.getInviteFriendsSet();
        this.configInviteFriendsQuantity = config.getInviteFriendsQuantity();
        // this.inviteFriendsAutoClearTime = config.getInviteFriendsAutoClearTime();

    }

    private void executeOperation() {

        Long chatId = this.chatId;
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
                        common.deleteMessageTask(deleteMessage, configDeleteSeconds);
                    }
                }
            };
            timer = new Timer();
            timer.scheduleAtFixedRate(systemMessageTask, 0, 5000); // 每n秒执行一次

        }

    }

    private void executeOperation2() {

        Long chatId = this.chatId;
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
                        common.deleteMessageTask(deleteMessage, configDeleteSeconds);
                    }
                }
            };
            timer2 = new Timer();
            timer2.scheduleAtFixedRate(systemMessageTask, 0, 5000); // 每n秒执行一次

        }

    }

    @Async
    private boolean isSubscribeChannel() {
        List<RecordChannelUsers> recordChannelUsersList = redisUtils.get(
                "RecordChannelUsers_" + springyBotId,
                new TypeReference<List<RecordChannelUsers>>() {
                });

        if (recordChannelUsersList != null) {
            boolean isSubscribed = recordChannelUsersList.stream()
                    .anyMatch(r -> r.getChannelId().equals(this.configChannelId)
                            && r.getUserId().equals(this.userId)
                            && r.getStatus());

            return isSubscribed;
        } else {
            log.info("No RecordChannelUsers key found in Redis.");
            return false;
        }
    }

    private String generate_warning_text() {
        return TriSpeakEnum.warning_text(this.configChannelTitle);
    }

    private String generate_warning_text2() {
        return TriSpeakEnum.warning_text2(this.configInviteFriendsQuantity);
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
