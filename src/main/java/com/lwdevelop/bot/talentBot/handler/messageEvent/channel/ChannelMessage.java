package com.lwdevelop.bot.talentBot.handler.messageEvent.channel;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import com.lwdevelop.bot.talentBot.utils.Common;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChannelMessage {
    public void handler(Common common) {
        String text = common.getUpdate().getChannelPost().getText();
        String chatId = String.valueOf(common.getUpdate().getChannelPost().getChatId());
        Integer messageId = common.getUpdate().getChannelPost().getMessageId();

        if(text.startsWith("https://t.me/")){
            log.info("Detected URL in message with ID: {} in channel with ID: {}. Deleting message...", messageId, chatId);
            DeleteMessage dm = new DeleteMessage(chatId,messageId);
            try {
                common.getBot().execute(dm);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
        
    }
}
