package com.lwdevelop.bot.triSpeak.handler;

import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.User;
import com.lwdevelop.bot.triSpeak.utils.Common;
import com.lwdevelop.bot.triSpeak.utils.SpringyBotEnum;
import com.lwdevelop.dto.ConfigDTO;
import com.lwdevelop.entity.SpringyBot;
import com.lwdevelop.service.impl.SpringyBotServiceImpl;
import com.lwdevelop.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GroupMessage {

    private Common common;
    private String chatId;
    private Long userId;
    private Integer messageId;
    private Boolean followChannelSet;
    private int deleteSeconds;
    private Long channel_id;
    private String channel_title;

    @Autowired
    private SpringyBotServiceImpl springyBotServiceImpl = SpringUtils.getApplicationContext()
            .getBean(SpringyBotServiceImpl.class);

    public GroupMessage(Common common) {
        this.common = common;
        this.chatId = String.valueOf(common.getUpdate().getMessage().getChatId());
        this.userId = common.getUpdate().getMessage().getFrom().getId();
        this.messageId = common.getUpdate().getMessage().getMessageId();
    }

    public void handler() {
        HashMap<Long,ConfigDTO> configDTO_map = this.common.getConfigDTO_map();
        if(configDTO_map.get(common.getSpringyBotId()) == null){
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

        long startTime = System.currentTimeMillis();
        if (followChannelSet) {
            if (!isSubscribeChannel()) {
                DeleteMessage deleteMessage = new DeleteMessage(chatId, messageId);
                common.executeAsync(deleteMessage);
                SendMessage response = new SendMessage(chatId, generate_warning_text());
                Integer msgId = common.executeAsync(response);
                common.deleteMessageTask(chatId, msgId, deleteSeconds);
            }
        }
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        System.out.println("Execution time: " + executionTime + "ms");

    }

    private boolean isSubscribeChannel() {
        String parseId = String.valueOf(this.channel_id);
        GetChatMember getChatMember = new GetChatMember(parseId, this.userId);
        boolean status;
        try {
            status = this.common.executeAsync(getChatMember).equals("left") ? false : true;
        } catch (Exception e) {
            status = false;
            log.error(e.toString());
        }
        return status;
    }

    private String generate_warning_text() {
        User user = this.common.getUpdate().getMessage().getFrom();
        String username = user.getUserName();
        String firstname = user.getFirstName();
        String lastname = user.getLastName();

        if (username == null) {
            firstname = firstname == null ? "" : firstname;
            lastname = lastname == null ? "" : lastname;
            username = firstname + lastname;
        }

        return SpringyBotEnum.warning_text(username, this.channel_title);
    }

}
