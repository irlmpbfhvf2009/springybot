package com.lwdevelop.botfactory;

import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import org.springframework.scheduling.annotation.Async;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.groupadministration.ExportChatInviteLink;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChat;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.groupadministration.RestrictChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class Common {

    private Long springyBotId;

    private Long botId;

    private String username;

    private Update update;

    private String inviteLink;

    private TelegramLongPollingBot bot;

    // 用来存储用户的状态(会话)
    private HashMap<Long, String> userState;

    private HashMap<Long, List<String>> groupMessageMap;
    private HashMap<Long, List<String>> groupMessageMap2;

    public Common(Long springyBotId, Long botId, String username) {
        this.springyBotId = springyBotId;
        this.botId = botId;
        this.username = username;
    }

    @Async
    public void deleteMessageTask(List<DeleteMessage> deleteSystemMessages, int second) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (DeleteMessage deleteMessage : deleteSystemMessages) {
                    executeAsync(deleteMessage);
                }
            }
        }, second * 1000);
    }

    @Async
    public void deleteMessageTask(DeleteMessage deleteSystemMessage, int second) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                executeAsync(deleteSystemMessage);
            }
        }, second * 1000);
    }

    @Async
    @SneakyThrows
    public Integer executeAsync(SendMessage sendMessage) {
        return this.bot.executeAsync(sendMessage).get().getMessageId();
    }

    @Async
    @SneakyThrows
    public void executeAsync_(SendMessage sendMessage) {
        this.bot.execute(sendMessage);
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
    public Boolean executeAsync(GetChatMember getChatMember) {
        String status;
        try {
            status = this.bot.executeAsync(getChatMember).get().getStatus();
            if (status.equals("left") || status.equals("kicked")) {
                return false;
            } else {
                return true;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            SendMessage sendMessage = new SendMessage(getChatMember.getUserId().toString(), "调用资料失败");
            this.executeAsync_(sendMessage);
            e.printStackTrace();
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Async
    @SneakyThrows
    public ChatMember getChatMember(GetChatMember getChatMember) {
        return this.bot.executeAsync(getChatMember).get();
    }

    @Async
    @SneakyThrows
    public Chat executeAsync(GetChat getChat) {
        return this.bot.executeAsync(getChat).get();
    }

    @Async
    @SneakyThrows
    public void executeAsync(RestrictChatMember restrictChatMember) {
        this.bot.executeAsync(restrictChatMember);
    }

    public String formatBot() {
        return this.username + "[" + this.botId + "]";
    }

    public String formatUser(User user) {
        String firstname = user.getFirstName() == null ? "" : user.getFirstName();
        String username = user.getUserName() == null ? "" : user.getUserName();
        String lastname = user.getLastName() == null ? "" : user.getLastName();
        String name = username == null ? firstname + lastname : username;
        return name + "[" + user.getId() + "]";
    }

    public void dealInviteLink(Long chatId) {
        try {
            String inviteLink = this.bot.execute(new ExportChatInviteLink(String.valueOf(chatId)));
            setInviteLink(inviteLink);
        } catch (TelegramApiException e) {
            setInviteLink("");
            log.error(e.toString());
        }
    }
}
