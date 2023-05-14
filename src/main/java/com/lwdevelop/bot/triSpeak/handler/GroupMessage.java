package com.lwdevelop.bot.triSpeak.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.User;
import com.lwdevelop.bot.triSpeak.utils.Common;
import com.lwdevelop.bot.triSpeak.utils.SpringyBotEnum;
import com.lwdevelop.entity.SpringyBot;
import com.lwdevelop.service.impl.SpringyBotServiceImpl;
import com.lwdevelop.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GroupMessage {

    private Common common;
    private String chatId;
    private Long userId;
    private Long channel_id;
    private String channel_title;
    private Integer messageId;
    private int deleteSeconds;

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

        SpringyBot springyBot = springyBotServiceImpl.findById(common.getSpringyBotId()).get();
        Boolean followChannelSet = springyBot.getConfig().getFollowChannelSet();
        this.channel_id = springyBot.getConfig().getFollowChannelSet_chatId();
        this.channel_title = springyBot.getConfig().getFollowChannelSet_chatTitle();
        this.deleteSeconds = springyBot.getConfig().getDeleteSeconds();

        if (followChannelSet) {

            if (!isSubscribeChannel()) {
                DeleteMessage deleteMessage = new DeleteMessage(chatId, messageId);
                common.executeAsync(deleteMessage);

                SendMessage response = new SendMessage(chatId, generate_warning_text());
                Integer msgId = common.executeAsync(response);
                common.deleteMessageTask(chatId, msgId, this.deleteSeconds);
            }

        }
    }

    private boolean isSubscribeChannel() {
        String parseId = String.valueOf(this.channel_id);
        GetChatMember getChatMember = new GetChatMember(parseId, this.userId);
        boolean status;
        try {
            status = common.executeAsync(getChatMember).equals("left") ? false : true;
        } catch (Exception e) {
            status = false;
            log.error(e.toString());
        }
        return status;
    }

    private String generate_warning_text() {
        User user = common.getUpdate().getMessage().getFrom();
        String username = user.getUserName();
        String firstname = user.getFirstName();
        String lastname = user.getLastName();

        if (username == null) {
            firstname = firstname == null ? "" : firstname;
            lastname = lastname == null ? "" : lastname;
            username = firstname + lastname;
        }

        return SpringyBotEnum.warning_text(username,this.channel_title);
    }

}
