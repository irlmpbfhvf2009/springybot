package com.lwdevelop.bot.triSpeak.handler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.groupadministration.RestrictChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.ChatPermissions;

import com.lwdevelop.bot.Common;
import com.lwdevelop.bot.triSpeak.utils.SpringyBotEnum;
import com.lwdevelop.dto.ConfigDTO;
import com.lwdevelop.entity.RestrictMember;
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
    private Long channel_id;
    private String channel_title;
    private String username;
    private String firstname;
    private String lastname;
    private HashMap<Long, List<String>> groupMessageMap;
    // private static MessageQueue messageQueue = new MessageQueue();

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
    }

    // private static List<DeleteMessage> deleteSystemMessages = new ArrayList<>();

    public void handler() {
        HashMap<Long, ConfigDTO> configDTO_map = this.common.getConfigDTO_map();
        if (configDTO_map.get(common.getSpringyBotId()) == null) {
            SpringyBot springyBot = springyBotServiceImpl.findById(common.getSpringyBotId()).get();
            this.followChannelSet = springyBot.getConfig().getFollowChannelSet();
            this.channel_id = springyBot.getConfig().getFollowChannelSet_chatId();
            this.channel_title = springyBot.getConfig().getFollowChannelSet_chatTitle();
            this.deleteSeconds = springyBot.getConfig().getDeleteSeconds();
            ConfigDTO configDTO = new ConfigDTO();
            configDTO.setFollowChannelSet(this.followChannelSet);
            configDTO.setFollowChannelSet_chatTitle(this.channel_title);
            configDTO.setFollowChannelSet_chatId(this.channel_id);
            configDTO.setDeleteSeconds(this.deleteSeconds);
            configDTO_map.put(common.getSpringyBotId(), configDTO);
        } else {
            this.followChannelSet = configDTO_map.get(common.getSpringyBotId()).getFollowChannelSet();
            this.deleteSeconds = configDTO_map.get(common.getSpringyBotId()).getDeleteSeconds();
            this.channel_id = configDTO_map.get(common.getSpringyBotId()).getFollowChannelSet_chatId();
            this.channel_title = configDTO_map.get(common.getSpringyBotId()).getFollowChannelSet_chatTitle();
        }

        if (followChannelSet) {
            if (!isSubscribeChannel()) { 

                // telegram 系統限制用戶3分鐘
                this.executeRestrictChatMember(3);

                // 删除消息
                this.deleteMessage(chatId, messageId); 

                // 发送系统消息
                this.executeOperation();
                // SendMessage response = new SendMessage(chatId, generate_warning_text());
                // Integer msgId = common.executeAsync(response);
                // DeleteMessage deleteMessage = new DeleteMessage(chatId, msgId);
                // common.deleteMessageTask(deleteMessage, this.deleteSeconds);
            }
        }

    }

    private void executeRestrictChatMember(int minute){
        ChatPermissions chatPermissions = setChatPermissions_false();
        int untilDate = setUntilDate_minute(minute);
        RestrictChatMember restrictChatMember = new RestrictChatMember(this.chatId, this.userId,
                chatPermissions, untilDate);
        common.executeAsync(restrictChatMember);

        SpringyBot springyBot = springyBotServiceImpl.findById(common.getSpringyBotId()).get();
        Optional<RestrictMember> optionalRestrictMember = springyBot.getRestrictMember().stream()
                .filter(rm -> rm.getChatId().equals(this.chatId) && rm.getUserId().equals(this.userId))
                .findAny();

        if (!optionalRestrictMember.isPresent()) {
            RestrictMember restrictMember = new RestrictMember();
            restrictMember.setChatId(this.chatId);
            restrictMember.setUserId(this.userId);
            restrictMember.setStatus(true);
            springyBot.getRestrictMember().add(restrictMember);
        } else {
            optionalRestrictMember.get().setStatus(true);
        }
        springyBotServiceImpl.save(springyBot);
    }

    private void executeOperation() {

        List<String> message;
        String username = generate_username();
        String warn_text = generate_warning_text();
        
        message = this.groupMessageMap.getOrDefault(this.chatId_long, new ArrayList<>());
        
        if (!message.contains(username)) {
            message.add(username);
        }
        
        this.groupMessageMap.put(this.chatId_long, message);
        this.common.setGroupMessageMap(groupMessageMap);
        
        int messageSize = message.size();
        
        if (messageSize >= 5) {
            StringBuilder textBuilder = new StringBuilder();
            for (String msg : message) {
                textBuilder.append(msg).append("\n");
            }
            textBuilder.append(warn_text);
            message.clear();
            SendMessage response = new SendMessage(chatId, textBuilder.toString());
            Integer msgId = common.executeAsync(response);
            DeleteMessage deleteMessage = new DeleteMessage(chatId, msgId);
            common.deleteMessageTask(deleteMessage, this.deleteSeconds);
        }
    }
    
    
    private ChatPermissions setChatPermissions_false() {
        ChatPermissions chatPermissions = new ChatPermissions();
        chatPermissions.setCanSendMessages(false);
        chatPermissions.setCanChangeInfo(false);
        chatPermissions.setCanInviteUsers(true);
        chatPermissions.setCanPinMessages(false);
        chatPermissions.setCanSendMediaMessages(false);
        chatPermissions.setCanAddWebPagePreviews(false);
        chatPermissions.setCanSendOtherMessages(false);
        chatPermissions.setCanSendPolls(false);
        return chatPermissions;
    }

    private int setUntilDate_minute(int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, minute);
        int untilDate = (int) (calendar.getTimeInMillis() / 1000);
        return untilDate;
    }

    @Async
    private void deleteMessage(String chatId, Integer messageId) {
        DeleteMessage deleteMessage = new DeleteMessage(chatId, messageId);
        common.executeAsync(deleteMessage);
    }

    // @Async
    // private Integer sendSystemMessage(String chatId) {
    //     SendMessage response = new SendMessage(chatId, generate_warning_text());
    //     Integer msgId = common.executeAsync(response);
    //     return msgId;
    // }
    // @Async
    // private void processDeleteSystemMessage(Integer msgId){
    // DeleteMessage deleteSystemMessage = new DeleteMessage(chatId, msgId);
    // deleteSystemMessages.add(deleteSystemMessage);
    // if (deleteSystemMessages.size() == 10) {
    // MessageTaskExecutor taskExecutor = new MessageTaskExecutor(this.common);
    // taskExecutor.executeDeleteMessageTask(deleteSystemMessages, deleteSeconds);
    // // common.deleteMessageTask(deleteSystemMessages, deleteSeconds);
    // deleteSystemMessages = new ArrayList<>();
    // }
    // }

    @Async
    private boolean isSubscribeChannel() {
        String parseId = String.valueOf(this.channel_id);
        GetChatMember getChatMember = new GetChatMember(parseId, this.userId);
        boolean status = this.common.executeAsync(getChatMember).equals("left") ? false : true;
        return status;
    }

    // private String generate_warning_text() {

    //     if (this.username == null) {
    //         this.firstname = this.firstname == null ? "" : this.firstname;
    //         this.lastname = this.lastname == null ? "" : this.lastname;
    //         this.username = this.firstname + this.lastname;
    //     } else {
    //         this.username = "@" + this.username;
    //     }

    //     return SpringyBotEnum.warning_text(this.username, this.channel_title);
    // }
    private String generate_warning_text() {
        return SpringyBotEnum.warning_text(this.channel_title);
    }

    private String generate_username() {
        if (this.username == null) {
            this.firstname = this.firstname == null ? "" : this.firstname;
            this.lastname = this.lastname == null ? "" : this.lastname;
            this.username = this.firstname + this.lastname;
        } else {
            this.username = "@" + this.username;
        }

        return this.username;
    }

}
