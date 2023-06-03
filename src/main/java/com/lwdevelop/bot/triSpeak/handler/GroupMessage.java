package com.lwdevelop.bot.triSpeak.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import com.lwdevelop.bot.Common;
import com.lwdevelop.bot.triSpeak.utils.KeyboardButton;
import com.lwdevelop.bot.triSpeak.utils.SpringyBotEnum;
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
    private HashMap<Long, List<String>> groupMessageMap;
    private static Timer timer;
    private static final InlineKeyboardMarkup inlineKeyboardButton = new KeyboardButton().ddb37_url();

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
        this.groupMessageMap = common.getGroupMessageMap();

        this.loadConfig();
    }

    // 關注頻道、邀請限制發言開關, 累積獎金開關
    public void handler() {

        // 關注頻道系統
        if (this.followChannelSet) {
            if (!isSubscribeChannel()) {
                // 删除消息
                DeleteMessage deleteMessage = new DeleteMessage(chatId, messageId);
                this.common.executeAsync(deleteMessage);

                // 发送系统消息
                String username = generate_username();
                String warn_text = generate_warning_text();
                this.executeOperation(this.chatId_long, username, warn_text);
            }
        }

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

    private static ArrayList<Long> recordChatIds = new ArrayList<>();

    private void executeOperation(Long chatId, String name, String warn_text) {

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
                        response.setReplyMarkup(inlineKeyboardButton);
                        Integer msgId = common.executeAsync(response);
                        DeleteMessage deleteMessage = new DeleteMessage(String.valueOf(chatId), msgId);
                        common.deleteMessageTask(deleteMessage, deleteSeconds);
                    }
                }
            };
            timer = new Timer();
            timer.scheduleAtFixedRate(systemMessageTask, 0, 7000); // 每n秒执行一次

        }

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

    private String generate_warning_text() {
        return SpringyBotEnum.warning_text(this.channel_title);
    }

    private String generate_username() {
        if (this.username == null) {
            this.firstname = this.firstname == null ? "" : this.firstname;
            this.lastname = this.lastname == null ? "" : this.lastname;
            this.username = "@" + this.firstname + this.lastname;
        } else {
            this.username = "@" + this.username;
        }

        return this.username;
    }

}
