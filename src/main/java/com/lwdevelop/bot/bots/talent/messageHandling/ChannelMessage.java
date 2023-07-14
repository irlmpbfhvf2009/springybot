package com.lwdevelop.bot.bots.talent.messageHandling;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import com.lwdevelop.bot.bots.utils.Common;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChannelMessage {
    
    private Common common;
    private String text;
    private String chatId;
    private Integer messageId;

    public ChannelMessage(Common common){
        this.common = common;
        this.text = common.getUpdate().getChannelPost().getText();
        this.chatId = String.valueOf(common.getUpdate().getChannelPost().getChatId());
        this.messageId = common.getUpdate().getChannelPost().getMessageId();
    }

    public void handler() {

        if(text.startsWith("https://t.me/")){
            log.info("Detected URL in message with ID: {} in channel with ID: {}. Deleting message...", messageId, chatId);
            DeleteMessage dm = new DeleteMessage(chatId,messageId);
            common.executeAsync(dm);
        }
        
    }
}
