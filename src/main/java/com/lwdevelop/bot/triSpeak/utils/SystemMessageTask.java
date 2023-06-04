package com.lwdevelop.bot.triSpeak.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import com.lwdevelop.bot.Common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class SystemMessageTask extends TimerTask {
    
    private HashMap<Long, HashMap<Long, String>> warnMessageMap;
    private String subscriptionWarnText;
    private final static InlineKeyboardMarkup inlineKeyboardButton = new KeyboardButton().ddb37_url();
    private int deleteSeconds;
    private Common common;

    @Override
    public void run() {
        System.out.println(warnMessageMap);
        for (Map.Entry<Long, HashMap<Long, String>> entry : warnMessageMap.entrySet()) {
            Long chatId = entry.getKey();
            HashMap<Long, String> message = entry.getValue();
            int messageSize = message.size();

            if (messageSize > 0) {
                StringBuilder textBuilder = new StringBuilder();
                for (String m : message.values()) {
                    textBuilder.append(m).append("\n");
                }
                textBuilder.append(subscriptionWarnText);

                SendMessage response = new SendMessage(String.valueOf(chatId), textBuilder.toString());
                response.setReplyMarkup(inlineKeyboardButton);

                Integer msgId = common.executeAsync(response);
                DeleteMessage deleteMessage = new DeleteMessage(String.valueOf(chatId), msgId);
                common.deleteMessageTask(deleteMessage, deleteSeconds);
            }
        }
    }
}
