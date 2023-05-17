package com.lwdevelop.bot.triSpeak.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.springframework.scheduling.annotation.Async;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChat;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.ChatMemberUpdated;
import org.telegram.telegrambots.meta.api.objects.Update;
import com.lwdevelop.dto.ConfigDTO;
import lombok.Data;
import lombok.SneakyThrows;

@Data
public class Common {

    private Long springyBotId;

    private Long botId;

    private String username;

    private Update update;

    private String inviteLink;

    private TelegramLongPollingBot bot;

    private ChatMemberUpdated chatMemberUpdated;

    // 用来存储用户的状态(会话)
    private HashMap<Long, String> userState;

    private HashMap<Long, ConfigDTO> configDTO_map;

    public Common(Long springyBotId, Long botId, String username) {
        this.springyBotId = springyBotId;
        this.botId = botId;
        this.username = username;
    }

    @Async
    public void deleteMessageTask(List<DeleteMessage> deleteSystemMessage, int second) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (DeleteMessage deleteMessage : deleteSystemMessage) {
                    executeAsync(deleteMessage);
                }
            }
        }, second);
    }

    @Async
    @SneakyThrows
    public Integer executeAsync(SendMessage sendMessage) {
        return this.bot.executeAsync(sendMessage).get().getMessageId();
    }

    @Async
    @SneakyThrows
    public Integer executeAsync(SendPhoto sendPhoto) {
        return this.bot.executeAsync(sendPhoto).get().getMessageId();
    }

    @Async
    @SneakyThrows
    public void executeAsync(EditMessageText editMessageText) {
        this.bot.executeAsync(editMessageText);
    }

    @Async
    @SneakyThrows
    public void executeAsync(DeleteMessage deleteMessage) {
        this.bot.executeAsync(deleteMessage);
    }

    @Async
    @SneakyThrows
    public String executeAsync(GetChatMember getChatMember) {
        return this.bot.executeAsync(getChatMember).get().getStatus();
    }

    @Async
    @SneakyThrows
    public Chat executeAsync(GetChat getChat) {
            return this.bot.executeAsync(getChat).get();
    }

}
