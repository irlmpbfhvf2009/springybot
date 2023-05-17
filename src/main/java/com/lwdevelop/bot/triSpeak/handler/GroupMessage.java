package com.lwdevelop.bot.triSpeak.handler;

import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import com.lwdevelop.bot.triSpeak.utils.Common;
import com.lwdevelop.bot.triSpeak.utils.MessageQueue;
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
                // 删除消息
                DeleteMessage deleteMessage = new DeleteMessage(chatId, messageId);
                common.executeAsync(deleteMessage);
                // 发送系统消息及删除任务
                SendMessage response = new SendMessage(chatId, generate_warning_text());
                MessageQueue.enqueue(new MessageQueueTask(deleteMessage, response, chatId, deleteSeconds));
                // Integer msgId = common.executeAsync(response);
                // common.deleteMessageTask(chatId, msgId, deleteSeconds);
            }
        }

    }
    public class MessageQueueTask implements Runnable {
        private final DeleteMessage deleteMessage;
        private final SendMessage sendMessage;
        private final String chatId;
        private final Integer deleteSeconds;
    
        public MessageQueueTask(DeleteMessage deleteMessage, SendMessage sendMessage, String chatId, Integer deleteSeconds) {
            this.deleteMessage = deleteMessage;
            this.sendMessage = sendMessage;
            this.chatId = chatId;
            this.deleteSeconds = deleteSeconds;
        }
    
        @Override
        public void run() {
            // 删除消息
            common.executeAsync(deleteMessage);
            
            // 发送系统消息及删除任务
            Integer msgId = common.executeAsync(sendMessage);
            common.deleteMessageTask(chatId, msgId, deleteSeconds);
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
        }

        return SpringyBotEnum.warning_text(this.username, this.channel_title);
    }

}
