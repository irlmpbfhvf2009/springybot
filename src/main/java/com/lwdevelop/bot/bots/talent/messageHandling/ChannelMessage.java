package com.lwdevelop.bot.bots.talent.messageHandling;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import com.lwdevelop.bot.bots.utils.Common;
import com.lwdevelop.bot.chatMessageHandlers.BaseChannelMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChannelMessage extends BaseChannelMessage{

    public ChannelMessage(Common common){
        super(common);
    }

    @Override
    public void handler() {
        if(text != null && text.startsWith("https://t.me/")){
            log.info("Detected URL in message with ID: {} in channel with ID: {}. Deleting message...", messageId, chatId_str);
            DeleteMessage dm = new DeleteMessage(chatId_str,messageId);
            common.executeAsync(dm);
        }
        
    }
}
