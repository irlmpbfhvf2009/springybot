package com.lwdevelop.bot.triSpeak.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import com.lwdevelop.bot.triSpeak.utils.Common;
import com.lwdevelop.bot.triSpeak.utils.MessageTaskExecutor;
import com.lwdevelop.bot.triSpeak.utils.SpringyBotEnum;
import com.lwdevelop.dto.ConfigDTO;
import com.lwdevelop.entity.SpringyBot;
import com.lwdevelop.service.impl.SpringyBotServiceImpl;
import com.lwdevelop.utils.SpringUtils;

public class GroupMessage {

    private Common common;
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

    @Autowired
    private SpringyBotServiceImpl springyBotServiceImpl = SpringUtils.getApplicationContext()
            .getBean(SpringyBotServiceImpl.class);

    public GroupMessage(Common common) {
        this.common = common;
        this.chatId = String.valueOf(common.getUpdate().getMessage().getChatId());
        this.userId = common.getUpdate().getMessage().getFrom().getId();
        this.messageId = common.getUpdate().getMessage().getMessageId();
        this.username = common.getUpdate().getMessage().getFrom().getUserName();
        this.firstname = common.getUpdate().getMessage().getFrom().getFirstName();
        this.lastname = common.getUpdate().getMessage().getFrom().getLastName();
    }

    private static List<DeleteMessage> deleteSystemMessages = new ArrayList<>();

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
            if (!isSubscribeChannel()) {  // 200ms

                // 删除消息.
                // System.out.println("删除消息 " + messageId);
                deleteMessage(chatId, messageId);  // 0ms
                // 发送系统消息
                // System.out.println("发送系统消息");
                Integer msgId = sendSystemMessage(chatId);  // 800ms
                //删除任务
                processDeleteSystemMessage(msgId);  // 0ms
                // System.out.println("删除任务" + msgId);
            }  // 1s ~ 1.5s
        }

    }

    @Async
    private void deleteMessage(String chatId,Integer messageId){
        DeleteMessage deleteMessage = new DeleteMessage(chatId, messageId);
        common.executeAsync(deleteMessage);
    }

    @Async
    private Integer sendSystemMessage(String chatId) {
        SendMessage response = new SendMessage(chatId, generate_warning_text());
        Integer msgId = common.executeAsync(response);
        return msgId;
    }
    @Async
    private void processDeleteSystemMessage(Integer msgId){
        DeleteMessage deleteSystemMessage = new DeleteMessage(chatId, msgId);
        deleteSystemMessages.add(deleteSystemMessage);
        if (deleteSystemMessages.size() == 10) {
            MessageTaskExecutor taskExecutor = new MessageTaskExecutor(this.common);
            taskExecutor.executeDeleteMessageTask(deleteSystemMessages, deleteSeconds);
            // common.deleteMessageTask(deleteSystemMessages, deleteSeconds);
            deleteSystemMessages = new ArrayList<>();
        }
    }

    private boolean isSubscribeChannel() {
        String parseId = String.valueOf(this.channel_id);
        GetChatMember getChatMember = new GetChatMember(parseId, this.userId);
        boolean status = this.common.executeAsync(getChatMember).equals("left") ? false : true;

        return status;
    }

    private String generate_warning_text() {

        if (this.username == null) {
            this.firstname = this.firstname == null ? "" : this.firstname;
            this.lastname = this.lastname == null ? "" : this.lastname;
            this.username = this.firstname + this.lastname;
        } else {
            this.username = "@" + this.username;
        }

        return SpringyBotEnum.warning_text(this.username, this.channel_title);
    }

}
