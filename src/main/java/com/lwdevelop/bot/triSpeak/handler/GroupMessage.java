package com.lwdevelop.bot.triSpeak.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;

import com.lwdevelop.bot.triSpeak.utils.Common;
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
    private Integer messageId;

    @Autowired
    private SpringyBotServiceImpl springyBotServiceImpl = SpringUtils.getApplicationContext()
            .getBean(SpringyBotServiceImpl.class);

    public GroupMessage(Common common){
        this.common=common;
        this.chatId=String.valueOf(common.getUpdate().getMessage().getChatId());
        this.userId = common.getUpdate().getMessage().getFrom().getId();
        this.messageId = common.getUpdate().getMessage().getMessageId();
    }
    public void handler() {

        SpringyBot springyBot = springyBotServiceImpl.findById(common.getSpringyBotId()).get();
        Boolean followChannelSet = springyBot.getConfig().getFollowChannelSet();
        this.channel_id = springyBot.getConfig().getFollowChannelSet_chatId();

        SendMessage response = new SendMessage();
        response.setChatId(chatId);

        if(followChannelSet){

            if(isSubscribeChannel()){
                response.setText("通过");
            }else{
                DeleteMessage deleteMessage = new DeleteMessage(chatId, messageId);
                common.executeAsync(deleteMessage);
                response.setText("@Leo : 关注频道 @rc499 后才能发言");
            }
            common.executeAsync(response);

        }
    }

    public boolean isSubscribeChannel() {
        String parseId = String.valueOf(this.channel_id);
        GetChatMember getChatMember = new GetChatMember(parseId,this.userId);
        boolean status;
        try{
            status = common.executeAsync(getChatMember).equals("left") ? false : true;
        }catch(Exception e){
            status = false;
            log.error(e.toString());
        }
        return status;
    }

}
